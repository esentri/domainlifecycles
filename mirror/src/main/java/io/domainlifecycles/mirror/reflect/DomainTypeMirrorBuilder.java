/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract Builder to create {@link DomainTypeMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public abstract class DomainTypeMirrorBuilder<T extends DomainTypeMirror> {

    private static final Logger log = LoggerFactory.getLogger(DomainTypeMirrorBuilder.class);

    protected final Class<?> domainClass;

    protected final List<Field> fields;
    protected final GenericTypeResolver genericTypeResolver;

    /**
     * Build and return the built mirror
     * @return built mirror
     */
    public abstract T build();

    /**
     * Constructor
     *
     * @param domainClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     */
    public DomainTypeMirrorBuilder(final Class<?> domainClass, GenericTypeResolver genericTypeResolver) {
        this.domainClass = domainClass;
        this.genericTypeResolver = genericTypeResolver;
        List<Field> theFields = Collections.emptyList();
        try {
            theFields = JavaReflect.fields(domainClass, MemberSelect.HIERARCHY);
        }catch (Throwable t){
            log.error("Accessing fields for {} failed!", domainClass.getName(), t);
        }
        this.fields = theFields;
    }

    protected List<FieldMirror> buildFields() {
        return fields
            .stream()
            .filter(f -> !f.isSynthetic())
            .map(f -> {
                    try {
                        return new FieldMirrorBuilder(
                            f,
                            domainClass,
                            isHidden(f),
                            genericTypeResolver
                        ).build();
                    }catch (Throwable t) {
                        //ignore
                        log.error("Building FieldMirror failed {}.{}", domainClass.getName(), f.getName(), t);
                        return null;
                    }
                }
            )
            .filter(Objects::nonNull)
            .toList();
    }

    protected boolean isHidden(Field f) {
        return fields.stream().anyMatch(c -> isHiddenBy(f, c));
    }

    private boolean isHiddenBy(Field f, Field candidate) {
        if (f.equals(candidate) || !f.getDeclaringClass().isAssignableFrom(candidate.getDeclaringClass())) {
            return false;
        }
        return f.getName().equals(candidate.getName());
    }

    protected List<MethodMirror> buildMethods() {
        var meth = JavaReflect.methods(domainClass, MemberSelect.HIERARCHY);
        return meth.stream()
            .filter(m -> !m.isSynthetic() && !m.isBridge())
            .map(m -> {
                    try{
                        return new MethodMirrorBuilder(m, domainClass, isOverridden(m, meth), genericTypeResolver).build();
                    }catch (Throwable t){
                        //ignore
                        log.error("Building MethodMirror failed {}.{}", domainClass.getName(), m.getName(), t);
                        return null;
                    }
                }
            )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private boolean isOverridden(Method m, List<Method> candidates) {
        return candidates.stream().anyMatch(c -> isOverriddenBy(m, c));
    }


    private boolean isOverriddenBy(Method m, Method candidate) {
        if (m.equals(candidate) || !m.getDeclaringClass().isAssignableFrom(candidate.getDeclaringClass())) {
            return false;
        }
        if (!m.getName().equals(candidate.getName())) {
            return false;
        }
        if (!candidate.getReturnType().equals(m.getReturnType())) {
            return false;
        }
        if (!candidateIsAsOrLessRestrictive(m, candidate)) {
            return false;
        }
        Class<?>[] subclassParamTypes = m.getParameterTypes();
        Class<?>[] superclassParamTypes = candidate.getParameterTypes();

        if (subclassParamTypes.length != superclassParamTypes.length) {
            return false;
        }
        for (int i = 0; i < subclassParamTypes.length; i++) {
            if (!subclassParamTypes[i].equals(superclassParamTypes[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean candidateIsAsOrLessRestrictive(Method m, Method candidate) {
        int subclassModifiers = m.getModifiers();
        int superclassModifiers = candidate.getModifiers();
        return subclassModifiers <= superclassModifiers;
    }

    protected List<String> buildInheritanceHierarchy() {
        var superClass = domainClass.getSuperclass();
        var hierarchy = new ArrayList<String>();
        if (superClass != null) {
            hierarchy.add(superClass.getName());
            while (superClass != null && !superClass.getName().equals(Object.class.getName())) {
                superClass = superClass.getSuperclass();
                if (superClass != null) {
                    hierarchy.add(superClass.getName());
                }
            }
        }
        return hierarchy;
    }

    protected List<String> buildInterfaceTypes() {
        return Arrays.stream(domainClass.getInterfaces())
            .map(Class::getName)
            .toList();
    }

    protected String getTypeName() {
        return domainClass.getName();
    }

    protected boolean isAbstract() {
        return JavaReflect.isAbstract(domainClass);
    }
}

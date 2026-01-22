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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.FieldMirrorBuilder;
import io.domainlifecycles.mirror.reflect.MethodMirrorBuilder;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Builder to create {@link DomainTypeMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public abstract class DomainTypeMirrorBuilder<T extends DomainTypeMirror> {

    private static final Logger log = LoggerFactory.getLogger(DomainTypeMirrorBuilder.class);

    /**
     * Represents the class being mirrored. This variable holds the domain class type
     * that is used as the basis for building type information and reflective metadata
     * within the `DomainTypeMirrorBuilder`.
     *
     * It is a final, protected field to ensure immutability after initialization and
     * restricts access to the containing class or subclasses.
     */
    protected final Class<?> domainClass;

    /**
     * A list of fields that are part of the domain class being mirrored.
     * These fields are analyzed and processed during the mirror-building process.
     */
    protected final List<Field> fields;

    /**
     * A specialized {@link GenericTypeResolver} instance used to resolve generic types and type arguments
     * for fields, methods, and other domain-specific elements within a given class context.
     *
     * This field is a final and protected member, ensuring that it is immutable and accessible
     * by subclasses of {@link DomainTypeMirrorBuilder}. It provides essential functionality
     * for resolving and handling type-related information during the building of domain type mirrors.
     */
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

    /**
     * Builds a list of {@link FieldMirror} instances by processing the fields of the associated domain class.
     * Filters out synthetic fields, constructs each field's corresponding {@link FieldMirror} using the
     * {@link FieldMirrorBuilder}, and excludes any fields that fail during the building process.
     *
     * @return a list of {@link FieldMirror} instances representing the non-synthetic, successfully constructed fields of the domain class
     */
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

    /**
     * Determines if the specified field is hidden by any other field in the current context.
     *
     * @param f the field to check for hidden status
     * @return true if the specified field is hidden, false otherwise
     */
    protected boolean isHidden(Field f) {
        return fields.stream().anyMatch(c -> isHiddenBy(f, c));
    }

    /**
     * Determines if a given field is hidden by another candidate field.
     *
     * A field is considered hidden by another if they belong to related classes
     * (i.e., the declaring class of the candidate is assignable from the declaring class of the field)
     * and have the same name, but are not the same instance.
     *
     * @param f the original field to check
     * @param candidate the candidate field to determine if it hides the original field
     * @return true if the candidate field hides the given field, false otherwise
     */
    private boolean isHiddenBy(Field f, Field candidate) {
        if (f.equals(candidate) || !f.getDeclaringClass().isAssignableFrom(candidate.getDeclaringClass())) {
            return false;
        }
        return f.getName().equals(candidate.getName());
    }

    /**
     * Builds a list of {@link MethodMirror} instances by processing the methods of the associated domain class.
     * Filters out synthetic and bridge methods, constructs each method's corresponding {@link MethodMirror} using
     * the {@link MethodMirrorBuilder}, determines whether each method is overridden, and excludes any methods
     * that fail during the building process.
     *
     * @return a list of {@link MethodMirror} instances representing the non-synthetic and non-bridge methods
     * of the domain class that were successfully constructed.
     */
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

    /**
     * Constructs the inheritance hierarchy of the associated domain class, starting from its immediate superclass
     * and continuing up the hierarchy until reaching the root {@code java.lang.Object} class.
     *
     * The hierarchy is represented as a list of fully qualified class names, ordered from the closest
     * superclass of the domain class to the {@code java.lang.Object}.
     *
     * @return a list of fully qualified class names representing the inheritance hierarchy of the domain class.
     */
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

    /**
     * Builds a list of fully qualified names of the interfaces implemented by the associated domain class.
     * The method retrieves all the interfaces of the domain class and converts their class objects into their
     * fully qualified names.
     *
     * @return a list of fully qualified interface names implemented by the domain class
     */
    protected List<String> buildInterfaceTypes() {
        return Arrays.stream(domainClass.getInterfaces())
            .map(Class::getName)
            .toList();
    }

    /**
     * Retrieves the fully qualified name of the associated domain class.
     *
     * @return the fully qualified name of the domain class
     */
    protected String getTypeName() {
        return domainClass.getName();
    }

    /**
     * Determines whether the associated domain class is abstract.
     *
     * @return true if the domain class is abstract, false otherwise
     */
    protected boolean isAbstract() {
        return JavaReflect.isAbstract(domainClass);
    }
}

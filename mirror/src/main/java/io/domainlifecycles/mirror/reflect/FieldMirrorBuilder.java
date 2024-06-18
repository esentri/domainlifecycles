/*
 *
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

import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.model.ValueReferenceModel;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.model.AggregateRootReferenceModel;
import io.domainlifecycles.mirror.model.EntityReferenceModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.reflect.JavaReflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Builder to create {@link FieldMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class FieldMirrorBuilder {
    private final Field field;

    private final Class<?> topLevelClass;
    private final AssertedContainableTypeMirrorBuilder typeMirrorBuilder;
    private final boolean hidden;

    public FieldMirrorBuilder(Field field, Class<?> topLevelClass, boolean hidden) {
        this.field = field;
        this.topLevelClass = Objects.requireNonNull(topLevelClass, "The corresponding top level class cannot be null!");
        this.typeMirrorBuilder = new AssertedContainableTypeMirrorBuilder(
            field.getType(),
            field.getAnnotatedType(),
            field.getGenericType(),
            Domain.getGenericTypeResolver().resolveFieldType(field, topLevelClass)
        );
        this.hidden = hidden;
    }

    private static final String GETTER_PREFIX = "get";
    private static final String BOOL_GETTER_PREFIX = "is";
    private boolean isPublicReadable() {
        if (JavaReflect.isPublic(field)) {
            return true;
        }
        Method mGetter = null;
        Method mBoolGetter = null;
        Method mReader = null;
        String upperFirst = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            mGetter = field.getDeclaringClass().getMethod(GETTER_PREFIX + upperFirst);
        } catch (NoSuchMethodException e) {
            //ignore
        }
        try {
            mBoolGetter = field.getDeclaringClass().getMethod(BOOL_GETTER_PREFIX + upperFirst);
        } catch (NoSuchMethodException e) {
            //ignore
        }
        try {
            mReader = field.getDeclaringClass().getMethod(field.getName());
        } catch (NoSuchMethodException e) {
            //ignore
        }
        return (mGetter != null && Modifier.isPublic(mGetter.getModifiers()) && mGetter.getParameterCount() == 0 && mGetter.getReturnType().equals(field.getType()))
            || (mBoolGetter != null && Modifier.isPublic(mBoolGetter.getModifiers()) && mBoolGetter.getParameterCount() == 0 && mBoolGetter.getReturnType().equals(field.getType()))
            || (mReader != null && Modifier.isPublic(mReader.getModifiers()) && mReader.getParameterCount() == 0 && mReader.getReturnType().equals(field.getType()));
    }

    private static final String SETTER_PREFIX = "set";
    private boolean isPublicWriteable() {
        if(JavaReflect.isFinal(field)){
            return false;
        }
        if (JavaReflect.isPublic(field)) {
            return true;
        }
        Method mSetter = null;
        Method mWriter = null;
        String upperFirst = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            mSetter = field.getDeclaringClass().getMethod(SETTER_PREFIX + upperFirst);

        } catch (NoSuchMethodException e) {
            //ignore
        }
        try {
            mWriter = field.getDeclaringClass().getMethod(field.getName());
        } catch (NoSuchMethodException e) {
            //ignore
        }
        return (mSetter != null
            && Modifier.isPublic(mSetter.getModifiers())
            && mSetter.getParameterCount() == 1
            && mSetter.getReturnType().equals(void.class)
            && mSetter.getParameterTypes()[0].equals(typeMirrorBuilder.getBasicType()))
            || (mWriter != null
            && Modifier.isPublic(mWriter.getModifiers())
            && mWriter.getParameterCount() == 1
            && mWriter.getReturnType().equals(void.class)
            && mWriter.getParameterTypes()[0].equals(typeMirrorBuilder.getBasicType()));
    }

    /**
     * Creates a new {@link FieldMirror}.
     */
    public FieldMirror build(){
        var name = field.getName();
        var declaredByTypeName = field.getDeclaringClass().getName();
        var isModifiable = !JavaReflect.isFinal(field);
        var isPublicReadable = isPublicReadable();
        var isPublicWriteable = isPublicWriteable();
        var typeMirror = typeMirrorBuilder.build();
        var accessLevel = AccessLevel.of(field);
        var isStatic = JavaReflect.isStatic(field);

        if(DomainType.ENTITY.equals(typeMirror.getDomainType())){
            return new EntityReferenceModel(
                name,
                typeMirror,
                accessLevel,
                declaredByTypeName,
                isModifiable,
                isPublicReadable,
                isPublicWriteable,
                isStatic,
                hidden
            );
        }else if(DomainType.VALUE_OBJECT.equals(typeMirror.getDomainType())
            || DomainType.ENUM.equals(typeMirror.getDomainType())
            || DomainType.IDENTITY.equals(typeMirror.getDomainType())
        ) {
            return new ValueReferenceModel(
                name,
                typeMirror,
                accessLevel,
                declaredByTypeName,
                isModifiable,
                isPublicReadable,
                isPublicWriteable,
                isStatic,
                hidden
            );
        }else if(DomainType.AGGREGATE_ROOT.equals(typeMirror.getDomainType())) {
            return new AggregateRootReferenceModel(
                name,
                typeMirror,
                accessLevel,
                declaredByTypeName,
                isModifiable,
                isPublicReadable,
                isPublicWriteable,
                isStatic,
                hidden
            );
        }

        return new FieldModel(
             name,
             typeMirror,
             accessLevel,
             declaredByTypeName,
             isModifiable,
             isPublicReadable,
             isPublicWriteable,
             isStatic,
             hidden
        );
    }

}

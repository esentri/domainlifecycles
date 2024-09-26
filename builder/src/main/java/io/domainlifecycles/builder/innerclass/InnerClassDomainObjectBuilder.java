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

package io.domainlifecycles.builder.innerclass;

import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.builder.AbstractDomainObjectBuilder;
import io.domainlifecycles.builder.DomainBuilderConfiguration;
import io.domainlifecycles.domain.types.internal.DomainObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation wrapping inner class domain object builders, so that DLC can use them internally.
 *
 * @param <T> type for which the domain object builder delivers new domain object instances
 *
 * @author Dominik Galler
 */
public final class InnerClassDomainObjectBuilder<T extends DomainObject> extends AbstractDomainObjectBuilder<T> {

    private final Object builderInstance;
    private final Class<?> builderClass;
    private DomainBuilderConfiguration domainBuilderConfiguration = new InnerClassDefaultDomainBuilderConfiguration();
    private final Map<String, Field> fieldMap = new HashMap<>();

    /**
     * This implementation relies on the domain structure information within the domain mirror.
     *
     * @param builderInstance instance of the builder
     */
    public InnerClassDomainObjectBuilder(Object builderInstance) {
        //noinspection unchecked
        super((Class<T>) builderInstance.getClass().getEnclosingClass());
        this.builderInstance = builderInstance;
        this.builderClass = this.builderInstance.getClass();
        Field[] fields = builderClass.getDeclaredFields();
        for(var f: fields){
            if(!f.canAccess(this.builderInstance)){
                f.trySetAccessible();
            }
            fieldMap.put(f.getName(), f);
        }
    }

    /**
     * This implementation relies on the domain structure information within the domain mirror and the passed
     * {@link DomainBuilderConfiguration}instance.
     *
     * @param builderInstance of the "inner" builder
     * @param domainBuilderConfiguration for builder
     */
    public InnerClassDomainObjectBuilder(Object builderInstance,
                                         DomainBuilderConfiguration domainBuilderConfiguration) {
        this(builderInstance);
        this.domainBuilderConfiguration = domainBuilderConfiguration;
    }

    /**
     * Creates a new DomainObject instance.
     *
     * @return the instance created.
     */
    @Override
    public T build() {
        return uncheckedCast(doBuild());
    }

    /**
     * Check, if the builder provides a setter for the given property name.
     * @param fieldName of field addressed
     * @return true if the inner builder instance provides a corresponding setter method.
     */
    @Override
    public boolean canInstantiateField(String fieldName) {
        return Arrays.stream(builderClass.getDeclaredMethods())
            .anyMatch(dm ->
                dm.getName().equals(getSetterNameFromFieldName(fieldName))
                    && dm.getParameterCount() == 1);
    }

    @Override
    public Object getBuilderInstance() {
        return builderInstance;
    }

    private DomainObject doBuild() {
        try {
            var m = builderClass.getDeclaredMethod(domainBuilderConfiguration.buildMethodName());
            return (DomainObject) m.invoke(builderInstance);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw DLCBuilderException.fail("Was not able to build DomainObjectBuilder! BuilderCLass('%s').",
                e,
                builderClass
            );
        }
    }

    private String getSetterNameFromFieldName(String propertyName) {
        return domainBuilderConfiguration.setterFromPropertyName(propertyName);
    }

    /**
     * Helps to avoid using {@code @SuppressWarnings({"unchecked"})} when casting to a generic type.
     * @param <K> type
     * @param obj object to avoid checks on
     * @return object
     */
    @SuppressWarnings({ "unchecked" })
    public <K> K uncheckedCast(Object obj) {
        return (K) obj;
    }

    @Override
    protected Object getValue(String name) {
        var f = fieldMap.get(name);
        try {
            return f.get(this.builderInstance);
        } catch (IllegalAccessException e) {
            throw DLCBuilderException.fail("Was not able to get value from DomainObjectBuilder! BuilderCLass('%s').",
                e,
                builderClass
            );
        }
    }

    @Override
    protected void setValue(String name, Object value) {
        if (value != null) {
            Method m;
            List<Method> methods = Arrays.stream(builderClass.getDeclaredMethods())
                .filter(dm -> dm.getName().equals(getSetterNameFromFieldName(name))
                    && dm.getParameterCount() == 1)
                .toList();
            if (methods.size() == 0) {
                throw DLCBuilderException.fail("Was not able to set property in DomainObjectBuilder! BuilderClass = "
                    + builderClass + ", Property = " + name);
            } else if (methods.size() == 1) {
                m = methods.get(0);
            } else {
                throw DLCBuilderException
                    .fail("Was not able to set property in DomainObjectBuilder. " +
                        "Multiple setters found in Lombok builder! BuilderClass = " + builderClass +
                        ", Property = " + name
                    );
            }
            Class<?> paramType = m.getParameterTypes()[0];
            Object param = value;
            if (Optional.class.equals(paramType) && !Optional.class.equals(value.getClass())) {
                param = Optional.of(value);
            } else if (Optional.class.equals(value.getClass()) && !Optional.class.equals(paramType)) {
                Optional<?> opt = (Optional<?>) value;
                param = opt.orElse(null);

            }
            try {
                m.invoke(this.builderInstance, param);
            } catch (IllegalArgumentException | IllegalAccessException |
                     InvocationTargetException e) {
                throw DLCBuilderException.fail("Was not able to set property in DomainObjectBuilder! BuilderClass = "
                    + builderClass + ", Property = " + name + ".", e
                );
            }
        }
    }
}

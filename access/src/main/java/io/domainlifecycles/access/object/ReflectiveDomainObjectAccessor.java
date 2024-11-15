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

package io.domainlifecycles.access.object;

import io.domainlifecycles.access.exception.DLCAccessException;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the {@link DynamicDomainObjectAccessor} by reflection.
 *
 * @author Mario Herb
 */
public class ReflectiveDomainObjectAccessor implements DynamicDomainObjectAccessor {
    private static Map<String, Field> fieldMap = new HashMap<>();
    private DomainObject domainObject;
    private Class<? extends DomainObject> domainObjectClass;

    /**
     * Initializes a new instance of ReflectiveDomainObjectAccessor with the provided domain object.
     *
     * @param domainObject the DomainObject instance to be accessed.
     */
    protected ReflectiveDomainObjectAccessor(DomainObject domainObject) {
        this.domainObject = domainObject;
        this.domainObjectClass = domainObject.getClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object peek(String fieldName) {
        try {
            var field = getField(fieldName);
            return field.get(domainObject);
        } catch (IllegalAccessException illegalAccessException) {
            throw DLCAccessException.fail(
                String.format("Failed to read '%s' from '%s'!", fieldName, domainObjectClass.getName()),
                illegalAccessException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void poke(String fieldName, Object argument) {
        try {
            var field = getField(fieldName);
            field.set(domainObject, argument);
        } catch (IllegalAccessException illegalAccessException) {
            throw DLCAccessException.fail(
                String.format("Failed to write '%s' in '%s'!", fieldName, domainObjectClass.getName()),
                illegalAccessException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assign(DomainObject domainObject) {
        this.domainObject = domainObject;
        this.domainObjectClass = domainObject.getClass();
    }

    @Override
    public DomainObject getAssigned() {
        return domainObject;
    }

    private Field getField(String fieldName) {
        var field = fieldMap.get(fieldKey(fieldName));
        if (field == null) {
            field = retrieveField(fieldName);
        }
        return field;
    }

    private synchronized Field retrieveField(String fieldName) {
        var field = JavaReflect
            .findField(domainObjectClass, MemberSelect.HIERARCHY, fieldName)
            .orElseThrow(() -> DLCAccessException.fail(
                String.format("Field '%s' not found in '%s'!", fieldName, domainObjectClass.getName())));
        field.trySetAccessible();
        var key = fieldKey(fieldName);
        fieldMap.put(key, field);
        return field;
    }

    private String fieldKey(String fieldName) {
        return domainObjectClass.getName() + "." + fieldName;
    }
}

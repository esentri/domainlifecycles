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

package io.domainlifecycles.jackson.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;

/**
 * This class provides several entry points to customize the mapping process of
 * {@link DomainObject}s.
 *
 * @param <T> type of mapped DomainObject
 * @author Mario Herb
 */
public abstract class JacksonMappingCustomizer<T extends DomainObject> {

    /**
     * The type for which the mapping is customized
     */
    public final Class<? extends T> instanceType;

    /**
     * Initialize the customizer with the corresponding type.
     *
     * @param instanceType the type of instance which will be mapped
     */
    public JacksonMappingCustomizer(Class<? extends T> instanceType) {
        this.instanceType = instanceType;
    }

    /**
     * Callback method, to specify the customization in the mapping process before reading a JSON field.
     * Reading means JSON -> Object transformation.
     *
     * @param mappingContext mapping information for DomainObject
     * @param fieldNode Jackson TreeNode
     * @param fieldName name of the field to be read
     * @param expectedType expected type of read value
     * @param codec Jackson codec
     * @return specifies if, the default mapping action for the specified field should be skipped or continued
     */
    public MappingAction beforeFieldRead(final DomainObjectMappingContext mappingContext, TreeNode fieldNode, String fieldName, Class<?> expectedType, ObjectCodec codec) {
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    /**
     * Callback method, to specify the customization in the mapping process after having read a JSON field.
     * Reading means JSON -> Object transformation.
     *
     * @param mappingContext mapping information for DomainObject
     * @param readValue read value
     * @param fieldName name of the field which value has been read
     * @param expectedType expected type of read value
     * @return specifies if, the default mapping action for the specified field should be skipped or continued
     */
    public MappingAction afterFieldRead(final DomainObjectMappingContext mappingContext, final Object readValue, String fieldName, Class<?> expectedType) {
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    /**
     * Callback method, to specify the customization in the mapping process before writing a JSON field.
     * Writing means Object -> JSON transformation.
     *
     * @param jsonGenerator Jackson JSON generator
     * @param fieldName name of the field to be mapped
     * @param fieldValue value to be written
     * @return specifies if, the default mapping action for the specified field should be skipped or continued
     */
    public MappingAction beforeFieldWrite(final JsonGenerator jsonGenerator, String fieldName, Object fieldValue) {
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    /**
     * Callback method, to specify the customization in the mapping process before reading an Object of the customizers {@link DomainObject} type .
     * Reading means JSON -> Object transformation.
     *
     * @param mappingContext mapping information for DomainObject
     * @param codec Jackson codec
     * @return specifies if, the default mapping action for the specified object should be skipped or continued
     */
    public MappingAction beforeObjectRead(final DomainObjectMappingContext mappingContext, ObjectCodec codec) {
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    /**
     * Callback method, to specify the customization in the mapping process before writing an Object of the customizers {@link DomainObject} type .
     * Writing means Object -> JSON transformation.
     *
     * @param jsonGenerator Jackson JSON generator
     * @param object object to be mapped
     * @return specifies if, the default mapping action for the specified object should be skipped or continued
     */
    public MappingAction beforeObjectWrite(final JsonGenerator jsonGenerator, T object) {
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    /**
     * Callback method, to specify the customization in the mapping process after having read an Object of the customizers {@link DomainObject} type .
     * Reading means JSON -> Object transformation.
     *
     * @param mappingContext mapping information for DomainObject
     * @param codec Jackson codec
     */
    public void afterObjectRead(final DomainObjectMappingContext mappingContext, ObjectCodec codec) {
    }
}

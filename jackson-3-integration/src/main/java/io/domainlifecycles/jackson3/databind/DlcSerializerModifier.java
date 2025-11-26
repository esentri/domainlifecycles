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

package io.domainlifecycles.jackson3.databind;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.SerializationConfig;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson3.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ser.ValueSerializerModifier;

/**
 * {@see BeanSerializerModifier}
 *
 * @author Mario Herb
 */
public class DlcSerializerModifier extends ValueSerializerModifier {

    /**
     * A container that holds registered customizers used for domain-specific
     * Jackson serialization and deserialization. The customizers within this
     * container provide specialized logic for customizing the mapping of
     * specific domain object types during the serialization process.
     */
    private final DlcJacksonModule.CustomizerContainer customizersContainer;

    /**
     * Initialize DlcSerializerModifier with the provided CustomizerContainer.
     *
     * @param customizersContainer the container for customizers used in serialization
     */
    public DlcSerializerModifier(DlcJacksonModule.CustomizerContainer customizersContainer) {
        this.customizersContainer = customizersContainer;
    }

    /**
     * Plug in {@link Domain} based serializer modifications
     */
    @Override
    public ValueSerializer<?> modifySerializer(SerializationConfig config, BeanDescription.Supplier beanDesc,
                                              ValueSerializer<?> serializer) {
        if (Entity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new EntitySerializer(
                (JacksonMappingCustomizer<Entity>) customizersContainer.findCustomizer(beanDesc.getBeanClass()));

        }
        if (ValueObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new ValueObjectSerializer(
                (JacksonMappingCustomizer<ValueObject>) customizersContainer.findCustomizer(beanDesc.getBeanClass()));
        }
        if (Identity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new IdentitySerializer();
        }
        return super.modifySerializer(config, beanDesc, serializer);
    }


}

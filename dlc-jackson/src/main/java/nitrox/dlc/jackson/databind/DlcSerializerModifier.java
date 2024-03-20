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

package nitrox.dlc.jackson.databind;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.ValueObject;
import nitrox.dlc.jackson.api.JacksonMappingCustomizer;
import nitrox.dlc.jackson.module.DlcJacksonModule;
import nitrox.dlc.mirror.api.Domain;

/**
 * {@see BeanSerializerModifier}
 *
 * @author Mario Herb
 */
public class DlcSerializerModifier extends BeanSerializerModifier {

    private final DlcJacksonModule.CustomizerContainer customizersContainer;

    public DlcSerializerModifier(DlcJacksonModule.CustomizerContainer customizersContainer) {
        this.customizersContainer = customizersContainer;
    }

    /**
     * Plug in {@link Domain} based serializer modifications
     */
    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (Entity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new EntitySerializer((JacksonMappingCustomizer<Entity>) customizersContainer.findCustomizer(beanDesc.getBeanClass()));

        }
        if (ValueObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new ValueObjectSerializer((JacksonMappingCustomizer<ValueObject>) customizersContainer.findCustomizer(beanDesc.getBeanClass()));
        }
        if (Identity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new IdentitySerializer();
        }
        return super.modifySerializer(config, beanDesc, serializer);
    }


}

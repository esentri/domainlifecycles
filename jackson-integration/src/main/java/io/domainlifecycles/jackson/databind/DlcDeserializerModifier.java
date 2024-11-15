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

package io.domainlifecycles.jackson.databind;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;

/**
 * {@see BeanDeserializerModifier}
 *
 * @author Mario Herb
 */
public class DlcDeserializerModifier extends BeanDeserializerModifier {

    private final DlcJacksonModule.CustomizerContainer customizersContainer;
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private final EntityIdentityProvider entityIdentityProvider;

    /**
     * Constructor for DlcDeserializerModifier
     *
     * @param customizersContainer The container for customizers.
     * @param domainObjectBuilderProvider The provider for DomainObjectBuilders.
     * @param entityIdentityProvider The provider for entity identities.
     */
    public DlcDeserializerModifier(DlcJacksonModule.CustomizerContainer customizersContainer,
                                   DomainObjectBuilderProvider domainObjectBuilderProvider,
                                   EntityIdentityProvider entityIdentityProvider) {
        this.customizersContainer = customizersContainer;
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
        this.entityIdentityProvider = entityIdentityProvider;

    }

    /**
     * Plug in {@link Domain} based deserializer modifications
     */
    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                  JsonDeserializer<?> deserializer) {
        if (Entity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new EntityDeserializer(beanDesc.getType(),
                customizersContainer,
                domainObjectBuilderProvider,
                entityIdentityProvider
            );
        }
        if (Identity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new IdentityDeserializer(
                beanDesc.getType()
            );
        }
        if (ValueObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new ValueObjectDeserializer(
                beanDesc.getType(),
                customizersContainer.findCustomizer(beanDesc.getBeanClass()),
                domainObjectBuilderProvider
            );
        }
        return super.modifyDeserializer(config, beanDesc, deserializer);
    }


}

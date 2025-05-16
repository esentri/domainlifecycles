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
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;

/**
 * {@see BeanDeserializerModifier}
 *
 * @author Mario Herb
 */
public class DlcDeserializerModifier extends BeanDeserializerModifier {

    /**
     * A container for managing Jackson mapping customizers associated with specific domain object types.
     * This field is used to locate and apply domain-specific customizations during the deserialization process.
     * It holds mappings between domain object classes and their respective {@link JacksonMappingCustomizer} instances.
     */
    private final DlcJacksonModule.CustomizerContainer customizersContainer;
    /**
     * A provider for creating instances of {@link DomainObjectBuilder} for domain object deserialization.
     * This field holds a reference to a {@link DomainObjectBuilderProvider} implementation, which is used
     * to supply builders for specific domain object types based on their type information.
     *
     * It is utilized during the deserialization process to customize and construct domain objects
     * as per the provided configurations and conventions.
     */
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;
    /**
     * Responsible for providing identity information for entities. This field is
     * used within the context of the deserialization process to retrieve and handle
     * identity-related data for {@link Entity} instances.
     *
     * The field is initialized via the {@link DlcDeserializerModifier} constructor
     * and remains immutable throughout the object's lifecycle. It facilitates integration
     * with the {@link EntityIdentityProvider}, enabling dynamic deserialization
     * adjustments based on the identity of domain-specific entities.
     *
     * This ensures that deserialization of entities can be customized depending
     * on their identity, which is particularly useful for maintaining consistency
     * across the domain model and preserving referential integrity during
     * the JSON-to-object transformation process.
     */
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

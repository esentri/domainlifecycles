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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Autoconfiguration for Jackson JSON processing integration with the DLC framework.
 * This configuration ensures that DLC domain objects can be seamlessly serialized to
 * and deserialized from JSON, maintaining domain integrity and supporting complex
 * domain object hierarchies.
 * <p>
 * The configuration provides specialized Jackson modules that understand DLC domain types:
 * <ul>
 *   <li>ValueObjects with their encapsulated values</li>
 *   <li>Domain Identities with proper type safety</li>
 *   <li>Entities and AggregateRoots with identity management</li>
 *   <li>Custom domain object builders for proper instantiation</li>
 * </ul>
 * </p>
 * <p>
 * Two variants are provided: one with EntityIdentityProvider support for handling
 * new entity creation during deserialization, and one without for simpler use cases.
 * </p>
 *
 * @author Mario Herb
 * @author Leon Völlinger
 *
 * @see DlcJacksonModule
 * @see EntityIdentityProvider
 * @see DomainObjectBuilderProvider
 */
@AutoConfiguration
@ConditionalOnBean(DomainObjectBuilderProvider.class)
@AutoConfigureAfter({DlcJooqPersistenceAutoConfiguration.class, DlcBuilderAutoConfiguration.class, DlcDomainAutoConfiguration.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DlcJacksonAutoConfiguration {

    /**
     * Creates a DLC Jackson module with EntityIdentityProvider support.
     * This module provides enhanced serialization/deserialization capabilities
     * for domain objects that require entity identity management.
     *
     * @param customizers list of custom mapping configurations to apply to the module
     * @param domainObjectBuilderProvider the builder provider for constructing domain objects during deserialization
     * @param entityIdentityProvider the provider for handling entity identity resolution
     * @return a configured DlcJacksonModule with entity identity support
     */
    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnBean(EntityIdentityProvider.class)
    public DlcJacksonModule dlcModuleConfigurationWithEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                      DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                      EntityIdentityProvider entityIdentityProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

    /**
     * Creates a basic DLC Jackson module without EntityIdentityProvider.
     * This is a fallback configuration for applications that don't require
     * entity identity management during JSON processing.
     *
     * @param customizers list of custom mapping configurations to apply to the module
     * @param domainObjectBuilderProvider the builder provider for constructing domain objects during deserialization
     * @return a configured DlcJacksonModule without entity identity support
     */

    @Bean
    @ConditionalOnMissingBean({EntityIdentityProvider.class, DlcJacksonModule.class})
    public DlcJacksonModule dlcModuleConfigurationWithoutEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                         DomainObjectBuilderProvider domainObjectBuilderProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }
}

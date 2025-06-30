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

@AutoConfiguration
@ConditionalOnBean(DomainObjectBuilderProvider.class)
@AutoConfigureAfter({DlcJooqPersistenceAutoConfiguration.class, DlcBuilderAutoConfiguration.class, DlcDomainAutoConfiguration.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DlcJacksonAutoConfiguration {

    /**
     * DLC Jackson integration
     */
    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnBean(EntityIdentityProvider.class)
    DlcJacksonModule dlcModuleConfigurationWithEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                      DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                      EntityIdentityProvider entityIdentityProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

    @ConditionalOnMissingBean
    @Bean
    DlcJacksonModule dlcModuleConfigurationWithoutEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                         DomainObjectBuilderProvider domainObjectBuilderProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }
}

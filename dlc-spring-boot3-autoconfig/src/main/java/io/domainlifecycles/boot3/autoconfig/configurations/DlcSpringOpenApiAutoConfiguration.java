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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.boot3.autoconfig.configurations;

import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for integrating SpringDoc API documentation features
 * into the DLC application framework. This class ensures that the necessary
 * SpringDoc configurations and customizations are properly registered and only
 * active when the relevant conditions are met.
 *
 * This configuration is only loaded if the SpringDoc library is present on the
 * classpath and the `springdoc.api-docs.enabled` property is set to `true` or is
 * absent (defaulting to `true`). It also ensures it is processed after the core
 * DLC domain configuration (`DlcDomainAutoConfiguration`).
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    after = DlcDomainAutoConfiguration.class,
    afterName = {
        "org.springdoc.core.configuration.SpringDocConfiguration"
    })
@ConditionalOnClass(name = "org.springdoc.core.properties.SpringDocConfigProperties")
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(prefix = "dlc.features.openapi", name = "enabled", havingValue = "true", matchIfMissing = true)
@Deprecated
public class DlcSpringOpenApiAutoConfiguration {

    /**
     * Configuration class for defining SpringDoc-specific beans for the DLC framework.
     *
     * This class is conditionally loaded when the `org.springdoc.core.properties.SpringDocConfigProperties`
     * class is present on the classpath, enabling customization of the SpringDoc OpenAPI integration.
     * It ensures that SpringDoc configuration is encapsulated within a nested static configuration class,
     * following modular design principles.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springdoc.core.properties.SpringDocConfigProperties")
    static class DlcSpringdocConfiguration {

        /**
         * Creates a new instance of the {@link DlcOpenApiCustomizer} bean if one is not already defined in the application context.
         * This method is responsible for customizing the SpringDoc OpenAPI configuration using the provided SpringDoc configuration properties.
         *
         * @param springDocConfigProperties the configuration properties for SpringDoc, used to customize OpenAPI settings
         * @return an instance of {@link DlcOpenApiCustomizer} initialized with the given SpringDoc configuration properties
         */
        @Bean
        @ConditionalOnMissingBean(DlcOpenApiCustomizer.class)
        public DlcOpenApiCustomizer openApiCustomizer(org.springdoc.core.properties.SpringDocConfigProperties springDocConfigProperties) {
            return new DlcOpenApiCustomizer(springDocConfigProperties);
        }
    }
}

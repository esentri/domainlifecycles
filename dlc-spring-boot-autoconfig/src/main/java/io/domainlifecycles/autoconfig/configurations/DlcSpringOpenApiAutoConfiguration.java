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

import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for OpenAPI/Swagger integration with Spring Boot applications using the DLC framework.
 *  This configuration provides automatic OpenAPI documentation generation for REST endpoints
 *  that use DLC domain objects, ensuring proper serialization and schema generation.
 *  <p>
 *  The configuration sets up customizers that understand DLC domain types,
 *  including ValueObjects, Identities, and Entities, to generate accurate API documentation
 *  with proper schema representations.
 *  </p>
 *
 * @author Leon Völlinger
 * @author Mario Herb
 */
@AutoConfiguration(after = DlcDomainAutoConfiguration.class)
@ConditionalOnClass(name="org.springdoc.core.properties.SpringDocConfigProperties")
public class DlcSpringOpenApiAutoConfiguration {

    /**
     * Creates a customizer for OpenAPI documentation that understands DLC domain objects.
     * This customizer ensures that DLC domain types are properly represented in the
     * generated OpenAPI schema, including correct handling of ValueObjects, Identities,
     * and other DLC-specific types.
     *
     * @param springDocConfigProperties properties for configuring SpringDoc OpenAPI generation
     * @return a {@link DlcOpenApiCustomizer} for enhancing API documentation with DLC support
     */
    @Bean
    @ConditionalOnMissingBean(DlcOpenApiCustomizer.class)
    public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
        return new DlcOpenApiCustomizer(springDocConfigProperties);
    }
}

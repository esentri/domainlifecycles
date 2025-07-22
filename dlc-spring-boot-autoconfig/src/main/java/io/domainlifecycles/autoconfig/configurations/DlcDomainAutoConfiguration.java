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

import io.domainlifecycles.autoconfig.configurations.properties.DlcDomainProperties;
import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Core domain configuration for enabling standard domain-related features in the DLC framework.
 * This serves as the foundation for all subsequent dependencies in DLC.
 *
 * @author leonvoellinger
 */
@AutoConfiguration
@EnableConfigurationProperties(DlcDomainProperties.class)
@AutoConfigureBefore({JooqAutoConfiguration.class})
public class DlcDomainAutoConfiguration {

    private @Value("${dlcDomainBasePackages}") String dlcDomainBasePackages;

    /**
     * Creates and initializes the domain mirror for the DLC framework.
     * The domain mirror is built by scanning the specified base packages for domain objects
     * and creating reflective metadata about the domain structure.
     * <p>
     * This method ensures that the domain is only initialized once and uses either
     * the configuration properties or the annotation value to determine which packages to scan.
     * </p>
     *
     * @param dlcDomainProperties the configuration properties containing domain settings
     * @return the initialized DomainMirror instance
     * @throws DLCAutoConfigException if the base packages property is missing or invalid
     */
    @Bean
    public DomainMirror initializedDomain(DlcDomainProperties dlcDomainProperties) {
        if (!Domain.isInitialized()) {
            String basePackages;
            if (dlcDomainProperties != null
                && dlcDomainProperties.getBasePackages() != null
                && !dlcDomainProperties.getBasePackages().isBlank()) {

                basePackages = dlcDomainProperties.getBasePackages();
            } else if (dlcDomainBasePackages != null && !dlcDomainBasePackages.isBlank()) {
                basePackages = dlcDomainBasePackages;
            } else {
                throw DLCAutoConfigException.fail(
                    "Property 'basePackages' is missing. Make sure you specified a property called " +
                        "'dlc.domain.basePackages' or add a 'dlcDomainBasePackages' value on the @EnableDLC annotation.");
            }
            String[] domainBasePackages = basePackages.split(",");
            Domain.initialize(new ReflectiveDomainMirrorFactory(domainBasePackages));
        }

        return Domain.getDomainMirror();
    }
}

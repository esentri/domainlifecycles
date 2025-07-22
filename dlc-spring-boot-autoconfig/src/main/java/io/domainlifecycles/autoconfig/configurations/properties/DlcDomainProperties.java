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

package io.domainlifecycles.autoconfig.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for DLC domain initialization.
 * This class holds the configuration values that control how the DLC framework
 * discovers and initializes domain objects within the application.
 * <p>
 * These properties can be configured via Spring Boot's configuration mechanism,
 * typically in application.properties or application.yml files using the
 * prefix "dlc.domain".
 * </p>
 *
 * @author leonvoellinger
**/
@ConfigurationProperties(prefix = "dlc.domain")
public class DlcDomainProperties {

    /**
     * Comma-separated list of base packages to scan for domain objects.
     * These packages will be recursively scanned to discover domain entities,
     * value objects, aggregates, and other DLC domain components.
     */
    private String basePackages;

    /**
     * Gets the base packages to scan for domain objects.
     *
     * @return comma-separated list of package names, or null if not configured
     */
    public String getBasePackages() {
        return basePackages;
    }

    /**
     * Sets the base packages to scan for domain objects.
     * Multiple packages should be separated by commas.
     *
     * @param basePackages comma-separated list of package names to scan
     */
    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }
}

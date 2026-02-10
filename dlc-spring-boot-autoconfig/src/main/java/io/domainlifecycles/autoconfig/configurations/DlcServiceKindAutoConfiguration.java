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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.configurations.registrar.ServiceKindBeanRegistrar;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration class for registering and initializing service kinds in the DLC framework.
 * This configuration ensures that service kind beans are discovered, registered, and integrated
 * after the core domain configuration is set up by {@code DlcDomainAutoConfiguration}.
 *
 * The class utilizes {@code ServiceKindBeanRegistrar} to scan domain-specific packages for
 * service kinds and automatically registers their implementations in the application context.
 * The scanning and registration logic relies on the base packages defined in the application's
 * environment properties under the key {@code dlc.domain.basePackages}.
 *
 * This configuration is essential for enabling service kinds to function as part of the extended
 * DLC application framework. It relies on the pre-initialized domain setup provided by
 * {@code DlcDomainAutoConfiguration}.
 *
 * Annotation configurations:
 * - {@code @AutoConfiguration(after = {DlcDomainAutoConfiguration.class})} ensures that this
 *   configuration is applied after the core domain configuration.
 * - {@code @Import(ServiceKindBeanRegistrar.class)} imports the bean registration logic necessary
 *   for detecting and registering service kind beans dynamically.
 *
 * @author Mario Herb
 */
@AutoConfiguration(after = {
    DlcDomainAutoConfiguration.class,
    DlcJooqPersistenceAutoConfiguration.class})
@Import(ServiceKindBeanRegistrar.class)
public class DlcServiceKindAutoConfiguration {
}

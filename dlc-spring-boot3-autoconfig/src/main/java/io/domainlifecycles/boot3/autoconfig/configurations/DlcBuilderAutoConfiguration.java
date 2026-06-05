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

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for domain object builder functionality in the DLC framework.
 * This configuration enables DLC compatibility with various builder patterns including
 * inner class builders (commonly used with Lombok's @Builder annotation) and other
 * builder implementations.
 * <p>
 * The configuration provides a default {@link DomainObjectBuilderProvider} that can handle
 * the creation of domain objects during deserialization and other framework operations
 * where object instantiation is required.
 * </p>
 * <p>
 * This autoconfiguration is a fundamental dependency for other DLC features like
 * Jackson integration and JOOQ persistence, as these components require the ability
 * to construct domain objects dynamically.
 * </p>
 *
 * @author Mario Herb
 * @author Leon Völlinger
 *
 * @see DomainObjectBuilderProvider
 * @see InnerClassDomainObjectBuilderProvider
 */
@AutoConfiguration
@AutoConfigureAfter(DlcDomainAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dlc.features.builder", name = "enabled", havingValue = "true", matchIfMissing = true)
@Deprecated
public class DlcBuilderAutoConfiguration {

    /**
     * Configures a domain object builder provider for inner class builders.
     * This provider is compatible with Lombok's @Builder annotation and other
     * inner class builder patterns commonly used in domain-driven design.
     * <p>
     * The provider enables the framework to instantiate domain objects during
     * operations like JSON deserialization, database record mapping, and other
     * scenarios where dynamic object creation is required.
     * </p>
     *
     * @return a {@link DomainObjectBuilderProvider} compatible with inner class builders
     */
    @Bean
    @ConditionalOnMissingBean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }
}

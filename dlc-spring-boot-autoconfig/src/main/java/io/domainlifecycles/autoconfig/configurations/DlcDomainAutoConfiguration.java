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
import io.domainlifecycles.autoconfig.configurations.properties.DlcJooqPersistenceProperties;
import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(DlcDomainProperties.class)
@AutoConfigureBefore({JooqAutoConfiguration.class})
public class DlcDomainAutoConfiguration {

    private @Value("${dlcDomainBasePackages}") String dlcDomainBasePackages;

    @Bean
    @ConditionalOnMissingBean(Domain.class)
    public DomainMirror initializedDomain(DlcDomainProperties dlcDomainProperties) {
        if (!Domain.isInitialized()) {
            String basePackages;
            if (dlcDomainProperties != null) {
                basePackages = dlcDomainProperties.getBasePackages();
            } else if (dlcDomainBasePackages != null) {
                basePackages = dlcDomainBasePackages;
            } else {
                throw DLCAutoConfigException.fail(
                    "Property 'basePackages' is missing. Make sure you specified a property called 'dlc.domain.basePackages'.");
            }
            String[] domainBasePackages = basePackages.split(",");
            Domain.initialize(new ReflectiveDomainMirrorFactory(domainBasePackages));
        }

        return Domain.getDomainMirror();
    }
}

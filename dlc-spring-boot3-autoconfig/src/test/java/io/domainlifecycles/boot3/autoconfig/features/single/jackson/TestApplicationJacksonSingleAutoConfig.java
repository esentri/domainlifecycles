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

package io.domainlifecycles.boot3.autoconfig.features.single.jackson;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(
    exclude = {
        DlcBuilderAutoConfiguration.class,
        DlcSpringWebAutoConfiguration.class,
        DlcSpringOpenApiAutoConfiguration.class,
        DlcJooqPersistenceAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    },
    dlcDomainBasePackages = "io.domainlifecycles.autoconfig"
)
public class TestApplicationJacksonSingleAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationJacksonSingleAutoConfig.class).run(args);
    }
}

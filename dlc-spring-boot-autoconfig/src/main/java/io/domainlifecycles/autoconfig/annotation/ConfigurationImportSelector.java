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

package io.domainlifecycles.autoconfig.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
            .getAnnotationAttributes(EnableDlc.class.getName());

        boolean enableBuilderAutoConfig = (boolean) attributes.get("enableBuilderAutoConfig");
        boolean enableJooqPersistenceAutoConfig = (boolean) attributes.get("enableJooqPersistenceAutoConfig");
        boolean enableDomainEventsAutoConfig = (boolean) attributes.get("enableDomainEventsAutoConfig");
        boolean enableSpringWebAutoConfig = (boolean) attributes.get("enableSpringWebAutoConfig");
        boolean enableJacksonAutoConfig = (boolean) attributes.get("enableJacksonAutoConfig");
        boolean enableSpringOpenApiAutoConfig = (boolean) attributes.get("enableSpringOpenApiAutoConfig");

        List<String> configs = new ArrayList<>();

        if (enableBuilderAutoConfig) {
            configs.add(DlcBuilderAutoConfiguration.class.getName());
        }

        if (enableJooqPersistenceAutoConfig) {
            configs.add(DlcJooqPersistenceAutoConfiguration.class.getName());
        }

        if (enableDomainEventsAutoConfig) {
            configs.add(DlcDomainEventsAutoConfiguration.class.getName());
        }

        if (enableSpringWebAutoConfig) {
            configs.add(DlcSpringWebAutoConfiguration.class.getName());
        }

        if (enableJacksonAutoConfig) {
            configs.add(DlcJacksonAutoConfiguration.class.getName());
        }

        if (enableSpringOpenApiAutoConfig) {
            configs.add(DlcSpringOpenApiAutoConfiguration.class.getName());
        }

        return configs.toArray(new String[0]);
    }
}

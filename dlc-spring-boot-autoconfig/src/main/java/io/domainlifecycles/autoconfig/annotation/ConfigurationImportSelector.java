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

import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcDomainAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Import selector for DLC autoconfiguration classes.
 * This class dynamically selects which DLC autoconfiguration classes should be imported
 * based on the attributes of the {@link EnableDlc} annotation.
 * <p>
 * The selector analyzes the annotation attributes and conditionally imports configuration classes
 * for various DLC components such as:
 * <ul>
 *   <li>Domain initialization (always imported)</li>
 *   <li>Builder support (conditional)</li>
 *   <li>JOOQ persistence (conditional)</li>
 *   <li>Domain events (conditional)</li>
 *   <li>Spring Web integration (conditional)</li>
 *   <li>Jackson JSON processing (conditional)</li>
 *   <li>OpenAPI documentation (conditional)</li>
 * </ul>
 * </p>
 * <p>
 * The selector also provides warnings when certain combinations of features are enabled
 * without their required dependencies.
 * </p>
 *
 * @author Mario Herb
 * @author Leon Völlinger
 *
 * @see EnableDlc
 * @see ImportSelector
 */
public class ConfigurationImportSelector implements ImportSelector {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationImportSelector.class);

    /**
     * Selects the DLC autoconfiguration classes to import.
     * This method defines which components of the DLC framework will be available
     * in the Spring application context when the @EnableDlc annotation is used.
     *
     * @param importingClassMetadata Metadata about the class that is importing this selector
     * @return Array of fully qualified class names to import
     */
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
        configs.add(DlcDomainAutoConfiguration.class.getName());

        if (enableBuilderAutoConfig) {
            configs.add(DlcBuilderAutoConfiguration.class.getName());
        }

        if (enableJooqPersistenceAutoConfig) {
            configs.add(DlcJooqPersistenceAutoConfiguration.class.getName());

            if(!enableBuilderAutoConfig) {
                LOGGER.warn("DLC persistence auto-configuration is enabled without using the builder auto-configuration. " +
                    "Make sure you provide all necessary beans yourself, or enable the builder auto configuration.");
            }
        }

        if (enableDomainEventsAutoConfig) {
            configs.add(DlcDomainEventsAutoConfiguration.class.getName());
        }

        if (enableSpringWebAutoConfig) {
            configs.add(DlcSpringWebAutoConfiguration.class.getName());
        }

        if (enableJacksonAutoConfig) {
            configs.add(DlcJacksonAutoConfiguration.class.getName());

            if(!enableBuilderAutoConfig) {
                LOGGER.warn("DLC Jackson auto-configuration is enabled without using the builder auto-configuration. " +
                    "Make sure you provide all necessary beans yourself, or enable the builder auto configuration.");
            }
        }

        if (enableSpringOpenApiAutoConfig) {
            configs.add(DlcSpringOpenApiAutoConfiguration.class.getName());
        }

        return configs.toArray(new String[0]);
    }
}

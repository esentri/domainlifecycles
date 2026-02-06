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

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Import selector for domain-related configuration properties.
 * This selector dynamically imports configuration classes based on the domain
 * configuration specified in the {@link EnableDlc} annotation.
 * <p>
 * The selector analyzes the {@code dlcDomainBasePackages} attribute from the
 * {@link EnableDlc} annotation and creates the necessary configuration properties
 * to make them available to the Spring application context.
 * </p>
 * <p>
 * This class works in conjunction with {@link DlcDomainProperties} to provide
 * a flexible configuration mechanism that supports both annotation-based
 * and properties-file-based configuration.
 * </p>
 *
 * @author leonvoellinger
 *
 * @see EnableDlc
 * @see DlcDomainProperties
 * @see ImportSelector
 */
public class DomainConfigImportSelector implements ImportSelector, EnvironmentAware {

    private Environment environment;

    /**
     * Selects configuration classes to import based on domain configuration.
     * This method examines the {@link EnableDlc} annotation attributes and
     * determines which domain-related configuration classes should be imported
     * into the Spring application context.
     * <p>
     * The method specifically looks for the {@code dlcDomainBasePackages} attribute
     * and ensures that the appropriate configuration is available for domain
     * initialization.
     * </p>
     *
     * @param importingClassMetadata metadata about the class that is importing this selector
     * @return array of fully qualified class names to import for domain configuration
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attrs = importingClassMetadata
            .getAnnotationAttributes(EnableDlc.class.getName());
        if (attrs != null) {
            String dlcDomainBasePackages = (String) attrs.get("dlcDomainBasePackages");

            // Register it in the environment for later use (e.g. by auto-config)
            if (environment instanceof ConfigurableEnvironment) {
                MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
                Map<String, Object> map = new HashMap<>();
                map.put("dlcDomainBasePackages", dlcDomainBasePackages);
                propertySources.addFirst(new MapPropertySource("enableDlcAnnotationDomain", map));
            }
        }

        // Optionally, import additional configuration manually
        return new String[0];
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

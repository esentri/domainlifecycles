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

package io.domainlifecycles.boot3.autoconfig.configurations.properties;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;

/**
 * A custom implementation of the {@link EnvironmentPostProcessor} interface that processes
 * the Spring `ConfigurableEnvironment` to register properties based on the presence of the
 * {@link EnableDlc} annotation within the application classpath.
 *
 * This post-processor is used to automatically configure certain DLC-related application settings
 * by scanning the application's base package for classes annotated with {@code @EnableDlc}.
 * When such classes are found, it extracts configuration parameters related to DLC domain
 * packages, jOOQ record packages, and SQL dialects and adds them to the environment's property sources.
 *
 * The retrieved properties are added to a property source named `enableDlcAnnotation` in the environment.
 *
 * The following configuration can be extracted from the {@code @EnableDlc} annotation:
 * - `dlc.domain.basePackages` - An array of base package names required for DLC domain configuration.
 * - `dlc.persistence.jooqRecordPackage` - The package containing jOOQ-generated record classes, if applicable.
 * - `dlc.persistence.sqlDialect` - The SQL dialect to be used with jOOQ, if applicable.
 *
 * This processor uses {@link ClassPathScanningCandidateComponentProvider} to identify eligible
 * classes in the application's base package and process their annotations.
 *
 * The order of this processor is set to {@link Ordered#HIGHEST_PRECEDENCE} to ensure it runs early
 * during application environment initialization.
 *
 * Implementation Notes:
 * - If the application's main class cannot be determined, the processor will terminate early without
 *   performing any operations.
 * - Classes annotated with {@code @EnableDlc} must be located within the main application class's base package.
 * - If the {@code @EnableDlc} annotation does not specify any properties, no modifications are made to the environment.
 * - Exceptions related to class loading during annotation processing are caught and ignored to prevent disruption
 *   during environment initialization.
 *
 * @author Mario Herb
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Deprecated
public class DlcEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * Processes the Spring application environment to extract and register properties
     * related to the {@link EnableDlc} annotation. This method scans for classes
     * annotated with {@link EnableDlc} within the application's base package,
     * retrieves their configuration attributes, and dynamically adds them
     * to the application's property sources.
     *
     * @param env the configurable environment of the Spring application. This is used to add
     *            property sources based on the attributes of the {@link EnableDlc} annotation.
     * @param app the SpringApplication instance being processed. This is used to obtain the
     *            main application class and derive the base package for classpath scanning.
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {

        Class<?> annotated = findDlcAnnotationClass(app);
        if (annotated == null) return;

        var props = new LinkedHashMap<String, Object>();

        EnableDlc ann = annotated.getAnnotation(EnableDlc.class);
        if (ann != null) {
            if(ann.dlcMirrorBasePackages() != null && ann.dlcMirrorBasePackages().length>0){
                props.put("dlc.features.mirror.base-packages", ann.dlcMirrorBasePackages());
            }
            if(!ann.jooqRecordPackage().isBlank()){
                props.put("dlc.features.persistence.jooq-record-package", ann.jooqRecordPackage());
            }
            if(!ann.jooqSqlDialect().isBlank()) {
                props.put("dlc.features.persistence.sql-dialect", ann.jooqSqlDialect());
            }
        }

        if (props.isEmpty()) return;

        env.getPropertySources().addLast(new MapPropertySource("enableDlcAnnotation", props));
    }

    private static Class<?> findDlcAnnotationClass(SpringApplication app) {
        for (Object source : app.getAllSources()) {
            if (source instanceof Class<?> cls) {
                if (cls.isAnnotationPresent(EnableDlc.class)) {
                    return cls;
                }
            }
        }
        return null;
    }
}

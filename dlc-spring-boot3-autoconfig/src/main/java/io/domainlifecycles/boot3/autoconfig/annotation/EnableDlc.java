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

package io.domainlifecycles.boot3.autoconfig.annotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable DLC (Domain Lifecycles) framework autoconfiguration.
 * <p>
 * This annotation activates the automatic configuration of various DLC components
 * including domain mirroring, persistence, event handling, JSON processing, and web integration.
 * The annotation allows fine-grained control over which components should be
 * automatically configured through its boolean attributes.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * {@code
 * @EnableDlc(
 *     dlcDomainBasePackages = "com.example.domain",
 *     jooqRecordPackage = "com.example.jooq.tables.records",
 *     jooqSqlDialect = "POSTGRES"
 * )
 * @SpringBootApplication
 * public class Application {
 *     public static void main(String[] args) {
 *         SpringApplication.run(Application.class, args);
 *     }
 * }
 * }
 * </pre>
 *
 * @author Mario Herb
 * @author Leon Völlinger
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration
@Deprecated
public @interface EnableDlc {

    /** if jOOQ is used, a package containing all generated jooq record classes must be provided
     * @return jooq record package
     * **/
    String jooqRecordPackage() default "";

    /** if jOOQ is used, specifying a possible SQL dialect is mandatory (@see org.jooq.SQLDialect)
     * @return jooq SQL dialect
     * **/
    String jooqSqlDialect() default "";

    /** DLC always requires all packages, which contain all Domain classes used within the application
     * @return domain base packages
     * **/
    String[] dlcDomainBasePackages() default {};

    /**
     * Optionally specify AutoConfigurations to be excluded
     * @return excluded auto-configurations
     */
    @AliasFor(
        annotation = ImportAutoConfiguration.class, attribute = "exclude")
    Class<?>[] exclude() default {};

}

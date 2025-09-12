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

import io.domainlifecycles.autoconfig.configurations.properties.DomainConfigImportSelector;
import io.domainlifecycles.autoconfig.configurations.properties.JooqPersistenceConfigImportSelector;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jooq.SQLDialect;
import org.springframework.context.annotation.Import;

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
 *     enableJooqPersistenceAutoConfig = true,
 *     enableDomainEventsAutoConfig = true,
 *     jooqRecordPackage = "com.example.jooq.tables.records",
 *     jooqSqlDialect = SQLDialect.POSTGRES
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
 * @see ConfigurationImportSelector
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JooqPersistenceConfigImportSelector.class, DomainConfigImportSelector.class, ConfigurationImportSelector.class})
public @interface EnableDlc {

    boolean enableSpringWebAutoConfig() default true;
    boolean enableBuilderAutoConfig() default true;
    boolean enableJooqPersistenceAutoConfig() default true;
    boolean enableDomainEventsAutoConfig() default false;
    boolean enableJacksonAutoConfig() default true;
    boolean enableSpringOpenApiAutoConfig() default true;
    String jooqRecordPackage() default "";
    SQLDialect jooqSqlDialect() default SQLDialect.DEFAULT;
    String dlcDomainBasePackages() default "";
}

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.domainlifecycles.autoconfig.configurations.properties.JooqPersistenceConfigImportSelector;
import org.jooq.SQLDialect;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JooqPersistenceConfigImportSelector.class, ConfigurationImportSelector.class})
public @interface EnableDlc {

    boolean enableSpringWebAutoConfig() default true;
    boolean enableBuilderAutoConfig() default true;
    boolean enableJooqPersistenceAutoConfig() default true;
    String  jooqRecordPackage() default "";
    SQLDialect jooqSqlDialect() default SQLDialect.DEFAULT;
    boolean enableDomainEventsAutoConfig() default true;
    boolean enableJacksonAutoConfig() default true;
    boolean enableSpringOpenApiAutoConfig() default true;
    boolean enableJmsEvents() default false;
    boolean enableGruelboxEvents() default false;
    boolean enableJakartaJmsGruelboxEvents() default false;
    boolean enableJakartaJmsGruelboxIdempotentEvents() default false;
}

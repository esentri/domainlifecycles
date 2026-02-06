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

package io.domainlifecycles.boot3.autoconfig.configurations.properties;

import org.jooq.SQLDialect;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for DLC JOOQ persistence functionality.
 * This class holds the configuration values that control how the DLC framework
 * integrates with JOOQ for database persistence operations.
 * <p>
 * These properties can be configured via Spring Boot's configuration mechanism,
 * typically in application.properties or application.yml files using the
 * prefix "dlc.persistence".
 * </p>
 *
 * @author leonvoellinger
 */
@ConfigurationProperties(prefix = "dlc.persistence")
@Deprecated
public class DlcJooqPersistenceProperties {

    /**
     * Package containing the generated JOOQ record classes.
     * This package is scanned to find the JOOQ record classes that correspond
     * to database tables and are used for persistence operations.
     */
    private String jooqRecordPackage;

    /**
     * SQL dialect to use for JOOQ operations.
     * This should match the database system being used (e.g., POSTGRES, MYSQL, H2).
     */
    private SQLDialect sqlDialect;

    /**
     * Gets the package containing JOOQ record classes.
     *
     * @return the package name where JOOQ records are located, or null if not configured
     */
    public String getJooqRecordPackage() {
        return jooqRecordPackage;
    }

    /**
     * Sets the package containing JOOQ record classes.
     * This package will be scanned to find record classes for persistence mapping.
     *
     * @param jooqRecordPackage the package name where JOOQ records are located
     */
    public void setJooqRecordPackage(String jooqRecordPackage) {
        this.jooqRecordPackage = jooqRecordPackage;
    }

    /**
     * Gets the SQL dialect for JOOQ operations.
     *
     * @return the configured SQL dialect, or null if not set
     */
    public SQLDialect getSqlDialect() {
        return sqlDialect;
    }

    /**
     * Sets the SQL dialect for JOOQ operations.
     * This should match the database system being used for optimal SQL generation.
     *
     * @param sqlDialect the SQL dialect to use (e.g., POSTGRES, MYSQL, H2)
     */
    public void setSqlDialect(SQLDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }
}

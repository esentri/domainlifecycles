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

import io.domainlifecycles.autoconfig.configurations.properties.DlcJooqPersistenceProperties;
import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.configuration.def.JooqRecordClassProvider;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.Set;

/**
 * Auto-configuration class for integrating JOOQ persistence with the DLC framework.
 * This class sets up beans and configuration necessary to enable JOOQ-based data access
 * and integration with domain-driven designs supported by DLC.
 *
 * Auto-configuration for this class occurs after several critical configurations,
 * such as the DataSource and other DLC-specific configurations, but before the
 * default Spring Boot JOOQ auto-configuration.
 *
 * Features of this auto-configuration include:
 * - Setting up a JOOQ {@link Configuration} object with custom SQL dialect and locking behavior.
 * - Creating a {@link DSLContext} bean for streamlined access to JOOQ DSL APIs.
 * - Providing a {@link JooqDomainPersistenceProvider} for domain persistence if a {@link DomainMirror} is available.
 * - Setting up an {@link EntityIdentityProvider} for handling entity identities in JOOQ operations.
 *
 * This class is conditionally activated when the JOOQ library is present on the classpath,
 * and certain dependent beans, like {@link DataSource}, are configured.
 *
 * Additional customization can be achieved through the {@link DlcJooqPersistenceProperties} configuration
 * class, which allows specifying properties like the JOOQ record package and SQL dialect.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    after = {
        DlcBuilderAutoConfiguration.class,
        DlcDomainAutoConfiguration.class
    },
    afterName = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
    beforeName = "org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration"
)
@EnableConfigurationProperties(DlcJooqPersistenceProperties.class)
@ConditionalOnClass(name = "org.jooq.DSLContext")
public class DlcJooqPersistenceAutoConfiguration {

    /**
     * Configuration class for setting up JOOQ persistence in a Spring Boot application.
     * This class provides the necessary beans and configurations to integrate JOOQ with
     * the application’s data source and domain persistence layer.
     *
     * The configuration is conditional on the presence of the JOOQ library in the classpath
     * and sets up beans only if required dependencies are available.
     *
     */
    @org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.jooq.DSLContext")
    static class JooqPersistenceConfiguration {

        @Value("${jooqRecordPackage:#{null}}")
        private String jooqRecordPackage;

        @Value("${jooqSqlDialect:#{null}}")
        private SQLDialect jooqSqlDialect;

        /**
         * Creates a {@link DataSourceConnectionProvider} bean for providing database connections.
         * This method wraps the given {@link DataSource} with a {@link TransactionAwareDataSourceProxy}
         * to ensure transaction-aware behavior.
         *
         * @param dataSource the data source to be wrapped by the connection provider
         * @return a {@link DataSourceConnectionProvider} instance configured with the given data source
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(name = "org.jooq.impl.DataSourceConnectionProvider")
        public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
        }

        /**
         * Creates a {@link DefaultConfiguration} bean for JOOQ configuration.
         * Configures the JOOQ settings, connection provider, and SQL dialect for database operations.
         * The SQL dialect is determined based on the provided persistence properties or a global dialect field.
         *
         * @param dataSource the {@link DataSource} used for database connections
         * @param persistenceProperties the custom persistence configuration properties for JOOQ,
         *                              providing details such as the SQL dialect to use
         * @return a configured {@link DefaultConfiguration} instance for JOOQ operations
         * @throws DLCAutoConfigException if the SQL dialect is not specified or cannot be determined
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(name = "org.jooq.Configuration")
        public DefaultConfiguration configuration(DataSource dataSource, DlcJooqPersistenceProperties persistenceProperties) {
            final var jooqConfig = new DefaultConfiguration();
            jooqConfig.settings().setExecuteWithOptimisticLocking(true);
            jooqConfig.setConnectionProvider(connectionProvider(dataSource));

            SQLDialect sqlDialect;
            if (persistenceProperties != null && persistenceProperties.getSqlDialect() != null) {
                sqlDialect = persistenceProperties.getSqlDialect();
            } else if (jooqSqlDialect != null) {
                sqlDialect = jooqSqlDialect;
            } else {
                throw DLCAutoConfigException.fail("Property 'sqlDialect' is missing. Specify 'dlc.persistence.sqlDialect' or 'jooqSqlDialect'.");
            }

            jooqConfig.set(sqlDialect);
            return jooqConfig;
        }

        /**
         * Creates a {@link DSLContext} bean for executing database queries using JOOQ.
         * This method uses the provided {@link Configuration} to initialize a default DSL context.
         *
         * @param configuration the JOOQ {@link Configuration} object containing settings, connection
         *                      provider, and SQL dialect for database operations
         * @return a {@link DSLContext} instance configured with the given {@link Configuration}
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(name = "org.jooq.DSLContext")
        public DSLContext dslContext(Configuration configuration) {
            return new DefaultDSLContext(configuration);
        }

        /**
         * Creates a {@link JooqDomainPersistenceProvider} bean for handling domain-specific persistence tasks.
         * The method configures the provider with necessary dependencies such as domain object builders,
         * custom record mappers, persistence properties, and a domain mirror.
         * It also determines the appropriate JOOQ record package to use during the setup.
         *
         * @param domainObjectBuilderProvider the provider for building domain objects from database records
         * @param customRecordMappers a set of custom mappers for converting database records to domain objects
         * @param persistenceProperties configuration properties for JOOQ persistence, including the record package
         * @param domainMirror the domain mirror for reflection and metadata about domain types
         * @return a configured {@link JooqDomainPersistenceProvider} instance
         * @throws DLCAutoConfigException if the required JOOQ record package property is missing or invalid
         */
        @Bean
        @ConditionalOnMissingBean(DomainPersistenceProvider.class)
        @ConditionalOnBean(DomainMirror.class)
        public JooqDomainPersistenceProvider domainPersistenceProvider(
            DomainObjectBuilderProvider domainObjectBuilderProvider,
            Set<RecordMapper<?, ?, ?>> customRecordMappers,
            DlcJooqPersistenceProperties persistenceProperties,
            DomainMirror domainMirror
        ) {
            String recordPackage;
            if (persistenceProperties != null
                && persistenceProperties.getJooqRecordPackage() != null
                && !persistenceProperties.getJooqRecordPackage().isBlank()) {
                recordPackage = persistenceProperties.getJooqRecordPackage();
            } else if (jooqRecordPackage != null && !jooqRecordPackage.isBlank()) {
                recordPackage = jooqRecordPackage;
            } else {
                throw DLCAutoConfigException.fail("Property 'jooqRecordPackage' is missing.");
            }

            return new JooqDomainPersistenceProvider(
                JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder
                    .newConfig()
                    .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
                    .withCustomRecordMappers(customRecordMappers)
                    .withRecordClassProvider(new JooqRecordClassProvider(recordPackage))
                    .make());
        }

        /**
         * Creates an {@link EntityIdentityProvider} bean for providing identity information for entities.
         * This implementation uses jOOQ's DSLContext to manage database sequences for generating identities.
         *
         * @param dslContext the {@link DSLContext} used for interacting with the database
         *                   and retrieving sequence values for identity generation
         * @return a {@link JooqEntityIdentityProvider} instance configured with the given DSLContext
         */
        @Bean
        @ConditionalOnMissingBean(EntityIdentityProvider.class)
        public EntityIdentityProvider identityProvider(DSLContext dslContext) {
            return new JooqEntityIdentityProvider(dslContext);
        }
    }
}

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

package io.domainlifecycles.boot3.autoconfig.configurations;

import io.domainlifecycles.boot3.autoconfig.configurations.persistence.SpringPersistenceEventPublisher;
import io.domainlifecycles.boot3.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.configuration.def.JooqRecordClassProvider;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
@ConditionalOnClass(name = "org.jooq.DSLContext")
@ConditionalOnProperty(prefix = "dlc.features.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Deprecated
public class DlcJooqPersistenceAutoConfiguration {

    /**
     * Configuration class for setting up JOOQ persistence in a Spring Boot application.
     * This class provides the necessary beans and configurations to integrate JOOQ with
     * the application’s data source and domain persistence layer.
     *
     * The configuration is conditional on the presence of the JOOQ library in the classpath
     * and sets up beans only if required dependencies are available. Additionally gated by
     * {@code dlc.features.persistence.jooq.enabled} (default {@code true}), independent of
     * {@code dlc.features.persistence.jdbc.enabled} - see {@link DlcJdbcPersistenceAutoConfiguration}'s
     * class javadoc for when to use one over the other, e.g. to force jOOQ off while both integrations
     * are on the classpath, without excluding this whole autoconfiguration class.
     *
     */
    @org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.jooq.DSLContext")
    @ConditionalOnProperty(prefix = "dlc.features.persistence.jooq", name = "enabled", havingValue = "true", matchIfMissing = true)
    static class JooqPersistenceConfiguration implements EnvironmentAware{

        private Environment environment;

        /**
         * Creates a {@link PersistenceEventPublisher} bean for publishing {@link PersistenceAction} events.
         * This method provides a {@link SpringPersistenceEventPublisher} instance, which bridges domain-specific
         * persistence events and Spring's event-publishing infrastructure.
         *
         * @param applicationEventPublisher the {@link ApplicationEventPublisher} used to publish events
         *                                  within the Spring application context
         * @return a {@link PersistenceEventPublisher} instance configured with the given {@link ApplicationEventPublisher}
         */
        @Bean
        @ConditionalOnMissingBean(PersistenceEventPublisher.class)
        public PersistenceEventPublisher persistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            return new SpringPersistenceEventPublisher(applicationEventPublisher);
        }

        /**
         * Creates a {@link SpringTransactionCacheProvider} bean backing the transaction cache feature: one cache
         * per Spring-managed transaction, kept as a resource of that transaction, read and written by
         * {@link #domainPersistenceProvider}. Holds at most {@code dlc.features.persistence.transaction-cache.max-size}
         * aggregates per transaction (256 by default), and is only created if
         * {@code dlc.features.persistence.transaction-cache.enabled} is {@code true} ({@code false} by default). Any bean of type
         * {@link TransactionCacheProvider} replaces it, whatever its name.
         *
         * @return a {@link SpringTransactionCacheProvider} instance
         * @throws DLCAutoConfigException if the maximum number of aggregates is not greater than 0
         */
        @Bean
        @ConditionalOnMissingBean(TransactionCacheProvider.class)
        @ConditionalOnProperty(prefix = TransactionCacheProperties.PREFIX, name = "enabled", havingValue = "true")
        public SpringTransactionCacheProvider<UpdatableRecord<?>> dlcTransactionCacheProvider() {
            return new SpringTransactionCacheProvider<>(TransactionCacheProperties.maxSize(environment));
        }

        /**
         * Creates a {@link DataSourceConnectionProvider} bean for providing database connections.
         * <p>
         * This method wraps the given {@link DataSource} with a {@link TransactionAwareDataSourceProxy}
         * to ensure transaction-aware behavior.
         *
         * @param dataSource the data source to be wrapped by the connection provider
         * @return a {@link ConnectionProvider} instance configured with the given data source
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(name = "org.jooq.impl.DataSourceConnectionProvider")
        public ConnectionProvider connectionProvider(DataSource dataSource) {
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
        }

        /**
         * Creates a {@link DefaultConfiguration} bean for JOOQ configuration.
         * Configures the JOOQ settings, connection provider, and SQL dialect for database operations.
         * The SQL dialect is determined based on the provided persistence properties or a global dialect field.
         *
         * @param connectionProvider the {@link ConnectionProvider} for JOOQ database operations
         * @return a configured {@link DefaultConfiguration} instance for JOOQ operations
         * @throws DLCAutoConfigException if the SQL dialect is not specified or cannot be determined
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(name = "org.jooq.Configuration")
        public DefaultConfiguration configuration(ConnectionProvider connectionProvider) {
            final var jooqConfig = new DefaultConfiguration();
            jooqConfig.settings().setExecuteWithOptimisticLocking(true);
            jooqConfig.setConnectionProvider(connectionProvider);
            SQLDialect sqlDialect;
            try {
                var property = environment.getProperty("dlc.features.persistence.sql-dialect");
                if(property == null) {
                    throw DLCAutoConfigException.fail("Property 'sqlDialect' is missing. Specify 'dlc.features.persistence.sql-dialect' or 'jooqSqlDialect' on '@EnableDlc'.");
                }
                sqlDialect = SQLDialect.valueOf(property);
            } catch (IllegalArgumentException e) {
                throw DLCAutoConfigException.fail("Property 'sqlDialect' is missing. Specify 'dlc.features.persistence.sql-dialect' or 'jooqSqlDialect' on '@EnableDlc'.");
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
         * @param domainMirror the domain mirror for reflection and metadata about domain types,
         *                     needed for correct order of bean instantiation
         * @param transactionCacheProvider the provider of the transaction cache - {@link #dlcTransactionCacheProvider}
         *                                 or a bean replacing it; none if the cache is disabled
         * @return a configured {@link JooqDomainPersistenceProvider} instance
         * @throws DLCAutoConfigException if the required JOOQ record package property is missing or invalid
         */
        @Bean
        @ConditionalOnMissingBean(DomainPersistenceProvider.class)
        @ConditionalOnBean(DomainMirror.class)
        public JooqDomainPersistenceProvider domainPersistenceProvider(
            DomainObjectBuilderProvider domainObjectBuilderProvider,
            Set<RecordMapper<?, ?, ?>> customRecordMappers,
            DomainMirror domainMirror,
            ObjectProvider<TransactionCacheProvider<UpdatableRecord<?>>> transactionCacheProvider
        ) {
            String recordPackage = environment.getProperty("dlc.features.persistence.jooq-record-package");
            if(recordPackage == null) {
                throw DLCAutoConfigException.fail("Property 'jooqRecordPackage' is missing. Specify 'dlc.features.persistence.jooq-record-package' or 'jooqRecordPackage' on '@EnableDlc'.");
            }

            // the single-argument constructor is used deliberately here, not the one that also registers
            // jOOQ's own TransactionCacheJooqBinder (a native org.jooq.TransactionListener) on the DSLContext:
            // this autoconfig only ever runs under Spring, whose transactions the SpringTransactionCacheProvider
            // follows - the jOOQ-native binder only works with a ThreadBoundTransactionCacheProvider anyway.
            var configuration = JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder
                .newConfig()
                .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
                .withCustomRecordMappers(customRecordMappers)
                .withRecordClassProvider(new JooqRecordClassProvider(recordPackage));
            if (TransactionCacheProperties.enabled(environment)) {
                var cacheProvider = transactionCacheProvider.getIfAvailable();
                if (cacheProvider == null) {
                    // the autoconfig's own provider is left out only for an existing one, which does not fit
                    throw DLCAutoConfigException.fail(
                        "A TransactionCacheProvider bean replaces the one of the jOOQ persistence autoconfig, but "
                            + "none is a TransactionCacheProvider<UpdatableRecord<?>>. Provide one of that type, or switch "
                            + "the cache off via '%s=false'.", TransactionCacheProperties.ENABLED);
                }
                configuration.withTransactionCacheEnabled(true).withTransactionCacheProvider(cacheProvider);
            } else {
                configuration.withTransactionCacheEnabled(false);
            }
            return new JooqDomainPersistenceProvider(configuration.make());
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

        /**
         * Sets the {@link Environment} for the configuration.
         * This method is automatically invoked by the Spring framework when the
         * class is registered as an {@link EnvironmentAware} component.
         *
         * @param environment the {@link Environment} object containing the current application environment properties
         */
        @Override
        public void setEnvironment(@NonNull Environment environment) {
            this.environment = environment;
        }
    }

}

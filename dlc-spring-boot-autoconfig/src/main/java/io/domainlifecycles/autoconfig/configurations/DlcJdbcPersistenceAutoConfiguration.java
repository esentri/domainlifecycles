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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.configurations.persistence.SpringPersistenceEventPublisher;
import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.dialect.MySqlJdbcDialect;
import io.domainlifecycles.jdbc.dialect.OracleJdbcDialect;
import io.domainlifecycles.jdbc.dialect.PostgresJdbcDialect;
import io.domainlifecycles.jdbc.dialect.SqlServerJdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcEntityIdentityProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
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
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

/**
 * Auto-configuration class for integrating plain JDBC based persistence ({@code jdbc-integration}) with the
 * DLC framework - the code-generation-free alternative to {@link DlcJooqPersistenceAutoConfiguration}, reading
 * the database schema once at startup via {@code java.sql.DatabaseMetaData} instead of relying on jOOQ's
 * generated record classes.
 * <p>
 * Auto-configuration for this class occurs after several critical configurations, such as the DataSource and
 * other DLC-specific configurations.
 * <p>
 * Features of this auto-configuration include:
 * - Resolving a {@link JdbcDialect} implementation from the same SQL dialect property/attribute
 *   {@link DlcJooqPersistenceAutoConfiguration} uses ({@code dlc.features.persistence.sql-dialect}/
 *   {@code jooqSqlDialect}) - a project only ever activates one of the two persistence backends, so the
 *   property name is shared rather than duplicated per backend.
 * - Reading the database schema once, via {@link JdbcSchemaMetadata}.
 * - Creating a {@link JdbcConnectionProvider} bean handing out the connection of the current Spring-managed
 *   transaction - see {@link JdbcPersistenceConfiguration#jdbcConnectionProvider}.
 * - Providing DLC's transaction cache for Spring-managed transactions, configurable via
 *   {@code dlc.features.persistence.transaction-cache.enabled}/{@code .max-size} - see
 *   {@link JdbcPersistenceConfiguration#dlcJdbcTransactionCacheProvider}.
 * - Providing a {@link JdbcDomainPersistenceProvider} for domain persistence if a {@link DomainMirror} is
 *   available.
 * - Setting up an {@link EntityIdentityProvider} for handling entity identities in JDBC operations.
 * <p>
 * This class is conditionally activated when {@code jdbc-integration} is present on the classpath, and
 * certain dependent beans, like {@link DataSource}, are configured. {@code afterName} references
 * {@code DataSourceAutoConfiguration} by its Spring Boot 4.x package - see
 * {@link DlcJooqPersistenceAutoConfiguration}'s class javadoc for why that matters and what a stale name
 * would silently do instead of failing loudly.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    after = {
        DlcBuilderAutoConfiguration.class,
        DlcDomainAutoConfiguration.class,
        DlcJooqPersistenceAutoConfiguration.class
    },
    afterName = "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration"
)
@ConditionalOnClass(name = "io.domainlifecycles.jdbc.connection.JdbcConnectionProvider")
@ConditionalOnProperty(prefix = "dlc.features.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DlcJdbcPersistenceAutoConfiguration {

    /**
     * Configuration class for setting up plain JDBC based persistence in a Spring Boot application. This
     * class provides the necessary beans and configurations to integrate {@code jdbc-integration} with the
     * application's data source and domain persistence layer.
     * <p>
     * The configuration is conditional on the presence of {@code jdbc-integration} on the classpath and sets
     * up beans only if required dependencies are available. Additionally gated by
     * {@code dlc.features.persistence.jdbc.enabled} (default {@code true}), independent of
     * {@code dlc.features.persistence.jooq.enabled} - e.g. to force JDBC off while both integrations are on
     * the classpath, without excluding this whole autoconfiguration class.
     */
    @org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "io.domainlifecycles.jdbc.connection.JdbcConnectionProvider")
    @ConditionalOnProperty(prefix = "dlc.features.persistence.jdbc", name = "enabled", havingValue = "true", matchIfMissing = true)
    static class JdbcPersistenceConfiguration implements EnvironmentAware {

        private Environment environment;

        /**
         * Creates a {@link PersistenceEventPublisher} bean for publishing {@link PersistenceAction} events.
         * Identical to, and shares its bean name and {@code @ConditionalOnMissingBean} guard with, the one
         * {@link DlcJooqPersistenceAutoConfiguration} provides - a project only ever activates one of the two
         * persistence backends, so at most one of these methods ever actually runs.
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
         * {@link #domainPersistenceProvider}. Like the other beans of this configuration, it is not created if a
         * {@link DomainPersistenceProvider} exists already - e.g. the one of
         * {@link DlcJooqPersistenceAutoConfiguration}, if both integrations are on the classpath: nothing would
         * use it, and a second cache provider bean would make injecting the one in use by type ambiguous. Holds at
         * most {@code dlc.features.persistence.transaction-cache.max-size} aggregates per transaction (256 by
         * default), and is not created if {@code dlc.features.persistence.transaction-cache.enabled} is
         * {@code false}. Any bean of type {@link TransactionCacheProvider} replaces it, whatever its name.
         *
         * @return a {@link SpringTransactionCacheProvider} instance
         * @throws DLCAutoConfigException if the maximum number of aggregates is not greater than 0
         */
        @Bean
        @ConditionalOnMissingBean({DomainPersistenceProvider.class, TransactionCacheProvider.class})
        @ConditionalOnProperty(prefix = TransactionCacheProperties.PREFIX, name = "enabled", havingValue = "true",
            matchIfMissing = true)
        public SpringTransactionCacheProvider<JdbcRecord> dlcJdbcTransactionCacheProvider() {
            return new SpringTransactionCacheProvider<>(TransactionCacheProperties.maxSize(environment));
        }

        /**
         * Creates a {@link JdbcConnectionProvider} bean handing out the connection bound to the currently
         * active Spring transaction (via {@code DataSourceUtils.getConnection(DataSource)}, the plain JDBC
         * equivalent of jOOQ's {@code TransactionAwareDataSourceProxy}). This is the plain JDBC analogue of
         * {@link DlcJooqPersistenceAutoConfiguration#connectionProvider}. A connection obtained outside a
         * transaction is closed again once the operation is done ({@code DataSourceUtils.releaseConnection}).
         *
         * @param dataSource               the data source to resolve the current transaction's connection from
         * @return a {@link JdbcConnectionProvider} instance configured with the given data source
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean({JdbcConnectionProvider.class, DomainPersistenceProvider.class})
        public JdbcConnectionProvider jdbcConnectionProvider(
            DataSource dataSource) {
            return new JdbcConnectionProvider() {
                @Override
                public Connection getConnection() {
                    return DataSourceUtils.getConnection(dataSource);
                }

                // closes a connection obtained outside a transaction, keeps one bound to a transaction open
                @Override
                public void releaseConnection(Connection connection) {
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
            };
        }

        /**
         * Creates a {@link JdbcDialect} bean, resolved from the same SQL dialect property/attribute
         * {@link DlcJooqPersistenceAutoConfiguration#configuration} uses.
         *
         * @return a {@link JdbcDialect} matching the configured SQL dialect
         * @throws DLCAutoConfigException if the SQL dialect property is missing or not one of the supported
         *                                 dialects (H2, POSTGRES, MYSQL, ORACLE, SQLSERVER)
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean({JdbcDialect.class, DomainPersistenceProvider.class})
        public JdbcDialect jdbcDialect() {
            var property = environment.getProperty("dlc.features.persistence.sql-dialect");
            if (property == null) {
                throw DLCAutoConfigException.fail(
                    "Property 'sqlDialect' is missing. Specify 'dlc.features.persistence.sql-dialect' or "
                        + "'jooqSqlDialect' on '@EnableDlc'.");
            }
            return switch (property.toUpperCase(Locale.ROOT)) {
                case "H2" -> new H2JdbcDialect();
                case "POSTGRES" -> new PostgresJdbcDialect();
                case "MYSQL" -> new MySqlJdbcDialect();
                case "ORACLE" -> new OracleJdbcDialect();
                case "SQLSERVER" -> new SqlServerJdbcDialect();
                default -> throw DLCAutoConfigException.fail(
                    "Unsupported SQL dialect '%s' for jdbc-integration. Supported values: H2, POSTGRES, MYSQL, "
                        + "ORACLE, SQLSERVER.",
                    property);
            };
        }

        /**
         * Creates a {@link JdbcSchemaMetadata} bean, reading the database schema once via a short-lived
         * connection obtained directly from the {@link DataSource} - a one-off, startup-time read, outside any
         * transaction.
         * <p>
         * Reads all schemas visible through the connection by default; set
         * {@code dlc.features.persistence.jdbc.schema-pattern} to narrow the read to a single schema - see
         * {@code JdbcSchemaMetadata}'s class javadoc for when that is required (a same-named table visible in
         * more than one schema).
         *
         * @param dataSource the data source to read the schema from
         * @return the schema metadata snapshot
         * @throws DLCAutoConfigException if the schema could not be read
         */
        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean({JdbcSchemaMetadata.class, DomainPersistenceProvider.class})
        public JdbcSchemaMetadata jdbcSchemaMetadata(DataSource dataSource) {
            var schemaPattern = environment.getProperty("dlc.features.persistence.jdbc.schema-pattern");
            try (Connection connection = dataSource.getConnection()) {
                return schemaPattern == null
                    ? JdbcSchemaMetadata.read(connection)
                    : JdbcSchemaMetadata.read(connection, schemaPattern);
            } catch (SQLException e) {
                throw DLCAutoConfigException.fail("Failed to read the JDBC schema metadata.", e);
            }
        }

        /**
         * Creates a {@link JdbcDomainPersistenceProvider} bean for handling domain-specific persistence tasks.
         * The method configures the provider with necessary dependencies such as domain object builders,
         * custom record mappers, the resolved dialect and schema metadata, and a domain mirror.
         *
         * @param domainObjectBuilderProvider the provider for building domain objects from database records
         * @param customRecordMappers         a set of custom mappers for converting database records to domain objects
         * @param domainMirror                the domain mirror for reflection and metadata about domain types,
         *                                     needed for correct order of bean instantiation
         * @param transactionCacheProvider     the provider of the transaction cache -
         *                                     {@link #dlcJdbcTransactionCacheProvider} or a bean replacing it; none if
         *                                     the cache is disabled
         * @param jdbcConnectionProvider       the connection provider every repository/fetcher built from the
         *                                     resulting provider reads directly
         * @param jdbcDialect                  the resolved SQL dialect
         * @param jdbcSchemaMetadata           the schema metadata snapshot
         * @return a configured {@link JdbcDomainPersistenceProvider} instance
         */
        @Bean
        @ConditionalOnMissingBean(DomainPersistenceProvider.class)
        @ConditionalOnBean(DomainMirror.class)
        public JdbcDomainPersistenceProvider domainPersistenceProvider(
            DomainObjectBuilderProvider domainObjectBuilderProvider,
            Set<RecordMapper<?, ?, ?>> customRecordMappers,
            DomainMirror domainMirror,
            ObjectProvider<TransactionCacheProvider<JdbcRecord>> transactionCacheProvider,
            JdbcConnectionProvider jdbcConnectionProvider,
            JdbcDialect jdbcDialect,
            JdbcSchemaMetadata jdbcSchemaMetadata
        ) {
            var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder
                .newConfig()
                .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
                .withCustomRecordMappers(customRecordMappers)
                .withConnectionProvider(jdbcConnectionProvider)
                .withDialect(jdbcDialect)
                .withSchemaMetadata(jdbcSchemaMetadata);
            if (TransactionCacheProperties.enabled(environment)) {
                var cacheProvider = transactionCacheProvider.getIfAvailable();
                if (cacheProvider == null) {
                    // the autoconfig's own provider is left out only for an existing one, which does not fit
                    throw DLCAutoConfigException.fail(
                        "A TransactionCacheProvider bean replaces the one of the JDBC persistence autoconfig, but "
                            + "none is a TransactionCacheProvider<JdbcRecord>. Provide one of that type, or switch "
                            + "the cache off via '%s=false'.", TransactionCacheProperties.ENABLED);
                }
                configuration.withTransactionCacheProvider(cacheProvider);
            } else {
                configuration.withTransactionCacheEnabled(false);
            }
            return new JdbcDomainPersistenceProvider(configuration.make());
        }

        /**
         * Creates an {@link EntityIdentityProvider} bean for providing identity information for entities.
         * This implementation uses a native database sequence per identity type to generate identities.
         *
         * @param jdbcConnectionProvider the connection provider used to query the sequence
         * @param jdbcDialect            the dialect providing the "next sequence value" SQL syntax
         * @return a {@link JdbcEntityIdentityProvider} instance
         */
        @Bean
        @ConditionalOnMissingBean(EntityIdentityProvider.class)
        public EntityIdentityProvider jdbcIdentityProvider(
            JdbcConnectionProvider jdbcConnectionProvider, JdbcDialect jdbcDialect) {
            return new JdbcEntityIdentityProvider(jdbcConnectionProvider, jdbcDialect);
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

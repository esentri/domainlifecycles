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
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import java.util.Set;
import javax.sql.DataSource;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * Autoconfiguration for JOOQ persistence functionality in the DLC framework.
 * This configuration sets up the necessary beans for database access and persistence operations
 * using JOOQ as the underlying database access technology.
 * <p>
 * This configuration provides default implementations for JOOQ-specific components including
 * connection providers, DSL contexts, domain persistence providers, and entity identity providers.
 * All beans can be overridden by providing custom implementations.
 * </p>
 *
 * @author leonvoellinger
 */
@AutoConfiguration
@EnableConfigurationProperties(DlcJooqPersistenceProperties.class)
@AutoConfigureAfter({DlcBuilderAutoConfiguration.class, DataSourceAutoConfiguration.class, DlcDomainAutoConfiguration.class, JooqAutoConfiguration.class})
public class DlcJooqPersistenceAutoConfiguration {

    private @Value("${jooqRecordPackage}") String jooqRecordPackage;
    private @Value("${jooqSqlDialect}") SQLDialect jooqSqlDialect;

    /**
     * Creates a connection provider for managing database connections.
     * Uses a transaction-aware proxy to ensure proper transaction handling.
     *
     * @param dataSource the data source used for database connections
     * @return a configured {@link DataSourceConnectionProvider}
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    /**
     * Creates a JOOQ configuration with optimistic locking enabled.
     * DLC requires optimistic locking to be enabled in the JOOQ configuration
     * for proper concurrency control in domain operations.
     *
     * @param dataSource the data source for database connections
     * @param persistenceProperties configuration properties for persistence settings
     * @return a configured {@link DefaultConfiguration} with optimistic locking enabled
     * @throws DLCAutoConfigException if the SQL dialect property is missing
     */
    @Bean
    @ConditionalOnBean({DataSource.class})
    @ConditionalOnMissingBean(Configuration.class)
    public DefaultConfiguration configuration(DataSource dataSource, DlcJooqPersistenceProperties persistenceProperties) {
        final var jooqConfig = new DefaultConfiguration();
        jooqConfig.settings().setExecuteWithOptimisticLocking(true);
        jooqConfig.setConnectionProvider(connectionProvider(dataSource));

        SQLDialect sqlDialect;

        if(persistenceProperties != null && persistenceProperties.getSqlDialect() != null) {
            sqlDialect = persistenceProperties.getSqlDialect();
        } else if (jooqSqlDialect != null) {
            sqlDialect = jooqSqlDialect;
        } else {
            throw DLCAutoConfigException.fail("Property 'sqlDialect' is missing. Make sure you specified a property " +
                "called 'dlc.persistence.sqlDialect' or add a 'jooqSqlDialect' value on the @EnableDLC annotation.");
        }

        jooqConfig.set(sqlDialect);
        return jooqConfig;
    }

    /**
     * Creates a DSL context for executing JOOQ queries and operations.
     * All DLC repository implementations require a {@link DSLContext} instance
     * for database interaction.
     *
     * @param dataSource the data source for database connections
     * @param persistenceProperties configuration properties for persistence settings
     * @return a configured {@link DSLContext} instance
     */

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(DSLContext.class)
    public DSLContext dslContext(DataSource dataSource, DlcJooqPersistenceProperties persistenceProperties) {
        return new DefaultDSLContext(configuration(dataSource, persistenceProperties));
    }

    /**
     * Creates the main domain persistence provider for JOOQ-based persistence operations.
     * This provider handles the mapping between domain objects and JOOQ records.
     * <p>
     * <strong>IMPORTANT:</strong> A record package where all JOOQ record classes are generated must be defined
     * either through properties or the @EnableDLC annotation.
     * </p>
     *
     * @param domainObjectBuilderProvider provider for building domain objects during deserialization
     * @param customRecordMappers all custom record mappers defined as Spring beans
     * @param persistenceProperties configuration properties containing the JOOQ record package
     * @return a configured {@link JooqDomainPersistenceProvider} instance
     * @throws DLCAutoConfigException if the jooqRecordPackage property is missing
     */

    @Bean
    @ConditionalOnMissingBean
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
        } else if(jooqRecordPackage != null && !jooqRecordPackage.isBlank()) {
            recordPackage = jooqRecordPackage;
        } else {
            throw DLCAutoConfigException.fail("Property 'jooqRecordPackage' is missing. Make sure you specified a " +
                "property called 'dlc.persistence.jooqRecordPackage' or add a 'jooqRecordPackage' value on the " +
                "@EnableDLC annotation.");
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
     * Creates an entity identity provider for managing entity IDs in the persistence layer.
     * This provider makes it possible for new Entities or AggregateRoots to be passed to the application
     * from outside (e.g., via a REST controller) and ensures that new instances receive new IDs
     * from the corresponding database sequences or other ID providers.
     * <p>
     * The identity provider assigns new ID values during the deserialization process.
     * This is necessary because DLC requires valid instances with non-null IDs within the domain.
     * </p>
     * <p>
     * This component is primarily used together with DLC Jackson integration for JSON processing.
     * </p>
     *
     * @param dslContext the JOOQ DSL context for database operations
     * @return a configured {@link EntityIdentityProvider}
     */
    @Bean
    @ConditionalOnMissingBean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {
        return new JooqEntityIdentityProvider(dslContext);
    }
}

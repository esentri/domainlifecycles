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
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.Set;

@AutoConfiguration
@EnableConfigurationProperties(DlcJooqPersistenceProperties.class)
@AutoConfigureAfter({DlcBuilderAutoConfiguration.class, DataSourceAutoConfiguration.class, DlcDomainAutoConfiguration.class, JooqAutoConfiguration.class})
public class DlcJooqPersistenceAutoConfiguration {

    private @Value("${jooqRecordPackage}") String jooqRecordPackage;
    private @Value("${jooqSqlDialect}") SQLDialect jooqSqlDialect;

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    /**
     * DLC requires optimistic locking in JOOQ Config
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
     * All DLC repository implementations need a {@link org.jooq.DSLContext} instance.
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(DSLContext.class)
    public DSLContext dslContext(DataSource dataSource, DlcJooqPersistenceProperties persistenceProperties) {
        return new DefaultDSLContext(configuration(dataSource, persistenceProperties));
    }

    /**
     * IMPORTANT: A record package where all JOOQ record classes are generated must be defined.
     *
     * @param domainObjectBuilderProvider {@link DomainObjectBuilderProvider}
     * @param customRecordMappers         {@link RecordMapper} all record mappers (should be defined as spring beans
     *                                                        to work like that)
     * @return {@link JooqDomainPersistenceProvider} instance configured
     */
    @Bean
    @ConditionalOnMissingBean
    public JooqDomainPersistenceProvider domainPersistenceProvider(
        DomainObjectBuilderProvider domainObjectBuilderProvider,
        Set<RecordMapper<?, ?, ?>> customRecordMappers,
        DlcJooqPersistenceProperties persistenceProperties
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
     * The entity identity provider, makes it possible that new Entities or AggregateRoots are to the application
     * from outside (via a REST controller) and that for new instances new IDs are fetched from the corresponding
     * database sequences or other ID providers.
     * The identity provider assignes the new id values within the deserialization process. We need that because we
     * only want to valid instances with nonnull IDs within our domain.
     * <p>
     * Only used together with DLC Jackson integration, see below...
     */
    @Bean
    @DependsOn("dslContext")
    @ConditionalOnMissingBean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {
        return new JooqEntityIdentityProvider(dslContext);
    }
}

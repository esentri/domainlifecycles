package io.domainlifecycles.springboot4.config;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.test.springboot3.tables.records.AktionsCodeBv3Record;
import java.util.Set;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

@Configuration
public class PersistenceConfig {

    @Bean
    @DependsOn("initializedDomain")
    public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                   Set<RecordMapper<?,?,?>> customRecordMappers) {

        JooqDomainPersistenceConfiguration jooqDomainPersistenceConfiguration = JooqDomainPersistenceConfiguration
            .JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withRecordPackage("io.domainlifecycles.test.springboot3.tables.records")
            .withCustomRecordMappers(customRecordMappers)
            .withEntityValueObjectRecordTypeConfiguration(
                new EntityValueObjectRecordTypeConfiguration(
                    BestellungBv3.class,
                    AktionsCodeBv3.class,
                    AktionsCodeBv3Record.class,
                    "aktionsCodes"
                )
            )
            .make();

        return new JooqDomainPersistenceProvider(
            jooqDomainPersistenceConfiguration);
    }

    @Bean
    @DependsOn("initializedDomain")
    EntityIdentityProvider customIdentityProvider(DSLContext dslContext) {
        return new EntityIdentityProvider() {
            private final JooqEntityIdentityProvider provider = new JooqEntityIdentityProvider(dslContext);

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if (entityTypeName.equals(TestRootSimpleUuid.class.getName())) {
                    return new TestRootSimpleUuidId(UUID.randomUUID());
                } else {
                    return provider.provideFor(entityTypeName);
                }
            }
        };
    }
}

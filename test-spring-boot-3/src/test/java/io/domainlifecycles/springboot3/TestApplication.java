package io.domainlifecycles.springboot3;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import io.domainlifecycles.test.springboot3.tables.records.AktionsCodeBv3Record;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.converter.ConverterStringVo;
import tests.shared.jackson.TypeTestValueObject;
import tests.shared.openapi.TestId;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.validation.jakarta.ValidatedAggregateRoot;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class TestApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory(
            TestId.class.getPackageName(),
            TypeTestValueObject.class.getPackageName(),
            "io.domainlifecycles.springboot3",
            ConverterStringVo.class.getPackageName(),
            BestellungBv3.class.getPackageName(),
            OptionalAggregate.class.getPackageName(),
            ValidatedAggregateRoot.class.getPackageName(),
            TestRootSimpleUuid.class.getPackageName(),
            TestRootSimple.class.getPackageName(),
            AutoMappedVoAggregateRoot.class.getPackageName()
        ));
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(TestApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        ValidationDomainClassExtender.extend("tests");
        log.info("ValidationExtension Done!");
    }

    @Bean
    public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                   Set<RecordMapper<?, ?, ?>> customRecordMappers) {

        JooqDomainPersistenceConfiguration jooqDomainPersistenceConfiguration = JooqDomainPersistenceConfiguration
            .JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withRecordPackage("io.domainlifecycles.test.springboot3.tables.records")
            .withCustomRecordMappers(customRecordMappers)
            .withEntityValueObjectRecordTypeConfiguration(
                new EntityValueObjectRecordTypeConfiguration<>(
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
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    @Bean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {

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

    @Bean
    DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                            DomainObjectBuilderProvider domainObjectBuilderProvider,
                                            EntityIdentityProvider entityIdentityProvider) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

    @Bean
    public DefaultDSLContext dslContext(DataSource dataSource) {
        return new DefaultDSLContext(configuration(dataSource));
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider
            (new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultConfiguration configuration(DataSource dataSource) {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.settings().setExecuteWithOptimisticLocking(true);
        jooqConfiguration.setConnectionProvider(connectionProvider(dataSource));
        jooqConfiguration.set(SQLDialect.H2);
        return jooqConfiguration;
    }

    @Bean
    public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
        log.debug("LOCALE: " + Locale.getDefault());
        return new DlcOpenApiCustomizer(springDocConfigProperties);

    }
}

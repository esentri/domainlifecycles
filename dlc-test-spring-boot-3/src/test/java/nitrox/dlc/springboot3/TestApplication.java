/*
 *
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

package nitrox.dlc.springboot3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nitrox.dlc.access.classes.DefaultClassProvider;
import nitrox.dlc.access.classes.ClassProvider;
import nitrox.dlc.access.object.DefaultIdentityFactory;
import nitrox.dlc.access.object.IdentityFactory;
import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.jackson.api.JacksonMappingCustomizer;
import nitrox.dlc.jackson.module.DlcJacksonModule;
import nitrox.dlc.jooq.configuration.JooqDomainPersistenceConfiguration;
import nitrox.dlc.jooq.imp.JooqEntityIdentityProvider;
import nitrox.dlc.jooq.imp.provider.JooqDomainPersistenceProvider;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.persistence.mapping.RecordMapper;
import nitrox.dlc.persistence.provider.EntityIdentityProvider;
import nitrox.dlc.persistence.records.EntityValueObjectRecordTypeConfiguration;
import nitrox.dlc.springdoc2.openapi.DlcOpenApiCustomizer;
import nitrox.dlc.test.springboot3.tables.records.AktionsCodeBv3Record;
import nitrox.dlc.validation.extend.ValidationDomainClassExtender;
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
import tests.shared.validation.javax.ValidatedAggregateRoot;

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
            "nitrox.dlc.springboot3",
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
    public void postConstruct(){
        ValidationDomainClassExtender.extend("tests");
        log.info("ValidationExtension Done!");
    }

    @Bean
    public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                   Set<RecordMapper<?,?,?>> customRecordMappers){

        JooqDomainPersistenceConfiguration jooqDomainPersistenceConfiguration = JooqDomainPersistenceConfiguration
            .JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withRecordPackage("nitrox.dlc.test.springboot3.tables.records")
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

        JooqDomainPersistenceProvider domainPersistenceProvider = new JooqDomainPersistenceProvider(jooqDomainPersistenceConfiguration);

        return domainPersistenceProvider;
    }

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider(){
        return new InnerClassDomainObjectBuilderProvider();
    }

    @Bean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {

        return new EntityIdentityProvider() {

            private final JooqEntityIdentityProvider provider = new JooqEntityIdentityProvider(dslContext);
            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if(entityTypeName.equals(TestRootSimpleUuid.class.getName())){
                    return new TestRootSimpleUuidId(UUID.randomUUID());
                }else{
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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public DefaultDSLContext dslContext(DataSource dataSource) {
        return new DefaultDSLContext(configuration(dataSource));
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider
            (new TransactionAwareDataSourceProxy(dataSource));
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

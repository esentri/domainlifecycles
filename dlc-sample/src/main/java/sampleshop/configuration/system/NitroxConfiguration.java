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

package sampleshop.configuration.system;



import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.ReadModelProvider;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.events.api.DomainEventsConfiguration;
import nitrox.dlc.jackson.api.JacksonMappingCustomizer;
import nitrox.dlc.jackson.module.DlcJacksonModule;
import nitrox.dlc.jooq.configuration.JooqDomainPersistenceConfiguration;
import nitrox.dlc.jooq.imp.JooqEntityIdentityProvider;
import nitrox.dlc.jooq.imp.provider.JooqDomainPersistenceProvider;
import nitrox.dlc.persistence.mapping.RecordMapper;
import nitrox.dlc.persistence.provider.EntityIdentityProvider;
import nitrox.dlc.services.Services;
import nitrox.dlc.services.api.ServiceProvider;
import nitrox.dlc.springdoc2.openapi.DlcOpenApiCustomizer;
import org.jooq.DSLContext;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sampleshop.outbound.event.SpringPersistenceEventPublisher;

import java.util.List;
import java.util.Set;
/**
 * Spring configuration for NitroX DLC.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@Configuration
public class NitroxConfiguration {

    private static final String DOMAIN_PKG = "sampleshop";

    private static final String JOOQ_RECORD_PKG = DOMAIN_PKG + ".tables.records";

    /**
     * IMPORTANT: A record package where all JOOQ record classes are generated must be defined.
     *
     * @param domainObjectBuilderProvider
     * @param customRecordMappers {@link RecordMapper} all record mappers (should be defined as spring beans to work like that)
     * @return {@link JooqDomainPersistenceProvider} instance configured
     */
    @Bean
    public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                   Set<RecordMapper<?, ?, ?>> customRecordMappers
                                                                   ) {
        return new JooqDomainPersistenceProvider(
            JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder
                .newConfig()
                .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
                .withCustomRecordMappers(customRecordMappers)
                .withRecordPackage(JOOQ_RECORD_PKG)
                .make());
    }

    /**
     * Default configuration to make NitroX DLC work with inner builders or Lombok builders.
     */
    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    /**
     * The entity identity provider, makes it possible that new Entities or AggregateRoots are to the application
     * from outside (via a REST controller) and that for new instances new IDs are fetched from the corresponding database sequences or other ID providers.
     * The identity provider assignes the new id values within the deserialization process. We need that because we only want to valid instances with nonnull IDs within our domain.
     *
     * Only used together with NitroX DLC Jackson integration, see below...
     */
    @Bean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {
        return new JooqEntityIdentityProvider(dslContext);
    }

    /**
     * NitroX DLC Jackson integration
     */
    @Bean
    DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                            DomainObjectBuilderProvider domainObjectBuilderProvider,
                                            EntityIdentityProvider entityIdentityProvider
                                            ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }


    /**
     * Using the Spring event bus to publish NitroX DLC persistence actions.
     */
    @Bean
    public SpringPersistenceEventPublisher springPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringPersistenceEventPublisher(applicationEventPublisher);
    }

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of various types of services.
     * It takes three parameters: repositories, applicationServices, and domainServices, which are lists of Repository, ApplicationService, and DomainService instances respectively
     */
    @Bean
    public ServiceProvider serviceProvider(
        List<Repository<?,?>> repositories,
        List<ApplicationService> applicationServices,
        List<DomainService> domainServices,
        List<ReadModelProvider<?>> readModelProviders,
        List<OutboundService> outboundServices
    ){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        readModelProviders.forEach(services::registerReadModelProviderInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        return services;
    }

    /**
     * Using the Spring event bus to publish NitroX DLC domain events.
     */
    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(ServiceProvider serviceProvider, PlatformTransactionManager transactionManager) {
        return new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withServiceProvider(serviceProvider)
            .withSpringPlatformTransactionManager(transactionManager)
            .make();
    }

    /**
     * Spring Doc Open API integration of NitroX DLC
     */
    @Bean
    public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
        return new DlcOpenApiCustomizer(springDocConfigProperties);
    }
}

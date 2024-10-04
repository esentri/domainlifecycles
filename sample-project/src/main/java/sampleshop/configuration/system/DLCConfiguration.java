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



import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
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
 * Spring configuration for DLC.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@Configuration
public class DLCConfiguration {

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
     * Default configuration to make DLC work with inner builders or Lombok builders.
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
     * Only used together with DLC Jackson integration, see below...
     */
    @Bean
    EntityIdentityProvider identityProvider(DSLContext dslContext) {
        return new JooqEntityIdentityProvider(dslContext);
    }

    /**
     * DLC Jackson integration
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
     * Using the Spring event bus to publish DLC persistence actions.
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
        List<QueryClient<?>> queryClients,
        List<OutboundService> outboundServices
    ){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        queryClients.forEach(services::registerQueryClientInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        return services;
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
        var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider,
            5,
            true).processingChannel("default");

        var router = new DomainEventTypeBasedRouter(List.of(channel));
        router.defineDefaultChannel("default");
        return new ChannelRoutingConfiguration(router);
    }

    /**
     * Spring Doc Open API integration of DLC
     */
    @Bean
    public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
        return new DlcOpenApiCustomizer(springDocConfigProperties);
    }
}

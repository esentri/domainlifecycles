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

package nitrox.dlc.events.spring;

import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.ReadModelProvider;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.events.ADomainService;
import nitrox.dlc.events.AReadModelProvider;
import nitrox.dlc.events.ARepository;
import nitrox.dlc.events.AnApplicationService;
import nitrox.dlc.events.AnOutboundService;
import nitrox.dlc.events.PassThroughDomainEvent;
import nitrox.dlc.events.api.DomainEventsConfiguration;
import nitrox.dlc.events.publish.direct.DirectSpringTransactionalDomainEventPublisher;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.services.Services;
import nitrox.dlc.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Locale;

@SpringBootApplication
@EnableTransactionManagement
public class TestApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("nitrox.dlc.events"));
    }

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }

    @Bean
    public AnApplicationService anApplicationService(){
        return new AnApplicationService();
    }

    @Bean
    public ADomainService aDomainService(){
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository(){
        return new ARepository();
    }

    @Bean
    public AReadModelProvider aReadModelProvider(){
        return new AReadModelProvider();
    }

    @Bean
    public AnOutboundService anOutboundService(){
        return new AnOutboundService();
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
     * Using DLC Events to publish NitroX DLC domain events.
     */
    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(ServiceProvider serviceProvider, PlatformTransactionManager transactionManager) {
        var config =  new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withSpringPlatformTransactionManager(transactionManager)
            .withServiceProvider(serviceProvider)
            .make();
        var pub = (DirectSpringTransactionalDomainEventPublisher)config.domainEventPublisher;
        pub.setPassThroughEventTypes(List.of(PassThroughDomainEvent.class));
        return config;
    }

}

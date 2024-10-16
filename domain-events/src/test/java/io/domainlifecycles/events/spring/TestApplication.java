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

package io.domainlifecycles.events.spring;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.PassThroughDomainEvent;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.publish.direct.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
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
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));
    }

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }

    @Bean
    public AnApplicationService anApplicationService() {
        return new AnApplicationService();
    }

    @Bean
    public ADomainService aDomainService() {
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository() {
        return new ARepository();
    }

    @Bean
    public AQueryClient aQueryClient() {
        return new AQueryClient();
    }

    @Bean
    public AnOutboundService anOutboundService() {
        return new AnOutboundService();
    }

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of
     * various types of services.
     * It takes three parameters: repositories, applicationServices, and domainServices, which are lists of
     * Repository, ApplicationService, and DomainService instances respectively
     */
    @Bean
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
        var services = new Services();
        serviceKinds.forEach(services::registerServiceKindInstance);
        return services;
    }

    /**
     * Using DLC Events to publish DLC domain events.
     */
    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(ServiceProvider serviceProvider,
                                                               PlatformTransactionManager transactionManager) {
        var config = new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withSpringPlatformTransactionManager(transactionManager)
            .withServiceProvider(serviceProvider)
            .make();
        var pub = (DirectSpringTransactionalDomainEventPublisher) config.domainEventPublisher;
        pub.setPassThroughEventTypes(List.of(PassThroughDomainEvent.class));
        return config;
    }

}

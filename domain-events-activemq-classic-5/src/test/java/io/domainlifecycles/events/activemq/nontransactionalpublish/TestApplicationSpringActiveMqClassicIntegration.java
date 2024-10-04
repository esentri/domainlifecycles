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

package io.domainlifecycles.events.activemq.nontransactionalpublish;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.activemq.domain.ADomainService;
import io.domainlifecycles.events.activemq.domain.AQueryClient;
import io.domainlifecycles.events.activemq.domain.ARepository;
import io.domainlifecycles.events.activemq.domain.AnApplicationService;
import io.domainlifecycles.events.activemq.domain.AnOutboundService;
import io.domainlifecycles.events.activemq.domain.AnotherApplicationService;
import io.domainlifecycles.events.activemq.domain.AnotherService;
import io.domainlifecycles.events.activemq.domain.TransactionalCounterService;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.InitializedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Locale;

@SpringBootApplication()
public class TestApplicationSpringActiveMqClassicIntegration {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationSpringActiveMqClassicIntegration.class).run(args);
    }

    @Bean
    InitializedDomain initializedDomain(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));
        return Domain.getInitializedDomain();
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
    public AQueryClient aQueryClient(){
        return new AQueryClient();
    }

    @Bean
    public AnOutboundService anOutboundService(){
        return new AnOutboundService();
    }

    @Bean
    public AnotherService anotherService(){
        return new AnotherService();
    }

    @Bean
    public AnotherApplicationService anotherApplicationService(AnotherService anotherService) { return new AnotherApplicationService(anotherService); }

    @Bean
    public TransactionalCounterService transactionalCounterService(JdbcTemplate jdbcTemplate){
        return new TransactionalCounterService(jdbcTemplate);
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
    @DependsOn("initializedDomain")
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    @Bean
    @DependsOn("initializedDomain")
    DlcJacksonModule dlcModuleConfiguration(
        List<? extends JacksonMappingCustomizer<?>> customizers,
        DomainObjectBuilderProvider domainObjectBuilderProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(
            domainObjectBuilderProvider
        );
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

}

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

package nitrox.dlc.events.springoutbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.QueryClient;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.events.ADomainService;
import nitrox.dlc.events.AQueryClient;
import nitrox.dlc.events.ARepository;
import nitrox.dlc.events.AnApplicationService;
import nitrox.dlc.events.AnOutboundService;
import nitrox.dlc.events.api.DomainEventsConfiguration;
import nitrox.dlc.events.publish.outbox.api.TransactionalOutbox;
import nitrox.dlc.events.publish.outbox.impl.SpringJdbcOutbox;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.services.Services;
import nitrox.dlc.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class OutboxTestApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("nitrox.dlc.events"));
    }

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(OutboxTestApplication.class).run(args);
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
    public AnOutboundService anOutboundService(){
        return new AnOutboundService();
    }

    @Bean
    public AQueryClient aQueryClient(){
        return new AQueryClient();
    }

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of various types of services.
     */
    @Bean
    public ServiceProvider serviceProvider(List<Repository<?,?>> repositories,
                                           List<ApplicationService> applicationServices,
                                           List<DomainService> domainServices,
                                           List<OutboundService> outboundServices,
                                           List<QueryClient<?>> queryClients){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        queryClients.forEach(services::registerQueryClientInstance);
        return services;
    }

    /**
     * Using DLC Events to publish NitroX DLC domain events.
     */
    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(TransactionalOutbox transactionalOutbox, ServiceProvider serviceProvider, PlatformTransactionManager transactionManager) {
        var config =  new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withSpringPlatformTransactionManager(transactionManager)
            .withServiceProvider(serviceProvider)
            .withTransactionalOutbox(transactionalOutbox)
            .make();
        return config;
    }

    /**
     * Creates a new instance of {@link TransactionalOutbox} using the provided {@link DataSource},
     * {@link ObjectMapper}, and {@link PlatformTransactionManager}.
     */
    @Bean
    public TransactionalOutbox transactionalOutbox(DataSource dataSource, ObjectMapper objectMapper, PlatformTransactionManager platformTransactionManager){
        return new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager);
    }

}

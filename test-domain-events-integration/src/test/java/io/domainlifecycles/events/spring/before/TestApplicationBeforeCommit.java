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

package io.domainlifecycles.events.spring.before;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.DomainMirror;
import testdomain.general.ADomainService;
import testdomain.general.AQueryHandler;
import testdomain.general.ARepository;
import testdomain.general.AnApplicationService;
import testdomain.general.AnOutboundService;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
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
public class TestApplicationBeforeCommit {


    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationBeforeCommit.class).run(args);
    }

    @Bean
    DomainMirror initializedDomain(){
        Domain.unInitialize();
        Domain.initialize(new ReflectiveDomainMirrorFactory("testdomain.general"));
        return Domain.getDomainMirror();
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
    public AQueryHandler aQueryHandler() {
        return new AQueryHandler();
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

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
        var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider,
            5,
            false).processingChannel("c1");

        var router = new DomainEventTypeBasedRouter(List.of(channel));
        router.defineDefaultChannel("c1");
        return new ChannelRoutingConfiguration(router);
    }

}

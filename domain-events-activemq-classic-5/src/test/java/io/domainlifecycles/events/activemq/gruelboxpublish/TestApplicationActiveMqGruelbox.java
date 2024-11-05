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

package io.domainlifecycles.events.activemq.gruelboxpublish;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.activemq.domain.ADomainService;
import io.domainlifecycles.events.activemq.domain.AQueryClient;
import io.domainlifecycles.events.activemq.domain.ARepository;
import io.domainlifecycles.events.activemq.domain.AnApplicationService;
import io.domainlifecycles.events.activemq.domain.AnOutboundService;
import io.domainlifecycles.events.activemq.domain.TransactionalCounterService;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.InitializedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Locale;

@SpringBootApplication
@EnableTransactionManagement
public class TestApplicationActiveMqGruelbox {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationActiveMqGruelbox.class).run(args);
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
    public TransactionalCounterService transactionalCounterService(JdbcTemplate jdbcTemplate){
        return new TransactionalCounterService(jdbcTemplate);
    }

    @Bean
    public ServiceProvider serviceProvider(
        List<ServiceKind> serviceInstances
    ){
        var services = new Services(serviceInstances);
        return services;
    }

}

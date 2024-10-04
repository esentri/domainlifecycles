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

package io.domainlifecycles.events.jakartajmsspringtxseparate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.jakarta.jms.api.SpringTransactionJakartaJmsChannelFactory;
import io.domainlifecycles.events.mq.api.AbstractMqConsumingChannel;
import io.domainlifecycles.events.mq.api.AbstractMqPublishingChannel;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@EnableJms
public class SpringJmsConfigSeparate {

    @Bean
    @DependsOn("initializedDomain")
    public SpringTransactionJakartaJmsChannelFactory springtransactionJakartaJmsChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionJakartaJmsChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
    }

    @Bean(destroyMethod = "close")
    public AbstractMqPublishingChannel channelPub(SpringTransactionJakartaJmsChannelFactory factory){
        return factory.publishOnlyChannel("jms2Pub");
    }

    @Bean(destroyMethod = "close")
    public AbstractMqConsumingChannel channelCons(SpringTransactionJakartaJmsChannelFactory factory){
        return factory.consumeOnlyChannel("jms2Cons");
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("jms2Pub");
        return new ChannelRoutingConfiguration(router);
    }

    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

}

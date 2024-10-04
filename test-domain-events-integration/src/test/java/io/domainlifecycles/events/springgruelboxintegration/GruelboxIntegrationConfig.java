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

package io.domainlifecycles.events.springgruelboxintegration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.events.MyTransactionOutboxListener;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Import({SpringTransactionManager.class})
@Slf4j
public class GruelboxIntegrationConfig {

    @Bean
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        DomainEventsInstantiator domainEventsInstantiator,
        TransactionOutboxListener transactionOutboxListener
    ) {
        return TransactionOutbox.builder()
            .instantiator(domainEventsInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .listener(transactionOutboxListener)
            .build();
    }

    @Bean
    public ProcessingChannel gruelboxChannel(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator
        ).processingChannel("c1");
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("c1");
        return new ChannelRoutingConfiguration(router);
    }

    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }

    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    public MyTransactionOutboxListener transactionOutboxListener(){
        return new MyTransactionOutboxListener();
    }

}

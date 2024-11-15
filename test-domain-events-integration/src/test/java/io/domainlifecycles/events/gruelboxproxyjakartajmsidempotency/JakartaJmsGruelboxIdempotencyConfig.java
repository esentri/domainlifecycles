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

package io.domainlifecycles.events.gruelboxproxyjakartajmsidempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.CounterDomainEvent;
import io.domainlifecycles.events.TransactionalCounterService;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;
import io.domainlifecycles.events.jakarta.jms.api.GruelboxProxyJakartaJmsChannelFactory;
import io.domainlifecycles.events.mq.api.AbstractMqProcessingChannel;
import io.domainlifecycles.events.mq.consume.SpringTransactionalIdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@EnableJms
@Import({SpringTransactionManager.class})
public class JakartaJmsGruelboxIdempotencyConfig {

    @Bean
    @DependsOn("initializedDomain")
    public IdempotencyConfiguration idempotencyConfiguration(){
        var idempotency = new IdempotencyConfiguration();
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    ADomainService.class,
                    "onDomainEvent",
                    ADomainEvent.class,
                    (e)-> ((ADomainEvent)e).message()
                )
            );
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    ARepository.class,
                    "onADomainEvent",
                    ADomainEvent.class,
                    (e)-> ((ADomainEvent)e).message()
                )
            );
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    AnApplicationService.class,
                    "onADomainEvent",
                    ADomainEvent.class,
                    (e)-> ((ADomainEvent)e).message()
                )
            );
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    AQueryClient.class,
                    "onADomainEvent",
                    ADomainEvent.class,
                    (e)-> ((ADomainEvent)e).message()
                )
            );
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    AnOutboundService.class,
                    "onADomainEvent",
                    ADomainEvent.class,
                    (e)-> ((ADomainEvent)e).message()
                )
            );
        idempotency
            .addConfigurationEntry(
                new IdempotencyConfigurationEntry(
                    TransactionalCounterService.class,
                    "counterEventSuccess",
                    CounterDomainEvent.class,
                    (e)-> ((CounterDomainEvent)e).action()
                )
            );
        return idempotency;
    }

    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }

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
    public SpringTransactionalIdempotencyAwareHandlerExecutorProxy springTransactionalIdempotencyAwareHandlerExecutorProxy(
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        IdempotencyConfiguration idempotencyConfiguration,
        TransactionOutbox transactionOutbox,
        PlatformTransactionManager platformTransactionManager


    ){
        return new SpringTransactionalIdempotencyAwareHandlerExecutorProxy(
            transactionalHandlerExecutor,
            idempotencyConfiguration,
            transactionOutbox,
            platformTransactionManager
        );

    }

    @Bean
    public GruelboxProxyJakartaJmsChannelFactory gruelboxProxyActiveMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        SpringTransactionalIdempotencyAwareHandlerExecutorProxy springTransactionalIdempotencyAwareHandlerExecutorProxy
    ){
        return new GruelboxProxyJakartaJmsChannelFactory(
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper,
            transactionOutbox,
            domainEventsInstantiator,
            jmsConnectionFactory,
            springTransactionalIdempotencyAwareHandlerExecutorProxy
        );
    }

    @Bean(destroyMethod = "close")
    public AbstractMqProcessingChannel channel(GruelboxProxyJakartaJmsChannelFactory factory){
        return factory.processingChannel("gruelboxJmsChannelIdempotency");
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("gruelboxJmsChannelIdempotency");
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

    @Bean
    public MyTransactionOutboxListener transactionOutboxListener(){
        return new MyTransactionOutboxListener();
    }

}

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

package io.domainlifecycles.autoconfig.features.multiple.events_builder.gruelbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringInstantiator;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory;
import io.domainlifecycles.events.gruelbox.api.GruelboxProcessingChannel;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import java.util.List;

import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

@Configuration
@Slf4j
@DependsOn("initializedDomain")
public class GruelboxEventAndBuilderAutoConfigTestConfiguration {

    @Bean
    @Lazy
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        SpringInstantiator springInstantiator,
        ObjectMapper objectMapper
    ) {
        return TransactionOutbox.builder()
            .instantiator(springInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .listener(new TransactionOutboxListener() {

                    @Override
                    public void success(TransactionOutboxEntry entry) {
                        log.info("Entry '{}' processed successfully!", entry);
                    }

                    @Override
                    public void failure(TransactionOutboxEntry entry, Throwable cause) {
                        log.error("Entry '{}' failed!", entry, cause);
                    }

                    @Override
                    public void blocked(TransactionOutboxEntry entry, Throwable cause) {
                        log.error("Entry '{}' blocked!", entry, cause);
                    }
                })
            .build();
    }

    @Bean(destroyMethod = "close")
    GruelboxProcessingChannel gruelboxChanne(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator
        ).processingChannel("c1");
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

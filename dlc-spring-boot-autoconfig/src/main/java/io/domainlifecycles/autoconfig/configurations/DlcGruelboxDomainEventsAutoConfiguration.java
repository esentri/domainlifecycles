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

package io.domainlifecycles.autoconfig.configurations;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.spring.SpringInstantiator;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.autoconfig.configurations.gruelbox.GruelboxBackgroundProcessor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Autoconfiguration class for integrating Gruelbox Transaction Outbox with DLC domain events.
 * This configuration enables seamless processing of domain events using the Gruelbox transaction outbox
 * and sets up the necessary beans for instantiation and transaction management.
 *
 * The configuration activates after DlcDomainEventsAutoConfiguration and requires the presence
 * of com.gruelbox.transactionoutbox.TransactionOutbox class on the classpath.
 *
 * It also enables scheduling to facilitate periodic polling of the transaction outbox for processing.
 *
 * @author Mario Herb
 */
@AutoConfiguration(after = DlcDomainEventsAutoConfiguration.class)
@Import({SpringInstantiator.class, SpringTransactionManager.class})
@EnableScheduling
@ConditionalOnClass(name="com.gruelbox.transactionoutbox.TransactionOutbox")
public class DlcGruelboxDomainEventsAutoConfiguration {

    /**
     * Creates a GruelboxBackgroundProcessor for handling transaction outbox operations
     * when both TransactionOutbox and GruelboxChannelFactory beans are available.
     *
     * @param transactionOutbox to be polled
     * @return A configured GruelboxBackgroundProcessor instance
     */
    @Bean
    @ConditionalOnBean(TransactionOutbox.class)
    public GruelboxBackgroundProcessor gruelboxBackgroundProcessor(TransactionOutbox transactionOutbox){
        return new GruelboxBackgroundProcessor(transactionOutbox);
    }

    /**
     * Creates an instance of {@link DomainEventsInstantiator}.
     * This bean is responsible for instantiating objects used in the processing of domain events,
     * particularly supporting the GruelboxDomainEventDispatcher and IdempotentExecutor classes.
     *
     * @return an instance of {@link DomainEventsInstantiator}.
     */
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }



}

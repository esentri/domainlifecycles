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

package io.domainlifecycles.events.spring.api;

import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.spring.publish.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

/**
 * The SpringTxInMemoryChannelFactory class extends the InMemoryChannelFactory class
 * and provides the capability to create channels for in-memory processing of domain events
 * with support for Spring Transactions.
 *
 * @author Mario Herb
 */
@Deprecated
public class SpringTxInMemoryChannelFactory extends InMemoryChannelFactory {

    private final PlatformTransactionManager transactionManager;
    private final boolean publishAfterCommit;

    /**
     * New SpringTxInMemoryChannelFactory.
     *
     * @param transactionManager The PlatformTransactionManager to be used for transaction management
     * @param serviceProvider The ServiceProvider used to retrieve instances of various types of services
     * @param executorThreads The number of executor threads to handle asynchronous processing
     * @param publishAfterCommit Flag indicating whether events should be published after transaction commit
     */
    public SpringTxInMemoryChannelFactory(PlatformTransactionManager transactionManager, ServiceProvider serviceProvider, int executorThreads, boolean publishAfterCommit) {
        super(serviceProvider, executorThreads);
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.publishAfterCommit = publishAfterCommit;
    }

    /**
     * This constructor creates a new instance of SpringTxInMemoryChannelFactory with the specified parameters.
     *
     * @param transactionManager The PlatformTransactionManager to be used for transaction management
     * @param serviceProvider The ServiceProvider used to retrieve instances of various types of services
     * @param publishAfterCommit Flag indicating whether events should be published after transaction commit
     */
    public SpringTxInMemoryChannelFactory(PlatformTransactionManager transactionManager, ServiceProvider serviceProvider, boolean publishAfterCommit) {
        this(transactionManager, serviceProvider, 0, publishAfterCommit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DomainEventPublisher useDomainEventPublisher(DomainEventConsumer domainEventConsumer) {
        return new DirectSpringTransactionalDomainEventPublisher(domainEventConsumer, publishAfterCommit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HandlerExecutor useHandlerExecutor() {
        return new SpringTransactionalHandlerExecutor(transactionManager);
    }
}

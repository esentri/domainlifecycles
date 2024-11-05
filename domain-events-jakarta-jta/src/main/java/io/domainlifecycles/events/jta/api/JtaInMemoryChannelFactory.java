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

package io.domainlifecycles.events.jta.api;

import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.jta.publish.DirectJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.jta.receive.execution.handler.JtaTransactionalHandlerExecutor;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.transaction.TransactionManager;

import java.util.Objects;

/**
 * The JtaInMemoryChannelFactory class extends InMemoryChannelFactory and provides
 * a channel factory implementation that supports JTA transaction management,
 * that means publishing Domain Events bound to a transaction and executing listeners
 * in new independent transactions.
 *
 * @author Mario Herb
 */
public class JtaInMemoryChannelFactory extends InMemoryChannelFactory {

    private final TransactionManager transactionManager;
    private final boolean publishAfterCommit;

    /**
     * Constructs a JtaInMemoryChannelFactory with the specified parameters.
     *
     * @param transactionManager The TransactionManager to be used for JTA transaction management.
     * @param serviceProvider The service provider used to retrieve instances of various types of services.
     * @param executorThreads The number of threads for asynchronous handling, 0 for synchronous handling.
     * @param publishAfterCommit Flag indicating whether to publish after committing a transaction.
     */
    public JtaInMemoryChannelFactory(TransactionManager transactionManager, ServiceProvider serviceProvider, int executorThreads, boolean publishAfterCommit) {
        super(serviceProvider, executorThreads);
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.publishAfterCommit = publishAfterCommit;
    }

    /**
     * Constructs a JtaInMemoryChannelFactory with the specified parameters.
     *
     * @param transactionManager The TransactionManager to be used for JTA transaction management.
     * @param serviceProvider The service provider used to retrieve instances of various types of services.
     * @param publishAfterCommit Flag indicating whether to publish after committing a transaction.
     */
    public JtaInMemoryChannelFactory(TransactionManager transactionManager, ServiceProvider serviceProvider, boolean publishAfterCommit) {
        this(transactionManager, serviceProvider, 0, publishAfterCommit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DomainEventPublisher useDomainEventPublisher(DomainEventConsumer domainEventConsumer) {
        return new DirectJtaTransactionalDomainEventPublisher(domainEventConsumer, transactionManager, publishAfterCommit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HandlerExecutor useHandlerExecutor() {
        return new JtaTransactionalHandlerExecutor(transactionManager);
    }
}

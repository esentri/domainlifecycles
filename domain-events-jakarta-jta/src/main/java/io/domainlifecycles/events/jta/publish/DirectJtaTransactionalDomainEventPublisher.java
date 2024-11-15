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

package io.domainlifecycles.events.jta.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.DomainEventConsumer;
import jakarta.transaction.TransactionManager;

import java.util.Collection;
import java.util.Objects;

/**
 * DirectJtaTransactionalDomainEventPublisher is a class that implements the DomainEventPublisher interface.
 * It is responsible for publishing domain events using an underlying event dispatcher and handling transactions.
 * It registers a synchronization with the transaction.
 * The synchronization will dispatch the domain event after or before the transaction is successfully committed (depending on <code>afterCommit</code>).
 *
 * This implementation provides only at-most-once delivery guarantees in case of <code>afterCommit</code> being <code>true</code>.
 * Events might still be lost.
 *
 * In case of <code>afterCommit</code> being <code>false</code>, "ghost events" might occur.
 * The transaction is rolled back, but the corresponding events are dispatched.
 *
 * To avoid "event loss" or "ghost events" in any case, consider
 * the outbox approach.
 *
 * @author Mario Herb
 */
public final class DirectJtaTransactionalDomainEventPublisher extends AbstractJtaTransactionalDomainEventPublisher {

    private final TransactionManager transactionManager;
    private final boolean afterCommit;

    /**
     * Initializes a new DirectJtaTransactionalDomainEventPublisher with the provided parameters.
     *
     * @param domainEventConsumer The DomainEventConsumer responsible for handling domain events
     * @param transactionManager The TransactionManager for managing transactions
     * @param afterCommit A boolean flag indicating if event dispatch should occur after transaction commit
     */
    public DirectJtaTransactionalDomainEventPublisher(
        DomainEventConsumer domainEventConsumer,
        TransactionManager transactionManager,
        boolean afterCommit
    ) {
        super(new DirectSender(
            Objects.requireNonNull(domainEventConsumer, "A DomainEventConsumer is required!")),
            transactionManager,
            afterCommit);

        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.afterCommit = afterCommit;
    }

    /**
     * This method is not intended to be called in this implementation.
     *
     * @param domainEvent the DomainEvent to be sent
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        throw new IllegalStateException("Should not be called in this implementation!");
    }

    private static class DirectSender implements JtaDomainEventSender{

        private final DomainEventConsumer domainEventConsumer;

        private DirectSender(DomainEventConsumer domainEventConsumer) {
            this.domainEventConsumer = domainEventConsumer;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void send(DomainEvent domainEvent) {
            this.domainEventConsumer.consume(domainEvent);
        }
    }

}

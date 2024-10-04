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

package io.domainlifecycles.events.spring.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.DomainEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * DirectSpringTransactionalDomainEventPublisher is a class that implements the DomainEventPublisher interface.
 * It is responsible for publishing domain events using an underlying event dispatcher and handling transactions.
 * If a transaction is active and the domain event is not a pass-through event, it registers a synchronization with the transaction.
 * The synchronization will dispatch the domain event after the transaction is successfully committed.
 * If there is no active transaction or the domain event is a pass-through event, the domain event is immediately dispatched.
 * <br>
 * This implementation provides only at-most-once delivery guarantees in case of <code>afterCommit</code> being <code>true</code>.
 *
 * In case of <code>afterCommit</code> being <code>false</code>, "ghost events" might occur.
 * The transaction is rolled back, but the corresponding events are dispatched.
 *
 * To avoid "event loss" or "ghost events" in any case, consider
 * the outbox approach.
 *
 * @author Mario Herb
 */
public final class DirectSpringTransactionalDomainEventPublisher extends AbstractSpringTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectSpringTransactionalDomainEventPublisher.class);

    private final DomainEventConsumer domainEventConsumer;

    /**
     * Initializes a DirectSpringTransactionalDomainEventPublisher with the specified DomainEventConsumer and afterCommit flag.
     * If afterCommit is true, the domain events will be dispatched after the transaction is successfully committed.
     *
     * @param domainEventConsumer The consumer responsible for handling received domain events
     * @param afterCommit Flag indicating whether to dispatch events after transaction commit
     */
    public DirectSpringTransactionalDomainEventPublisher(
        DomainEventConsumer domainEventConsumer,
        boolean afterCommit
    ) {
        super(afterCommit);
        this.domainEventConsumer = Objects.requireNonNull(domainEventConsumer, "A DomainEventConsumer is required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        this.domainEventConsumer.consume(domainEvent);
    }
}

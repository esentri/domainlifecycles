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

package io.domainlifecycles.events.publish.outbox;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.publish.outbox.api.TransactionalOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;

/**
 * This class is a Spring implementation of the DomainEventPublisher interface.
 * It is responsible for publishing domain events using a transactional outbox and a platform transaction manager.
 *
 * @author Mario Herb
 */
public class SpringOutboxDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringOutboxDomainEventPublisher.class);

    private final TransactionalOutbox transactionalOutbox;
    private final PlatformTransactionManager transactionManager;

    public SpringOutboxDomainEventPublisher(TransactionalOutbox transactionalOutbox,
                                            PlatformTransactionManager transactionManager) {
        this.transactionalOutbox = Objects.requireNonNull(transactionalOutbox,
            "SpringOutboxDomainEventPublisher needs a non null TransactionalOutbox!");
        this.transactionManager = Objects.requireNonNull(transactionManager,
            "SpringOutboxDomainEventPublisher needs a non null PlatformTransactionManager!");
    }

    /**
     * Publishes a domain event using a transactional outbox and a transaction manager.
     * If there is an active transaction, it inserts the domain event into the transactional outbox.
     * If there is no active transaction, it begins a new transaction, inserts the domain event into the outbox, and
     * commits the transaction.
     * If any exceptions occur during the transaction handling, it throws a DLCEventsException.
     *
     * @param domainEvent the domain event to be published
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            log.debug("Inserting DomainEvent {} into TransactionalOutbox", domainEvent);
            transactionalOutbox.insert(domainEvent);
        } else {
            try {
                DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
                definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                var status = transactionManager.getTransaction(definition);
                log.debug("Inserting DomainEvent {} into TransactionalOutbox", domainEvent);
                transactionalOutbox.insert(domainEvent);
                transactionManager.commit(status);
            } catch (Throwable t) {
                throw DLCEventsException.fail("Inserting DomainEvent into Outbox failed for %s", domainEvent, t);
            }
        }

    }
}

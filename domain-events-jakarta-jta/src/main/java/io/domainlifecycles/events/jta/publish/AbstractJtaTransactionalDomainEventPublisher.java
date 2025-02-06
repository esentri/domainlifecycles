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
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents an abstract class that extends AbstractTransactionalDomainEventPublisher and provides
 * JTA transactional event publishing capabilities.
 *
 * @author Mario Herb
 */
public abstract class AbstractJtaTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractJtaTransactionalDomainEventPublisher.class);

    private final JtaDomainEventSender sender;
    private final TransactionManager transactionManager;
    private final boolean afterCommit;

    /**
     * Creates a new AbstractJtaTransactionalDomainEventPublisher
     *
     * @param sender JtaDomainEventSender implementation responsible for sending domain events
     * @param transactionManager TransactionManager implementation for managing transactions
     * @param afterCommit Boolean flag indicating whether event dispatch should occur after transaction commit
     */
    public AbstractJtaTransactionalDomainEventPublisher(
        JtaDomainEventSender sender,
        TransactionManager transactionManager,
        boolean afterCommit
    ) {
        this.sender = Objects.requireNonNull(sender, "A JtaDomainEventSender is required!");
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.afterCommit = afterCommit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        try {
            final var transaction = transactionManager.getTransaction();
            if(transaction == null) {
                var msg = String.format("No transaction active, but active transaction is required! Event dispatching failed for %s", domainEvent);
                log.error(msg);
                throw DLCEventsException.fail(msg);
            }else{
                if(this.afterCommit){
                    transaction.registerSynchronization(new AfterCommitSynchronization(sender, domainEvent));
                }else{
                    transaction.registerSynchronization(new BeforeCommitSynchronization(sender, domainEvent));
                }
            }
        } catch (SystemException | RollbackException e) {
            throw DLCEventsException.fail("Couldn't get transaction! Event dispatching failed for %s", domainEvent, e);
        }
    }

}

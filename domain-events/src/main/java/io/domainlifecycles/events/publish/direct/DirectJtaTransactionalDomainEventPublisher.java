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

package io.domainlifecycles.events.publish.direct;

import io.domainlifecycles.events.publish.outbox.JtaOutboxDomainEventPublisher;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * DirectJtaTransactionalDomainEventPublisher is a class that implements the DomainEventPublisher interface.
 * It is responsible for publishing domain events using an underlying event dispatcher and handling transactions.
 * If a transaction is active and the domain event is not a pass-through event, it registers a synchronization with the transaction.
 * The synchronization will dispatch the domain event after the transaction is successfully committed.
 * If there is no active transaction or the domain event is a pass-through event, the domain event is immediately dispatched.
 * <br>
 * This implementation provides only at-most-once delivery guarantees. To avoid "event loss" in any case, consider
 * the outbox approach {@link JtaOutboxDomainEventPublisher}.
 *
 * @author Mario Herb
 */
public class DirectJtaTransactionalDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectJtaTransactionalDomainEventPublisher.class);

    private final ReceivingDomainEventHandler receivingDomainEventHandler;
    private final TransactionManager transactionManager;
    private Collection<Class<? extends DomainEvent>> passThroughEventTypes;

    public DirectJtaTransactionalDomainEventPublisher(
        ReceivingDomainEventHandler receivingDomainEventHandler,
        TransactionManager transactionManager
    ) {
        this.receivingDomainEventHandler = receivingDomainEventHandler;
        this.transactionManager = transactionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        try {
            final var transaction = transactionManager.getTransaction();

            if(transaction == null || (passThroughEventTypes != null && passThroughEventTypes.contains(domainEvent.getClass()))) {
                log.debug("Passing DomainEvent {} to DomainEventDispatcher passing through to ReceivingDomainEventHandler directly", domainEvent);
                receivingDomainEventHandler.handleReceived(domainEvent);
            }else{
                transaction.registerSynchronization(new Synchronization() {
                    @Override
                    public void beforeCompletion() {
                    }

                    @Override
                    public void afterCompletion(int i) {
                        if (Status.STATUS_COMMITTED == i) {
                            log.debug("Publisher transaction committed. Passing DomainEvent {} to ReceivingDomainEventHandler!", domainEvent);
                            receivingDomainEventHandler.handleReceived(domainEvent);
                        }else{
                            log.debug("DomainEvent {} not dispatched. Transaction not committed!", domainEvent);
                        }
                    }
                });
            }
        } catch (SystemException | RollbackException e) {
            throw DLCEventsException.fail("Couldn't get transaction! Event dispatching failed for %s", domainEvent, e);
        }
    }

    /**
     * Sets the pass-through event types.
     * Pass-through event types are the types of events that should bypass the transaction handling and be immediately dispatched.
     * If a transaction is active and the domain event is not a pass-through event, it will register a synchronization with the transaction
     * to dispatch the event after the transaction is successfully committed.
     *
     * @param passThroughEventTypes the collection of pass-through event types
     */
    public void setPassThroughEventTypes(Collection<Class<? extends DomainEvent>> passThroughEventTypes){
        this.passThroughEventTypes = passThroughEventTypes;
    }



}

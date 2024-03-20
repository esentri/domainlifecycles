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

package nitrox.dlc.events.publish.direct;

import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.events.publish.DomainEventPublisher;
import nitrox.dlc.events.receive.ReceivingDomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
/**
 * DirectSpringTransactionalDomainEventPublisher is a class that implements the DomainEventPublisher interface.
 * It is responsible for publishing domain events using an underlying event dispatcher and handling transactions.
 * If a transaction is active and the domain event is not a pass-through event, it registers a synchronization with the transaction.
 * The synchronization will dispatch the domain event after the transaction is successfully committed.
 * If there is no active transaction or the domain event is a pass-through event, the domain event is immediately dispatched.
 * <br>
 * This implementation provides only at-most-once delivery guarantees. To avoid "event loss" in any case, consider
 * the outbox approach {@link nitrox.dlc.events.publish.outbox.SpringOutboxDomainEventPublisher}.
 *
 * @author Mario Herb
 */
public class DirectSpringTransactionalDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectSpringTransactionalDomainEventPublisher.class);

    private final ReceivingDomainEventHandler receivingDomainEventHandler;
    private Collection<Class<? extends DomainEvent>> passThroughEventTypes;

    public DirectSpringTransactionalDomainEventPublisher(
        ReceivingDomainEventHandler receivingDomainEventHandler
    ) {
        this.receivingDomainEventHandler = receivingDomainEventHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        if(!TransactionSynchronizationManager.isActualTransactionActive() || (passThroughEventTypes != null && passThroughEventTypes.contains(domainEvent.getClass()))) {
            log.debug("Passing DomainEvent {} to DomainEventDispatcher passing through to ReceivingDomainEventHandler directly", domainEvent);
            receivingDomainEventHandler.handleReceived(domainEvent);
        }else{
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        log.debug("Publisher transaction committed. Passing DomainEvent {} to ReceivingDomainEventHandler!", domainEvent);
                        receivingDomainEventHandler.handleReceived(domainEvent);
                    }
                }
            );
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

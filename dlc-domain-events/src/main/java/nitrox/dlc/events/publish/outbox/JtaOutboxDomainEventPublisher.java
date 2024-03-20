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

package nitrox.dlc.events.publish.outbox;

import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;
import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.events.exception.DLCEventsException;
import nitrox.dlc.events.publish.DomainEventPublisher;
import nitrox.dlc.events.publish.direct.DirectJtaTransactionalDomainEventPublisher;
import nitrox.dlc.events.publish.outbox.api.TransactionalOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * JtaOutboxDomainEventPublisher is a class that implements the DomainEventPublisher interface.
 * It is responsible for publishing domain events using a transactional outbox and a transaction manager.
 *
 * @see DomainEventPublisher
 * @see TransactionalOutbox
 * @see TransactionManager
 * @see DLCEventsException
 * @author Mario Herb
 */
public class JtaOutboxDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectJtaTransactionalDomainEventPublisher.class);

    private final TransactionManager transactionManager;
    private final TransactionalOutbox transactionalOutbox;

    public JtaOutboxDomainEventPublisher(TransactionalOutbox transactionalOutbox, TransactionManager transactionManager) {
        this.transactionalOutbox = Objects.requireNonNull(transactionalOutbox, "JtaOutboxDomainEventPublisher needs a non null TransactionalOutbox!");
        this.transactionManager =  Objects.requireNonNull(transactionManager, "JtaOutboxDomainEventPublisher needs a non null TransactionManager!");
    }

    /**
     * Publishes a domain event using a transactional outbox and a transaction manager.
     * If there is an active transaction, it inserts the domain event into the transactional outbox.
     * If there is no active transaction, it begins a new transaction, inserts the domain event into the outbox, and commits the transaction.
     * If any exceptions occur during the transaction handling, it throws a DLCEventsException.
     *
     * @param domainEvent the domain event to be published
     * @throws DLCEventsException if inserting the domain event into the outbox fails or there is an error accessing the transaction manager
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        Transaction transaction = null;
        try {
            transaction = transactionManager.getTransaction();
        } catch (SystemException e) {
            throw DLCEventsException.fail("Couldn't access transaction manager! Inserting DomainEvent into Outbox failed for %s", domainEvent, e);
        }
        if(transaction == null) {
            try{
                transactionManager.begin();
                transactionalOutbox.insert(domainEvent);
                transactionManager.commit();
            } catch (SystemException
                     | RollbackException
                     | NotSupportedException
                     | HeuristicRollbackException
                     | HeuristicMixedException e){
                throw DLCEventsException.fail("Inserting DomainEvent into Outbox failed for %s", domainEvent, e);
            }
        }else{
            transactionalOutbox.insert(domainEvent);
        }
        log.debug("Inserting DomainEvent {} into TransactionalOutbox", domainEvent);
    }
}

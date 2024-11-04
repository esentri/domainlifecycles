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
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;



public abstract class AbstractSpringTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractSpringTransactionalDomainEventPublisher.class);
    private final boolean afterCommit;

    public AbstractSpringTransactionalDomainEventPublisher(
        boolean afterCommit
    ) {
        this.afterCommit = afterCommit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        if(!TransactionSynchronizationManager.isActualTransactionActive()) {
            var msg = String.format("No transaction active, but active transaction is required! Event dispatching failed for %s", domainEvent);
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }else{
            if(afterCommit){
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            log.debug("Publisher transaction committed. Passing DomainEvent {}!", domainEvent);
                            send(domainEvent);
                        }
                    }
                );
            }else{
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void beforeCommit(boolean readOnly) {
                            log.debug("Publisher transaction about to commit. Passing DomainEvent {} to DomainEventConsumer!", domainEvent);
                            send(domainEvent);
                        }
                    }
                );
            }
        }
    }
}

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

package io.domainlifecycles.events.jta.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Synchronization callback to publish events before commit.
 *
 * @author Mario Herb
 */
final class BeforeCommitSynchronization implements Synchronization {

    private static final Logger log = LoggerFactory.getLogger(BeforeCommitSynchronization.class);
    private final ReceivingDomainEventHandler receivingDomainEventHandler;
    private final DomainEvent publishedDomainEvent;

    public BeforeCommitSynchronization(ReceivingDomainEventHandler receivingDomainEventHandler, DomainEvent publishedDomainEvent) {
        this.receivingDomainEventHandler = Objects.requireNonNull(receivingDomainEventHandler, "A ReceivingDomainEventHandler is required!");
        this.publishedDomainEvent = Objects.requireNonNull(publishedDomainEvent, "A DomainEvent is required!");
    }


    @Override
    public void beforeCompletion() {
        log.debug("Publisher transaction about to complete. Passing DomainEvent {} to ReceivingDomainEventHandler!", publishedDomainEvent);
        receivingDomainEventHandler.handleReceived(publishedDomainEvent);
    }

    @Override
    public void afterCompletion(int i) {

    }
}

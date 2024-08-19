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

package io.domainlifecycles.events.spring.outbox.api;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.List;
import java.util.UUID;

/**
 * Represents a batch of domain events in the outbox.
 *
 * The OutboxBatch class is responsible for storing a batch of domain events to be sent out.
 * Each OutboxBatch instance is associated with a unique batch ID generated using UUID.randomUUID().
 * It contains a list of domain events that are to be sent out within the batch.
 *
 * The OutboxBatch class provides a getter method for retrieving the batch ID.
 * The class does not provide any setter methods to modify the batch ID or the list of domain events.
 *
 * @author Mario Herb
 */
public class OutboxBatch {

    final UUID batchId = UUID.randomUUID();
    List<DomainEvent> domainEvents;

    /**
     * Constructs an OutboxBatch object with the provided list of domain events.
     *
     * @param domainEvents the list of domain events to be stored in the OutboxBatch
     */
    public OutboxBatch(List<DomainEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }

    /**
     * Retrieves the batch ID associated with the OutboxBatch.
     *
     * The batch ID is a unique identifier generated using UUID.randomUUID().
     *
     * @return the batch ID
     */
    public UUID getBatchId() {
        return batchId;
    }

    /**
     * Retrieves the list of domain events associated with the OutboxBatch.
     *
     * @return the list of domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
}

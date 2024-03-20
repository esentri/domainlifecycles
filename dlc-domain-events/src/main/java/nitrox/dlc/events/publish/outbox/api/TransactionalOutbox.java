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

package nitrox.dlc.events.publish.outbox.api;

import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.events.publish.outbox.api.OutboxBatch;
import nitrox.dlc.events.publish.outbox.api.ProcessingResult;

/**
 * Represents a transactional outbox for storing domain events before sending them.
 *
 * @author Mario Herb
 */
public interface TransactionalOutbox {

    void insert(DomainEvent domainEvent);

    /**
     * Retrieves a batch of domain events from the outbox for sending.
     *
     * The implementor must make sure the events are delivered in the order they were sent.
     * Also the implementor should take care, that no duplicates are sent by concurrent sender processes.
     * That means as long as a batch is processed, no other batches could be fetched. If a batch
     * takes too long and not successful batch acknowledgement was received it must be given free to be reprocessed.
     *
     * @param batchSize the number of domain events to retrieve in a batch
     * @return an {@code OutboxBatch} containing the retrieved domain events
     */
    OutboxBatch fetchBatchForSending(int batchSize);

    /**
     * Sends a batch of domain events successfully.
     *
     * @param batch the {@code OutboxBatch} containing the domain events to be sent.
     */
    void sentSuccessfully(OutboxBatch batch);

    /**
     * Marks a domain event as failed.
     *
     * @param domainEvent the domain event to mark as failed
     */
    void markFailed(DomainEvent domainEvent, ProcessingResult result);

}

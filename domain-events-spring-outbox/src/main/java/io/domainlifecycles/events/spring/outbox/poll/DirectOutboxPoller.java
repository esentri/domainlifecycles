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

package io.domainlifecycles.events.spring.outbox.poll;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.spring.outbox.api.ProcessingResult;
import io.domainlifecycles.events.spring.outbox.api.TransactionalOutbox;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.processor.ExecutionResult;

import java.util.Objects;

/**
 * The DirectOutboxPoller class is a subclass of AbstractOutboxPoller which sends polled
 * domain events directly to a receiving domain event handler.
 *
 * Example usage:
 *
 * TransactionalOutbox transactionalOutbox = new TransactionalOutboxImpl();
 * ReceivingDomainEventHandler receivingDomainEventHandler = new ReceivingDomainEventHandlerImpl();
 * DirectOutboxPoller outboxPoller = new DirectOutboxPoller(transactionalOutbox, receivingDomainEventHandler);
 * outboxPoller.setDelay(5000);
 * outboxPoller.setPeriod(1000);
 * outboxPoller.setMaxBatchSize(10);
 *
 * @author Mario Herb
 */
public class DirectOutboxPoller extends AbstractOutboxPoller {

    private final ReceivingDomainEventHandler receivingDomainEventHandler;

    public DirectOutboxPoller(TransactionalOutbox transactionalOutbox, ReceivingDomainEventHandler receivingDomainEventHandler) {
        super(transactionalOutbox);
        this.receivingDomainEventHandler = Objects.requireNonNull(receivingDomainEventHandler, "A ReceivingDomainEventHandler is required!");
    }

    /**
     * Sends a domain event to a receiving domain event handler.
     *
     * @param domainEvent the domain event to be sent
     * @return the processing result of sending the domain event
     */
    @Override
    protected ProcessingResult send(DomainEvent domainEvent) {
        var results = receivingDomainEventHandler.handleReceived(domainEvent);
        if(results.stream().allMatch(ExecutionResult::success)){
            return ProcessingResult.OK;
        }else if(results.stream().noneMatch(ExecutionResult::success)){
            return ProcessingResult.FAILED;
        }
        return ProcessingResult.FAILED_PARTIALLY;
    }
}

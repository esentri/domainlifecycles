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

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DirectPassThroughDomainEventPublisher class is an implementation of the DomainEventPublisher interface.
 * It simply delegates the publishing of a domain event to a ReceivingDomainEventHandler instance.
 * <br>
 * Be aware that message loss is possible.
 *
 * @author Mario Herb
 */
public class DirectPassThroughDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DirectPassThroughDomainEventPublisher.class);

    private final ReceivingDomainEventHandler receivingDomainEventHandler;

    /**
     * The DirectPassThroughDomainEventPublisher class is an implementation of the DomainEventPublisher interface.
     * It simply delegates the publishing of a domain event to a ReceivingDomainEventHandler instance.
     */
    public DirectPassThroughDomainEventPublisher(ReceivingDomainEventHandler receivingDomainEventHandler) {
        this.receivingDomainEventHandler = receivingDomainEventHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Passing DomainEvent {} through to ReceivingDomainEventHandler directly", domainEvent);
        receivingDomainEventHandler.handleReceived(domainEvent);
    }
}

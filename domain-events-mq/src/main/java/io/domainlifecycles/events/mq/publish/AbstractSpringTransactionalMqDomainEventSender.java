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

package io.domainlifecycles.events.mq.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.spring.publish.AbstractSpringTransactionalDomainEventPublisher;

/**
 * Represents an abstract class for sending domain events to a message queue with a sending process bound to Spring transaction phases.
 * Extends AbstractSpringTransactionalDomainEventPublisher.
 *
 * @param <TOPIC> the type of topic used in the message queue
 *
 * @author Mario Herb
 */
public abstract class AbstractSpringTransactionalMqDomainEventSender<TOPIC> extends AbstractSpringTransactionalDomainEventPublisher {

    private final AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher;

    /**
     * Constructs an AbstractSpringTransactionalMqDomainEventSender with the specified parameters.
     *
     * @param afterCommit a boolean flag indicating whether events should be published after transaction commit
     * @param abstractMqDomainEventPublisher the AbstractMqDomainEventPublisher instance to delegate event publishing to
     */
    public AbstractSpringTransactionalMqDomainEventSender(
        boolean afterCommit,
        AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher
    ) {
        super(afterCommit);
        this.abstractMqDomainEventPublisher = abstractMqDomainEventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        abstractMqDomainEventPublisher.publish(domainEvent);
    }
}

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

package io.domainlifecycles.events.jakarta.jms.publish;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.mq.publish.AbstractSpringTransactionalMqDomainEventSender;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import jakarta.jms.Topic;

/**
 * Represents a SpringTransactionalJakartaJmsDomainEventPublisher that extends AbstractSpringTransactionalMqDomainEventSender
 * and implements the MqDomainEventPublisher interface. This class is responsible for publishing DomainEvent messages
 * to a JMS Topic using Jakarta Messaging API. It uses a JakartaJmsDomainEventPublisher instance to handle the JMS actions.
 *
 * When instantiated, it initializes the JakartaJmsDomainEventPublisher with the specified afterCommit flag and the provided
 * JakartaJmsDomainEventPublisher instance.
 *
 * It overrides the send method to publish a DomainEvent using the JakartaJmsDomainEventPublisher.
 * It implements the closeAll method to close the connection and session of the internal JakartaJmsDomainEventPublisher.
 *
 * @see AbstractSpringTransactionalMqDomainEventSender
 * @see MqDomainEventPublisher
 *
 * @author Mario Herb
 */
public class SpringTransactionalJakartaJmsDomainEventPublisher extends AbstractSpringTransactionalMqDomainEventSender<Topic> implements MqDomainEventPublisher {

    private final JakartaJmsDomainEventPublisher jakartaJmsDomainEventPublisher;

    /**
     * Initializes a new instance of SpringTransactionalJakartaJmsDomainEventPublisher.
     *
     * @param afterCommit A flag indicating whether to publish events after a transaction commit.
     * @param jakartaJmsDomainEventPublisher The Jakarta JMS Domain Event Publisher instance to use.
     */
    public SpringTransactionalJakartaJmsDomainEventPublisher(boolean afterCommit, JakartaJmsDomainEventPublisher jakartaJmsDomainEventPublisher) {
        super(afterCommit, jakartaJmsDomainEventPublisher);
        this.jakartaJmsDomainEventPublisher = jakartaJmsDomainEventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        jakartaJmsDomainEventPublisher.publish(domainEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeAll() {
        jakartaJmsDomainEventPublisher.closeAll();
    }
}

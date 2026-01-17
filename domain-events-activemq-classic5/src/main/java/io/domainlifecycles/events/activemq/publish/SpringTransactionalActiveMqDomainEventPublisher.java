/*
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

package io.domainlifecycles.events.activemq.publish;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.mq.publish.AbstractSpringTransactionalMqDomainEventSender;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import jakarta.jms.Topic;

/**
 * Represents a domain event publisher that publishes domain events to ActiveMQ virtual topics in a Spring transactional manner.
 *
 * @author Mario Herb
 */
public class SpringTransactionalActiveMqDomainEventPublisher extends AbstractSpringTransactionalMqDomainEventSender<Topic> implements MqDomainEventPublisher {

    private final ActiveMqDomainEventPublisher activeMqDomainEventPublisher;

    /**
     * Constructs a SpringTransactionalActiveMqDomainEventPublisher with the given parameters.
     *
     * @param afterCommit boolean flag indicating whether events should be published after transaction commit
     * @param activeMqDomainEventPublisher the ActiveMqDomainEventPublisher to delegate event publishing to
     */
    public SpringTransactionalActiveMqDomainEventPublisher(boolean afterCommit, ActiveMqDomainEventPublisher activeMqDomainEventPublisher) {
        super(afterCommit, activeMqDomainEventPublisher);
        this.activeMqDomainEventPublisher = activeMqDomainEventPublisher;
    }

    /**
     * Sends a domain event using ActiveMqDomainEventPublisher.
     *
     * @param domainEvent the DomainEvent to be sent
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        activeMqDomainEventPublisher.publish(domainEvent);
    }

    /**
     * Closes the connection and session used for the ActiveMQ publisher, if they are not already closed.
     * Any exceptions that occur during the closing process are logged.
     */
    @Override
    public void closeAll() {
        activeMqDomainEventPublisher.closeAll();
    }
}

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
import io.domainlifecycles.events.jta.publish.AbstractJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.jta.publish.JtaDomainEventSender;
import jakarta.transaction.TransactionManager;

import java.util.Objects;

/**
 * Represents an abstract class that extends AbstractJtaTransactionalDomainEventPublisher and provides
 * message queue (MQ) domain event sending capabilities with a sending process bound to JTA transaction phases.
 *
 * This class is designed to work with a specific type of message queue represented by the generic type TOPIC.
 *
 * @param <TOPIC> the type the topic,that events are sent to
 *
 * @author Mario Herb
 */
public abstract class AbstractJtaTransactionalMqDomainEventSender<TOPIC> extends AbstractJtaTransactionalDomainEventPublisher {

    private final AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher;

    /**
     * Initializes a new instance of AbstractJtaTransactionalMqDomainEventSender.
     *
     * @param abstractMqDomainEventPublisher The AbstractMqDomainEventPublisher implementation responsible for publishing domain events
     * @param transactionManager The TransactionManager implementation for managing transactions
     * @param afterCommit Boolean flag indicating whether event dispatch should occur after transaction commit
     */
    public AbstractJtaTransactionalMqDomainEventSender(
        AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher,
        TransactionManager transactionManager,
        boolean afterCommit
    ) {
        super(new MqJtaDomainEventSender(abstractMqDomainEventPublisher), transactionManager, afterCommit);
        this.abstractMqDomainEventPublisher = abstractMqDomainEventPublisher;
    }

    /**
     * {@inheritDo}
     */
    @Override
    protected void send(DomainEvent domainEvent) {
        abstractMqDomainEventPublisher.publish(domainEvent);
    }

    private static class MqJtaDomainEventSender implements JtaDomainEventSender{

        private final AbstractMqDomainEventPublisher abstractMqDomainEventPublisher;

        private MqJtaDomainEventSender(AbstractMqDomainEventPublisher abstractMqDomainEventPublisher) {
            this.abstractMqDomainEventPublisher = Objects.requireNonNull(abstractMqDomainEventPublisher, "AbstractMqDomainEventPublisher is required!");
        }

        /**
         * {@inheritDo}
         */
        @Override
        public void send(DomainEvent domainEvent) {
            abstractMqDomainEventPublisher.publish(domainEvent);
        }
    }

}

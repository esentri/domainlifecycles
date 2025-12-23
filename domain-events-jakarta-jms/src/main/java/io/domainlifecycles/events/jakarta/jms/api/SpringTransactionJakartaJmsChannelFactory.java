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

package io.domainlifecycles.events.jakarta.jms.api;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.jakarta.jms.publish.JakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.jakarta.jms.publish.SpringTransactionalJakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

/**
 * The SpringTransactionJakartaJmsChannelFactory class extends JakartaJmsChannelFactory and provides additional
 * functionality for publishing Domain Events processed via Jakarta JMS compliant message brokers
 * bound the transaction managed by a Spring Transaction manager.
 *
 * @author Mario Herb
 */
public class SpringTransactionJakartaJmsChannelFactory extends JakartaJmsChannelFactory{

    /**
     * Creates a new SpringTransactionJakartaJmsChannelFactory
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to the message broker
     * @param domainEventSerializer The DomainEventSerializer instance
     */
    public SpringTransactionJakartaJmsChannelFactory(
        ConnectionFactory connectionFactory,
        DomainEventSerializer domainEventSerializer
    ) {
        super(
                connectionFactory,
                null,
                null,
                null,
                domainEventSerializer
        );
    }

    /**
     * Initializes a new SpringTransactionJakartaJmsChannelFactory with the provided parameters.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to the message broker
     * @param serviceProvider The ServiceProvider instance for providing various types of services
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names
     * @param handlerExecutor The HandlerExecutor instance for executing domain event listeners
     * @param domainEventSerializer The DomainEventSerializer instance for serialization and deserialization
     */
    public SpringTransactionJakartaJmsChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            domainEventSerializer
        );
    }

    /**
     * Creates a new SpringTransactionalJakartaJmsDomainEventPublisher which adds the transaction bound behaviour for
     * publishing Domain Events.
     *
     * @param domainEventSerializer The DomainEventSerializer instance to be used for serialization and deserialization
     * @return a new instance of SpringTransactionalJakartaJmsDomainEventPublisher with the provided DomainEventSerializer
     */
    @Override
    protected SpringTransactionalJakartaJmsDomainEventPublisher provideMqDomainEventPublisher(DomainEventSerializer domainEventSerializer) {
        var jakartaJmsDomainEventPublisher = (JakartaJmsDomainEventPublisher) super.provideMqDomainEventPublisher(domainEventSerializer);
        return new SpringTransactionalJakartaJmsDomainEventPublisher(true, jakartaJmsDomainEventPublisher);
    }
}

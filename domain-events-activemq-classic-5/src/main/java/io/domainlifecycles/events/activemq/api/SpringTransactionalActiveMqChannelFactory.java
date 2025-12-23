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

package io.domainlifecycles.events.activemq.api;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.activemq.publish.SpringTransactionalActiveMqDomainEventPublisher;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

/**
 * A factory for creating ActiveMQ Classic 5 channels to publish and consume domain events with Spring transactional support.
 * Extends ActiveMqChannelFactory for providing consuming and publishing configurations.
 *
 * This class provides constructors for initializing the factory with different combinations of parameters.
 * It overrides the provideMqDomainEventPublisher method to return a SpringTransactionalActiveMqDomainEventPublisher instance.
 *
 * So the sending of Domain Events is bound to a transactions phase and happens after commit.
 * Be aware that events might be lost on rare occasions.
 *
 * @author Mario Herb
 */
public class SpringTransactionalActiveMqChannelFactory extends ActiveMqChannelFactory{

    /**
     * Constructor for creating a SpringTransactionalActiveMqChannelFactory.
     * Initializes the factory with the provided ConnectionFactory and ObjectMapper.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to ActiveMQ.
     * @param domainEventSerializer serialization and deserialization of domain events.
     */
    public SpringTransactionalActiveMqChannelFactory(
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
     * Constructor for creating a SpringTransactionalActiveMqChannelFactory.
     * Initializes the factory with the provided parameters.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to ActiveMQ.
     * @param serviceProvider The ServiceProvider responsible for providing instances of various services.
     * @param classProvider The ClassProvider for providing Class instances based on class names.
     * @param handlerExecutor The HandlerExecutor for executing domain event listeners.
     * @param domainEventSerializer for serialization and deserialization of domain events.
     */
    public SpringTransactionalActiveMqChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer
    ) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            domainEventSerializer
        );
    }

    /**
     * Provides a new MqDomainEventPublisher instance with transactional support for ActiveMQ implementation.
     *
     * @param domainEventSerializer for serialization and deserialization of domain events.
     * @return A new SpringTransactionalActiveMqDomainEventPublisher instance created by wrapping the provided ActiveMqDomainEventPublisher.
     */
    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(DomainEventSerializer domainEventSerializer) {
        var activeMqDomainEventPublisher = (ActiveMqDomainEventPublisher) super.provideMqDomainEventPublisher(domainEventSerializer);
        return new SpringTransactionalActiveMqDomainEventPublisher(true, activeMqDomainEventPublisher);
    }
}

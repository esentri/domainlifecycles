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
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.jakarta.jms.consume.JakartaJmsDomainEventConsumer;
import io.domainlifecycles.events.jakarta.jms.publish.JakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.mq.api.AbstractMqChannelFactory;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

/**
 * The JakartaJmsChannelFactory class extends AbstractMqChannelFactory and provides methods for creating channels
 * to handle Domain Events processed via Jakarta JMS compliant message brokers.
 *
 * @author Mario Herb
 */
public class JakartaJmsChannelFactory extends AbstractMqChannelFactory {

    private final ConnectionFactory connectionFactory;
    private long receiveTimeoutMs = 100;

    /**
     * Constructs a JakartaJmsChannelFactory with the provided ConnectionFactory and ObjectMapper.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to the message broker
     * @param domainEventSerializer to serialize/deserialize messages
     */
    public JakartaJmsChannelFactory(ConnectionFactory connectionFactory,
                                    DomainEventSerializer domainEventSerializer){
        super(null, null, null, domainEventSerializer);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Constructs a JakartaJmsChannelFactory with the provided parameters.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to the message broker
     * @param serviceProvider The ServiceProvider instance to provide various types of services
     * @param classProvider The ClassProvider instance to provide Class instances for full qualified class names
     * @param handlerExecutor The HandlerExecutor instance to execute domain event listeners
     * @param domainEventSerializer The DomainEventSerializer instance
     */
    public JakartaJmsChannelFactory(ConnectionFactory connectionFactory,
                                    ServiceProvider serviceProvider,
                                    ClassProvider classProvider,
                                    HandlerExecutor handlerExecutor,
                                    DomainEventSerializer domainEventSerializer){
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(DomainEventSerializer domainEventSerializer) {
         return new JakartaJmsDomainEventPublisher(connectionFactory, domainEventSerializer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MqDomainEventConsumer provideMqDomainEventConsumer(DomainEventSerializer domainEventSerializer, ExecutionContextDetector executionContextDetector, ExecutionContextProcessor executionContextProcessor, ClassProvider classProvider) {
        return new JakartaJmsDomainEventConsumer(
            connectionFactory,
            domainEventSerializer,
            executionContextDetector,
            executionContextProcessor,
            classProvider,
            this.receiveTimeoutMs
        );
    }

    /**
     * Retrieves the receive timeout value in milliseconds.
     *
     * @return The receive timeout value in milliseconds
     */
    public long getReceiveTimeoutMs() {
        return receiveTimeoutMs;
    }

    /**
     * Sets the receive timeout value in milliseconds.
     *
     * @param receiveTimeoutMs The receive timeout value to be set in milliseconds
     */
    public void setReceiveTimeoutMs(long receiveTimeoutMs) {
        this.receiveTimeoutMs = receiveTimeoutMs;
    }
}

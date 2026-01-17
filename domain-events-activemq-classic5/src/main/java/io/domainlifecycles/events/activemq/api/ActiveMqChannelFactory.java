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
import io.domainlifecycles.events.activemq.consume.ActiveMqDomainEventConsumer;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.mq.api.AbstractMqChannelFactory;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

/**
 * A factory for creating ActiveMQ Classic 5 channels to publish and consume domain events.
 * Extends AbstractMqChannelFactory for providing consuming and publishing configurations.
 *
 * Each DomainEvent type is sent to a dedicated 'virtual topic'. Service instances which provide handler methods
 * with the exact same signature of listener methods form a consumer group.
 *
 * With this factory the sending process is not bound to a transaction phase.
 *
 * @author Mario Herb
 */
public class ActiveMqChannelFactory extends AbstractMqChannelFactory {

    private String virtualTopicPrefix = "VirtualTopic.";
    private String virtualTopicConsumerPrefix = "Consumer.";

    private final ConnectionFactory connectionFactory;
    private long receiveTimeoutMs = 100;

    /**
     * Constructs a new ActiveMqChannelFactory with the provided ConnectionFactory and ObjectMapper.
     * This constructor should only be used for publish only use cases.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to ActiveMQ.
     * @param domainEventSerializer serialization and deserialization of domain events.
     */
    public ActiveMqChannelFactory(ConnectionFactory connectionFactory,
                                  DomainEventSerializer domainEventSerializer){
        super(null, null, null, domainEventSerializer);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Creates a new ActiveMqChannelFactory with the provided parameters.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to ActiveMQ.
     * @param serviceProvider The ServiceProvider responsible for providing instances of various services.
     * @param classProvider The ClassProvider for providing Class instances based on class names.
     * @param handlerExecutor The HandlerExecutor for executing domain event listeners.
     * @param domainEventSerializer for serialization and deserialization of domain events.
     */
    public ActiveMqChannelFactory(ConnectionFactory connectionFactory,
                                  ServiceProvider serviceProvider,
                                  ClassProvider classProvider,
                                  HandlerExecutor handlerExecutor,
                                  DomainEventSerializer domainEventSerializer){
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Provides a new MqDomainEventPublisher instance for ActiveMQ implementation.
     *
     * @param domainEventSerializer for serialization and deserialization of objects.
     * @return A new ActiveMqDomainEventPublisher instance created with the provided ConnectionFactory, ObjectMapper, and virtualTopicPrefix.
     */
    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(DomainEventSerializer domainEventSerializer) {
        return new ActiveMqDomainEventPublisher(connectionFactory, domainEventSerializer, virtualTopicPrefix);
    }

    /**
     * Provides an ActiveMqDomainEventConsumer instance with the given parameters.
     *
     * @param domainEventSerializer serialization and deserialization of domain events.
     * @param executionContextDetector The ExecutionContextDetector for detecting execution contexts.
     * @param executionContextProcessor The ExecutionContextProcessor for processing execution contexts.
     * @param classProvider The ClassProvider for providing Class instances.
     * @return an instance of ActiveMqDomainEventConsumer initialized with the provided parameters
     */
    @Override
    protected ActiveMqDomainEventConsumer provideMqDomainEventConsumer(
        DomainEventSerializer domainEventSerializer,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
    ) {
        return new ActiveMqDomainEventConsumer(
            connectionFactory,
            domainEventSerializer,
            executionContextDetector,
            executionContextProcessor,
            classProvider,
            this.virtualTopicConsumerPrefix,
            this.virtualTopicPrefix,
            this.receiveTimeoutMs
        );
    }

    /**
     * Retrieves the prefix for virtual topics.
     *
     * @return The prefix for virtual topics.
     */
    public String getVirtualTopicPrefix() {
        return virtualTopicPrefix;
    }

    /**
     * Retrieves the prefix for virtual topic consumers.
     *
     * @return The prefix for virtual topic consumers.
     */
    public String getVirtualTopicConsumerPrefix() {
        return virtualTopicConsumerPrefix;
    }

    /**
     * Sets the prefix for virtual topics.
     *
     * @param virtualTopicPrefix The prefix to be set for virtual topics.
     */
    public void setVirtualTopicPrefix(String virtualTopicPrefix) {
        this.virtualTopicPrefix = virtualTopicPrefix;
    }

    /**
     * Sets the prefix for virtual topic consumers.
     *
     * @param virtualTopicConsumerPrefix The prefix to be set for virtual topic consumers.
     */
    public void setVirtualTopicConsumerPrefix(String virtualTopicConsumerPrefix) {
        this.virtualTopicConsumerPrefix = virtualTopicConsumerPrefix;
    }

    /**
     * Sets the receive timeout value in milliseconds for receiving messages.
     *
     * @param receiveTimeoutMs The timeout value in milliseconds for receiving messages.
     */
    public void setReceiveTimeoutMs(long receiveTimeoutMs) {
        this.receiveTimeoutMs = receiveTimeoutMs;
    }
}

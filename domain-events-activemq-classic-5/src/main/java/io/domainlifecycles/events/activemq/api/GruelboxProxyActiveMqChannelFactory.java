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

package io.domainlifecycles.events.activemq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.activemq.consume.ActiveMqDomainEventConsumer;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.mq.api.AbstractGruelboxProxyMqChannelFactory;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

/**
 * GruelboxProxyActiveMqChannelFactory is a class that extends AbstractGruelboxProxyMqChannelFactory and provides
 * functionality for creating ActiveMq channel factories for messaging that have a Gruelbox outbox as a proxy in place.
 *
 * The outbox avoids Domain Event loss or ghost events when sending them to an Active MQ message broker.
 *
 * @author Mario Herb
 */
public class GruelboxProxyActiveMqChannelFactory extends AbstractGruelboxProxyMqChannelFactory {

    private final ConnectionFactory connectionFactory;
    private long receiveTimeoutMs = 100;
    private String virtualTopicPrefix = "VirtualTopic.";
    private String virtualTopicConsumerPrefix = "Consumer.";

    /**
     * Initializes a GruelboxProxyActiveMqChannelFactory with the provided dependencies.
     *
     * @param serviceProvider The ServiceProvider instance for providing various types of services
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names
     * @param handlerExecutor The HandlerExecutor instance for handling domain event listeners
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization
     * @param transactionOutbox The TransactionOutbox instance for managing transactional outbox messages
     * @param domainEventsInstantiator The DomainEventsInstantiator instance for instantiating a domain event sender
     * @param connectionFactory The ConnectionFactory instance for creating JMS connections
     */
    public GruelboxProxyActiveMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        ConnectionFactory connectionFactory
    ) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper, transactionOutbox, domainEventsInstantiator);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Initializes a GruelboxProxyActiveMqChannelFactory with the provided dependencies.
     *
     * @param serviceProvider The ServiceProvider instance for providing various types of services
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names
     * @param handlerExecutor The HandlerExecutor instance for handling domain event listeners
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization
     * @param transactionOutbox The TransactionOutbox instance for managing transactional outbox messages
     * @param domainEventsInstantiator The DomainEventsInstantiator instance for instantiating a domain event sender
     * @param pollerConfiguration The PollerConfiguration for polling configuration on the outbox
     * @param publishingSchedulerConfiguration The PublishingSchedulerConfiguration for scheduling publishing tasks
     * @param connectionFactory The ConnectionFactory instance for creating JMS connections
     */
    public GruelboxProxyActiveMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        PollerConfiguration pollerConfiguration,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration, ConnectionFactory connectionFactory) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper, transactionOutbox, domainEventsInstantiator, pollerConfiguration, publishingSchedulerConfiguration);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Initializes a GruelboxProxyActiveMqChannelFactory with the provided dependencies.
     * This constructor should only be used for consumer only channels.
     *
     * @param serviceProvider The ServiceProvider instance for providing various types of services.
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names.
     * @param handlerExecutor The HandlerExecutor instance for handling domain event listeners.
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization.
     * @param connectionFactory The ConnectionFactory instance for creating ActiveMq connections.
     */
    public GruelboxProxyActiveMqChannelFactory(ServiceProvider serviceProvider, ClassProvider classProvider, HandlerExecutor handlerExecutor, ObjectMapper objectMapper, ConnectionFactory connectionFactory) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Initializes a GruelboxProxyActiveMqChannelFactory with the provided dependencies.
     *
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization
     * @param transactionOutbox The TransactionOutbox instance for managing transactional outbox messages
     * @param domainEventsInstantiator The DomainEventsInstantiator instance for instantiating a domain event sender
     * @param connectionFactory The ConnectionFactory instance for creating ActiveMq connections
     */
    public GruelboxProxyActiveMqChannelFactory(ObjectMapper objectMapper, TransactionOutbox transactionOutbox, DomainEventsInstantiator domainEventsInstantiator, ConnectionFactory connectionFactory) {
        super(objectMapper, transactionOutbox, domainEventsInstantiator);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Initializes a GruelboxProxyJakartaJmsChannelFactory with the provided dependencies.
     *
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization
     * @param transactionOutbox The TransactionOutbox instance for managing transactional outbox messages
     * @param domainEventsInstantiator The DomainEventsInstantiator instance for instantiating a domain event sender
     * @param pollerConfiguration The PollerConfiguration for polling configuration on the outbox
     * @param publishingSchedulerConfiguration The PublishingSchedulerConfiguration for scheduling publishing tasks
     * @param connectionFactory The ConnectionFactory instance for creating ActiveMq connections
     */
    public GruelboxProxyActiveMqChannelFactory(ObjectMapper objectMapper, TransactionOutbox transactionOutbox, DomainEventsInstantiator domainEventsInstantiator, PollerConfiguration pollerConfiguration, PublishingSchedulerConfiguration publishingSchedulerConfiguration, ConnectionFactory connectionFactory) {
        super(objectMapper, transactionOutbox, domainEventsInstantiator, pollerConfiguration, publishingSchedulerConfiguration);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    /**
     * Creates an Active MQ based Domain Event publisher.
     *
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization.
     * @return A new instance of JakartaJmsDomainEventPublisher initialized with the provided ObjectMapper.
     */
    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        return new ActiveMqDomainEventPublisher(connectionFactory, objectMapper, this.virtualTopicPrefix);
    }

    /**
     * Creates an Active MQ based Domain Event Consumer
     *
     * @param objectMapper The ObjectMapper instance for serialization/deserialization
     * @param executionContextDetector The ExecutionContextDetector instance for detecting execution contexts
     * @param executionContextProcessor The ExecutionContextProcessor instance for processing execution contexts
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names
     * @return A new MqDomainEventConsumer initialized with the provided instances
     */
    @Override
    protected MqDomainEventConsumer provideMqDomainEventConsumer(ObjectMapper objectMapper, ExecutionContextDetector executionContextDetector, ExecutionContextProcessor executionContextProcessor, ClassProvider classProvider) {
        return new ActiveMqDomainEventConsumer(
            connectionFactory,
            objectMapper,
            executionContextDetector,
            executionContextProcessor,
            classProvider,
            this.virtualTopicConsumerPrefix,
            this.virtualTopicPrefix,
            this.receiveTimeoutMs
        );
    }

    /**
     * Sets the receive timeout in milliseconds for the messaging channel.
     *
     * @param receiveTimeoutMs The receive timeout value to be set in milliseconds.
     */
    public void setReceiveTimeoutMs(long receiveTimeoutMs) {
        this.receiveTimeoutMs = receiveTimeoutMs;
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
}

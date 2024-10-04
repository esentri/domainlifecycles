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

package io.domainlifecycles.events.mq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

/**
 * Abstract class representing a factory for creating different types of message queue channels.
 *
 * @author Mario Herb
 */
public abstract class AbstractMqChannelFactory implements ChannelFactory {

    protected final ServiceProvider serviceProvider;
    protected final ClassProvider classProvider;
    protected final HandlerExecutor handlerExecutor;
    protected final ObjectMapper objectMapper;

    /**
     * Constructs a new AbstractMqChannelFactory with the given parameters.
     * Could be used for publishing and consuming use cases.
     *
     * @param serviceProvider The ServiceProvider instance to provide various types of services.
     * @param classProvider The ClassProvider instance to provide Class instances for full qualified class names.
     * @param handlerExecutor The HandlerExecutor instance to execute domain event listeners.
     * @param objectMapper The ObjectMapper instance for serialization/deserialization.
     */
    public AbstractMqChannelFactory(ServiceProvider serviceProvider,
                                    ClassProvider classProvider,
                                    HandlerExecutor handlerExecutor,
                                    ObjectMapper objectMapper) {
        this.serviceProvider = serviceProvider;
        this.classProvider = classProvider;
        this.handlerExecutor = handlerExecutor;
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper is required!");
    }

    /**
     * Constructs a new AbstractMqChannelFactory with the given ObjectMapper instance.
     * Should be used for publishing use cases only.
     *
     * @param objectMapper The ObjectMapper instance for serialization/deserialization
     */
    public AbstractMqChannelFactory(ObjectMapper objectMapper){
        this(null, null, null, objectMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMqConsumingChannel consumeOnlyChannel(String channelName) {
        return new AbstractMqConsumingChannel(channelName, consumingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMqPublishingChannel publishOnlyChannel(String channelName) {
        return new AbstractMqPublishingChannel(channelName, publishingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMqProcessingChannel processingChannel(String channelName) {
        return new AbstractMqProcessingChannel(channelName, consumingConfiguration(), publishingConfiguration());
    }

    /**
     * This method creates and returns an instance of AbstractMqConsumingConfiguration
     * which contains a Message Queue (MQ) Domain Event Consumer configuration.
     *
     * @return a new instance of AbstractMqConsumingConfiguration populated with the necessary configurations
     */
    protected AbstractMqConsumingConfiguration consumingConfiguration(){
        Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(Objects.requireNonNull(handlerExecutor,"A HandlerExecutor is required!"));

        return new AbstractMqConsumingConfiguration(provideMqDomainEventConsumer(
            this.objectMapper,
            executionContextDetector,
            executionContextProcessor,
            Objects.requireNonNull(this.classProvider, "A ClassProvider is required!")
        ));
    }

    /**
     * Returns an AbstractMqPublishingConfiguration object that represents a configuration for publishing domain events via a dedicated message broker infrastructure.
     *
     * @return An instance of AbstractMqPublishingConfiguration populated with the necessary configurations
     */
    protected AbstractMqPublishingConfiguration publishingConfiguration(){
        return new AbstractMqPublishingConfiguration(provideMqDomainEventPublisher(this.objectMapper));
    }

    /**
     * Provides a method to obtain an implementation of MqDomainEventPublisher for publishing domain events.
     *
     * @param objectMapper The ObjectMapper instance used for serialization/deserialization.
     * @return An implementation of MqDomainEventPublisher with the provided ObjectMapper.
     */
    abstract protected MqDomainEventPublisher provideMqDomainEventPublisher(
        ObjectMapper objectMapper
    );

    /**
     * Provides an abstract method to create and return an instance of MqDomainEventConsumer,
     * which is responsible for consuming Message Queue (MQ) domain events.
     *
     * @param objectMapper The ObjectMapper instance for serialization/deserialization
     * @param executionContextDetector The ExecutionContextDetector instance for detecting execution contexts
     * @param executionContextProcessor The ExecutionContextProcessor instance for processing execution contexts
     * @param classProvider The ClassProvider instance for providing Class instances for full qualified class names
     * @return a MqDomainEventConsumer instance created with the provided parameters
     */
    abstract protected MqDomainEventConsumer provideMqDomainEventConsumer(
        ObjectMapper objectMapper,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
    );


}

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

package io.domainlifecycles.events.api;

import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.publish.direct.DirectPassThroughDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.GeneralReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.AsyncExecutionContextProcessor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;


import java.util.Objects;

/**
 * The DomainEventsConfigurationImpl class represents the configuration
 * for handling and dispatching domain events in the application.
 *
 * @author Mario Herb
 */
public final class DomainEventsConfigurationImpl implements DomainEventsConfiguration{


    private final DomainEventPublisher domainEventPublisher;
    private final ServiceProvider serviceProvider;
    private final ReceivingDomainEventHandler receivingDomainEventHandler;
    private final ExecutionContextDetector executionContextDetector;
    private final HandlerExecutor handlerExecutor;
    private final ExecutionContextProcessor executionContextProcessor;

    protected DomainEventsConfigurationImpl(
        DomainEventPublisher domainEventPublisher,
        ServiceProvider serviceProvider,
        ReceivingDomainEventHandler receivingDomainEventHandler,
        ExecutionContextDetector executionContextDetector,
        HandlerExecutor handlerExecutor,
        ExecutionContextProcessor executionContextProcessor
    ) {
        Objects.requireNonNull(
            serviceProvider,
            "A ServiceProvider is needed to be able to dispatch events properly!"
        );

        Objects.requireNonNull(
            executionContextDetector,
            "An ExecutionContextDetector is needed to be able to process events properly!"
        );

        Objects.requireNonNull(
            handlerExecutor,
            "A HandlerExecutor is needed to be able to process events properly!"
        );

        Objects.requireNonNull(
            executionContextProcessor,
            "An ExecutionContextProcessor is needed to be able to process events properly!"
        );

        Objects.requireNonNull(
            receivingDomainEventHandler,
            "A ReceivingDomainEventHandler is needed to be able to process events properly!"
        );

        Objects.requireNonNull(
            domainEventPublisher,
            "A DomainEventPublisher is needed to be able to publish events properly!"
        );
        this.domainEventPublisher = domainEventPublisher;
        this.serviceProvider = serviceProvider;
        this.receivingDomainEventHandler = receivingDomainEventHandler;
        this.executionContextDetector = executionContextDetector;
        this.handlerExecutor = handlerExecutor;
        this.executionContextProcessor = executionContextProcessor;
        DomainEvents.registerConfiguration(this);
    }

    @Override
    public DomainEventPublisher getDomainEventPublisher() {
        return this.domainEventPublisher;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return this.serviceProvider;
    }

    @Override
    public ReceivingDomainEventHandler getReceivingDomainEventHandler() {
        return this.receivingDomainEventHandler;
    }

    @Override
    public ExecutionContextDetector getExecutionContextDetector() {
        return this.executionContextDetector;
    }

    @Override
    public HandlerExecutor getHandlerExecutor() {
        return this.handlerExecutor;
    }

    @Override
    public ExecutionContextProcessor getExecutionContextProcessor() {
        return this.executionContextProcessor;
    }

    /**
     * The DomainEventsConfigurationBuilder class is used to build a configuration for publishing and handling
     * domain events in the application.
     */


    public DomainEventsConfigurationBuilder toBuilder(){
        return new DomainEventsConfigurationBuilder()
            .withServiceProvider(this.serviceProvider)
            .withDomainEventPublisher(this.domainEventPublisher)
            .withReceivingDomainEventHandler(this.receivingDomainEventHandler)
            .withHandlerExecutor(this.handlerExecutor)
            .withExecutionContextProcessor(this.executionContextProcessor)
            .withExecutionContextDetector(this.executionContextDetector);
    }
}

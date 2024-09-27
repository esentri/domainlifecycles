
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
package io.domainlifecycles.events.inmemory;

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

/**
 * Constructs a new `InMemoryConsumingConfiguration` object.
 *
 * @param serviceProvider The service provider.
 * @param domainEventConsumer The domain event consumer.
 * @param executionContextDetector The execution context detector.
 * @param handlerExecutor The handler executor.
 * @param executionContextProcessor The execution context processor.
 * @throws NullPointerException if any of the parameters is null.
 *
 * @author Mario Herb
 */
record InMemoryConsumingConfiguration(ServiceProvider serviceProvider, DomainEventConsumer domainEventConsumer,
                                             ExecutionContextDetector executionContextDetector, HandlerExecutor handlerExecutor,
                                             ExecutionContextProcessor executionContextProcessor) implements ConsumingConfiguration {

    InMemoryConsumingConfiguration(ServiceProvider serviceProvider,
                                          DomainEventConsumer domainEventConsumer,
                                          ExecutionContextDetector executionContextDetector,
                                          HandlerExecutor handlerExecutor,
                                          ExecutionContextProcessor executionContextProcessor) {
        this.serviceProvider = Objects.requireNonNull(serviceProvider, "ServiceProvider is required!");
        this.domainEventConsumer = Objects.requireNonNull(domainEventConsumer, "DomainEventConsumer is required!");
        this.executionContextDetector = Objects.requireNonNull(executionContextDetector, "ExecutionContextDetector is required!");
        this.handlerExecutor = Objects.requireNonNull(handlerExecutor, "HandlerExecutor is required!");
        this.executionContextProcessor = Objects.requireNonNull(executionContextProcessor, "ExecutionContextProcessor is required!");
    }
}

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

package io.domainlifecycles.events.inmemory;

import io.domainlifecycles.events.consume.GeneralDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;

import io.domainlifecycles.events.consume.execution.processor.AsyncExecutionContextProcessor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

/**
 * The InMemoryConsumingConfigurationFactory class provides methods for creating instances of InMemoryConsumingConfiguration.
 * It allows consuming domain events either synchronously or asynchronously.
 *
 * @author Mario Herb
 */
class InMemoryConsumingConfigurationFactory {

    /**
     * Creates a synchronous InMemoryConsumingConfiguration.
     *
     * @param serviceProvider The service provider responsible for providing instances of services.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @return The InMemoryConsumingConfiguration object containing the necessary instances for consuming domain events synchronously.
     */
    InMemoryConsumingConfiguration consumeSync(ServiceProvider serviceProvider, HandlerExecutor handlerExecutor){
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);

        return new InMemoryConsumingConfiguration(
            serviceProvider,
            domainEventConsumer,
            executionContextDetector,
            handlerExecutor,
            executionContextProcessor);
    }

    /**
     * Creates an asynchronous InMemoryConsumingConfiguration.
     *
     * @param serviceProvider The service provider responsible for providing instances of services.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param executorThreadPoolSize The size of the executor thread pool.
     * @return The InMemoryConsumingConfiguration object containing the necessary instances for consuming domain events asynchronously.
     */
    InMemoryConsumingConfiguration consumeAsync(ServiceProvider serviceProvider, HandlerExecutor handlerExecutor, int executorThreadPoolSize){
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new AsyncExecutionContextProcessor(handlerExecutor, executorThreadPoolSize);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);

        return new InMemoryConsumingConfiguration(
            serviceProvider,
            domainEventConsumer,
            executionContextDetector,
            handlerExecutor,
            executionContextProcessor);
    }
}

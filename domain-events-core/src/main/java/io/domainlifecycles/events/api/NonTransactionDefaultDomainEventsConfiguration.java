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

import io.domainlifecycles.events.publish.direct.DirectPassThroughDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.GeneralReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.AsyncExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;


public final class NonTransactionDefaultDomainEventsConfiguration{

    private final DomainEventsConfiguration domainEventsConfiguration;

    public NonTransactionDefaultDomainEventsConfiguration(ServiceProvider serviceProvider) {
        var builder = new DomainEventsConfigurationBuilder();
        var handlerExecutor = new ReflectiveHandlerExecutor();
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new AsyncExecutionContextProcessor(handlerExecutor);
        var receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(executionContextDetector, executionContextProcessor);
        var domainEventPublisher = new DirectPassThroughDomainEventPublisher(receivingDomainEventHandler);
        domainEventsConfiguration = builder
            .withServiceProvider(serviceProvider)
            .withReceivingDomainEventHandler(receivingDomainEventHandler)
            .withDomainEventPublisher(domainEventPublisher)
            .withHandlerExecutor(handlerExecutor)
            .withExecutionContextProcessor(executionContextProcessor)
            .withExecutionContextDetector(executionContextDetector)
            .build();
    }

    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }
}

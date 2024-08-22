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
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

public class DomainEventsConfigurationBuilder {

    private DomainEventPublisher domainEventPublisher;
    private ServiceProvider serviceProvider;
    private ReceivingDomainEventHandler receivingDomainEventHandler;
    private ExecutionContextDetector executionContextDetector;
    private HandlerExecutor handlerExecutor;
    private ExecutionContextProcessor executionContextProcessor;

    public DomainEventsConfigurationBuilder withDomainEventPublisher(DomainEventPublisher domainEventPublisher){
        this.domainEventPublisher = domainEventPublisher;
        return this;
    }

    public DomainEventsConfigurationBuilder withServiceProvider(ServiceProvider serviceProvider){
        this.serviceProvider = serviceProvider;
        return this;
    }

    public DomainEventsConfigurationBuilder withReceivingDomainEventHandler(ReceivingDomainEventHandler receivingDomainEventHandler){
        this.receivingDomainEventHandler = receivingDomainEventHandler;
        return this;
    }

    public DomainEventsConfigurationBuilder withExecutionContextDetector(ExecutionContextDetector executionContextDetector){
        this.executionContextDetector = executionContextDetector;
        return this;
    }

    public DomainEventsConfigurationBuilder withHandlerExecutor(HandlerExecutor handlerExecutor){
        this.handlerExecutor = handlerExecutor;
        return this;
    }

    public DomainEventsConfigurationBuilder withExecutionContextProcessor(ExecutionContextProcessor executionContextProcessor){
        this.executionContextProcessor = executionContextProcessor;
        return this;
    }

    public DomainEventsConfigurationImpl build(){
        var config = new DomainEventsConfigurationImpl(
            this.domainEventPublisher,
            this.serviceProvider,
            this.receivingDomainEventHandler,
            this.executionContextDetector,
            this.handlerExecutor,
            this.executionContextProcessor
        );
        return config;
    }
}

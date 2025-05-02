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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.consume.execution.detector.AggregateExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ServiceExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;

/**
 * IdempotentExecutor is responsible for executing idempotent operations based on the provided IdempotentExecutionContext and service provider.
 *
 * @author Mario Herb
 */
public class IdempotentExecutor {

    private final ServiceProvider serviceProvider;
    private final HandlerExecutor handlerExecutor;

    /**
     * Initializes a new IdempotentExecutor with the provided ServiceProvider and HandlerExecutor.
     *
     * @param serviceProvider The service provider used to retrieve service instances.
     * @param handlerExecutor The executor used to handle domain event listeners.
     */
    public IdempotentExecutor(ServiceProvider serviceProvider, HandlerExecutor handlerExecutor) {
        this.serviceProvider = serviceProvider;
        this.handlerExecutor = handlerExecutor;
    }

    /**
     * Executes an idempotent operation based on the provided IdempotentExecutionContext.
     * This method retrieves the service instance based on the handler class,
     * creates an appropriate ExecutionContext based on the handler type,
     * and then executes the handler using the HandlerExecutor.
     *
     * @param idempotentExecutionContext The context for executing the idempotent operation.
     */
    public void execute(IdempotentExecutionContext idempotentExecutionContext){
        ExecutionContext executionContext = null;
        var service = serviceProvider.getServiceKindInstance(idempotentExecutionContext.handlerClass().getName());
        if(Repository.class.isAssignableFrom(idempotentExecutionContext.handlerClass()) && (idempotentExecutionContext.domainEvent() instanceof AggregateDomainEvent)){
            executionContext = new AggregateExecutionContext<>((Repository<?,?>) service, idempotentExecutionContext.handlerMethod(), idempotentExecutionContext.handlerClass().getName(), (AggregateDomainEvent)idempotentExecutionContext.domainEvent());
        }else{
            executionContext = new ServiceExecutionContext(service, idempotentExecutionContext.handlerClass().getName(), idempotentExecutionContext.handlerMethod(), idempotentExecutionContext.domainEvent());
        }
        handlerExecutor.execute(executionContext);
    }

}

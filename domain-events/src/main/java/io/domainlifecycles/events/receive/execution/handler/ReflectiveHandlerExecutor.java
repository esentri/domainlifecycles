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

package io.domainlifecycles.events.receive.execution.handler;


import io.domainlifecycles.domain.types.AggregateRoot;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.receive.execution.detector.AggregateExecutionContext;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContext;
import io.domainlifecycles.events.receive.execution.detector.ServiceExecutionContext;
import io.domainlifecycles.reflect.JavaReflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReflectiveHandlerExecutor is a concrete implementation of the HandlerExecutor interface.
 * It executes domain event listeners using reflection.
 *
 * @author Mario Herb
 */
public class ReflectiveHandlerExecutor implements HandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(ReflectiveHandlerExecutor.class);

    /**
     * Executes the handler method for a domain event listener based on the given execution context.
     *
     * @param executionContext The execution context representing the handler and the domain event.
     * @return true if the execution of the handler method was successful, false otherwise.
     */
    @Override
    public boolean execute(ExecutionContext executionContext) {
        if (executionContext instanceof ServiceExecutionContext) {
            return callDomainEventListenerMethods((ServiceExecutionContext) executionContext);
        } else {
            return callDomainEventListenerMethodsOnAggregate((AggregateExecutionContext<?, ?>) executionContext);
        }
    }

    private boolean callDomainEventListenerMethods(ServiceExecutionContext serviceExecutionContext) {
        return JavaReflect
            .methods(serviceExecutionContext.handler().getClass())
            .stream()
            .filter(m -> m.getName().equals(serviceExecutionContext.handlerMethodName())
                && m.getParameters()[0].getType().getName().equals(
                serviceExecutionContext.domainEvent().getClass().getName()))
            .findFirst()
            .map(m -> {
                var success = false;
                try {
                    try {
                        beforeExecution(serviceExecutionContext);
                        m.invoke(serviceExecutionContext.handler(), serviceExecutionContext.domainEvent());
                        success = true;
                        log.debug("DomainEvent Listener method {} called on {} for DomainEvent {}",
                            serviceExecutionContext.handlerMethodName(),
                            serviceExecutionContext.handler(),
                            serviceExecutionContext.domainEvent()
                        );

                    } catch (Throwable e) {
                        log.error("DomainEvent Listener method {} execution on {} for DomainEvent {} failed!",
                            serviceExecutionContext.handlerMethodName(),
                            serviceExecutionContext.handler(),
                            serviceExecutionContext.domainEvent()
                        );
                    }
                } finally {
                    afterExecution(serviceExecutionContext, success);
                }
                return success;
            })
            .orElse(false);
    }

    private <I extends Identity<?>, A extends AggregateRoot<I>> boolean callDomainEventListenerMethodsOnAggregate(AggregateExecutionContext<I, A> aggregateExecutionContext) {
        if (aggregateExecutionContext.domainEvent().targetId() == null) {
            throw DLCEventsException.fail("TargetId is null for AggregateDomainEvent %s! " +
                    "DomainEvent could not be delivered to AggregateRoot!",
                aggregateExecutionContext.domainEvent().getClass().getName());
        }
        var success = false;
        try {
            beforeExecution(aggregateExecutionContext);
            var root = aggregateExecutionContext.aggregateRepository()
                .findById(aggregateExecutionContext.domainEvent().targetId())
                .orElseThrow(() -> DLCEventsException.fail("No Aggregate instance found for AggregateDomainEvent %s! " +
                        "DomainEvent could not be delivered to AggregateRoot!",
                    aggregateExecutionContext.domainEvent().getClass().getName()));
            var method = JavaReflect
                .methods(root.getClass())
                .stream()
                .filter(m -> m.getName().equals(aggregateExecutionContext.aggregateHandlerMethodName())
                    && m.getParameters()[0].getType().getName().equals(
                    aggregateExecutionContext.domainEvent().getClass().getName()))
                .findFirst()
                .orElseThrow(() -> DLCEventsException.fail("Method {} not found on {}",
                    aggregateExecutionContext.aggregateHandlerMethodName(),
                    root.getClass().getName()
                ));
            try {
                method.invoke(root, aggregateExecutionContext.domainEvent());
                success = true;
                log.debug("DomainEvent Listener method {} called on {} for AggregateDomainEvent {}",
                    aggregateExecutionContext.aggregateHandlerMethodName(),
                    root,
                    aggregateExecutionContext.domainEvent()
                );
            } catch (Throwable e) {
                log.error("DomainEvent Listener method {} execution on {} for AggregateDomainEvent {} failed!",
                    aggregateExecutionContext.aggregateHandlerMethodName(),
                    root,
                    aggregateExecutionContext.domainEvent()
                );
            }
            if (success) {
                aggregateExecutionContext.aggregateRepository().update(root);
                log.debug("AggregateRoot {} with Id {} updated on repository",
                    root.getClass().getName(),
                    aggregateExecutionContext.domainEvent().targetId()
                );
            }
        } finally {
            afterExecution(aggregateExecutionContext, success);
        }
        return success;
    }


}

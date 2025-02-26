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

package io.domainlifecycles.events.consume;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionResult;

import java.util.List;

/**
 * The {@code DomainEventConsumer} interface defines a contract for classes
 * that handle received domain events.
 *
 * <p>A DomainEventConsumer is responsible for detecting execution contexts
 * for a domain event and processing them to produce execution results. The execution results
 * represent the outcome of processing the execution contexts.
 *
 * <p>Each implementation of this interface should provide an implementation for the
 * {@link #consume(DomainEvent)} method, which takes a domain event and returns a
 * list of execution results.
 *
 * <p><b>Note:</b> It is important to ensure that a non-null domainEvent is provided to the
 * {@link #consume(DomainEvent)} method.
 *
 * @see ExecutionResult
 * @see ExecutionContextDetector
 * @see ExecutionContextProcessor
 * @see DomainEvent
 *
 * @author Mario Herb
 */
public interface DomainEventConsumer {

    /**
     * Handles a received domain event and returns a list of execution results.
     *
     * @param domainEvent The domain event to be handled
     * @return The list of execution results
     */
    List<ExecutionResult> consume(DomainEvent domainEvent);

    /**
     * Handles a received domain event and returns the execution result for the specified
     * target execution context.
     *
     * @param domainEvent The domain event to be handled
     * @param executionContext The target context, where the event should be processed
     * @return The execution result
     */
    ExecutionResult consume(DomainEvent domainEvent, TargetExecutionContext executionContext);
}

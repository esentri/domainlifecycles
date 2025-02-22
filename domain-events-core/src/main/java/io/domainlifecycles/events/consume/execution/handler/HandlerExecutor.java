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

package io.domainlifecycles.events.consume.execution.handler;

import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;

/**
 * HandlerExecutor interface represents an executor for handling domain event listeners.
 *
 * @author Mario Herb
 */
public interface HandlerExecutor {

    /**
     * Executes a handler within the provided execution context.
     *
     * @param executionContext the execution context for the handler containing necessary information
     * @return true if the execution was successful, false otherwise
     */
    boolean execute(ExecutionContext executionContext);

    /**
     * This method is called before the execution of a listener method for a domain event.
     * It provides the necessary parameters to perform any pre-processing tasks.
     *
     * @param executionContext the execution context
     */
    default void beforeExecution(ExecutionContext executionContext) {
    }

    /**
     * This method is called after the execution of a listener method for a domain event.
     * It can be used for any post-processing tasks.
     *
     * @param executionContext the execution context executed
     * @param success          A boolean flag indicating whether the listener method execution was successful or not.
     */
    default void afterExecution(ExecutionContext executionContext, boolean success) {
    }
}

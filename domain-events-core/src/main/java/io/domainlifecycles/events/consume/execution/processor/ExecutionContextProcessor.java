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

package io.domainlifecycles.events.consume.execution.processor;

import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;

import java.util.List;

/**
 * The {@code ExecutionContextProcessor} interface defines a contract for classes
 * that process execution contexts.
 *
 * <p>An execution context processor is responsible for processing a list of {@link ExecutionContext}
 * objects and producing a list of {@link ExecutionResult} objects. The execution results represent
 * the outcome of processing the execution contexts.
 *
 * <p>Each implementation of this interface should provide an implementation for the
 * {@link #process(List)} method, which takes a list of execution contexts and returns
 * a list of execution results.
 *
 * @author Mario Herb
 * @see ExecutionContext
 * @see ExecutionResult
 */
public interface ExecutionContextProcessor {

    /**
     * The {@code process} method processes a list of {@link ExecutionContext} objects and
     * produces a list of {@link ExecutionResult} objects. It takes a list of execution contexts as
     * input and returns the corresponding execution results.
     *
     * @param contextList the list of {@link ExecutionContext} objects to be processed
     * @return the list of {@link ExecutionResult} objects produced by processing the execution contexts
     */
    List<ExecutionResult> process(List<ExecutionContext> contextList);
}

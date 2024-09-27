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
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * SimpleExecutionContextProcessor is an implementation of the ExecutionContextProcessor interface.
 * It processes a list of ExecutionContext objects by executing a HandlerExecutor on each context in single thread.
 *
 * @author Mario Herb
 */
public class SimpleExecutionContextProcessor implements ExecutionContextProcessor {

    private static final Logger log = LoggerFactory.getLogger(SimpleExecutionContextProcessor.class);

    protected final HandlerExecutor handlerExecutor;

    public SimpleExecutionContextProcessor(HandlerExecutor handlerExecutor) {
        this.handlerExecutor = Objects.requireNonNull(handlerExecutor, "A HandlerExecutor is required!");
    }

    /**
     * Processes a list of {@link ExecutionContext} objects by executing a {@link HandlerExecutor} on each context.
     *
     * @param contextList The list of execution contexts to be processed.
     * @return A list of {@link ExecutionResult} objects, representing the results of the processing.
     */
    @Override
    public List<ExecutionResult> process(List<ExecutionContext> contextList) {
        log.debug("Processing detected execution contexts ({} instances)!", contextList.size());
        return contextList.stream().map(context -> new ExecutionResult(context, handlerExecutor.execute(context))).toList();
    }
}

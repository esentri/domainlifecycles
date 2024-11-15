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

package io.domainlifecycles.events.consume;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The GeneralDomainEventConsumer is responsible for handling received domain events.
 * It detects execution contexts for a domain event and processes them to produce execution results.
 * The execution results represent the outcome of processing the execution contexts.
 *
 * @author Mario Herb
 */
public final class GeneralDomainEventConsumer implements DomainEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(GeneralDomainEventConsumer.class);
    private final ExecutionContextDetector executionContextDetector;
    private final ExecutionContextProcessor executionContextProcessor;

    /**
     * Constructs a GeneralDomainEventConsumer with the provided ExecutionContextDetector and ExecutionContextProcessor.
     * An ExecutionContextDetector and ExecutionContextProcessor must be provided for DomainEvent handling.
     *
     * @param executionContextDetector The ExecutionContextDetector to detect execution contexts for domain events
     * @param executionContextProcessor The ExecutionContextProcessor to process execution contexts and produce execution results
     */
    public GeneralDomainEventConsumer(
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor
    ) {
        this.executionContextDetector = Objects.requireNonNull(executionContextDetector,
            "An ExecutionContextDetector must be provided for DomainEvent handling!");
        this.executionContextProcessor = Objects.requireNonNull(executionContextProcessor,
            "An ExecutionContextProcessor must be provided for DomainEvent handling!");
    }

    /**
     * Handles a received domain event by detecting execution contexts and
     * processing them to produce execution results.
     *
     * @param domainEvent The domain event to be handled
     * @return The list of execution results
     */
    @Override
    public List<ExecutionResult> consume(DomainEvent domainEvent) {
        log.debug("Received {}", domainEvent);
        var executionContexts = executionContextDetector.detectExecutionContexts(domainEvent);
        if (executionContexts == null || executionContexts.isEmpty()) {
            log.debug("No execution contexts detected for {}", domainEvent);
            return Collections.emptyList();
        }
        return executionContextProcessor.process(executionContexts);
    }
}

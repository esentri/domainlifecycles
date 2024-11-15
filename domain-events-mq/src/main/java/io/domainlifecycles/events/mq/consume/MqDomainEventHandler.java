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

package io.domainlifecycles.events.mq.consume;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.mirror.api.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a handler for processing domain events received from a message queue.
 * It detects the execution contexts for a domain event and processes them using the provided ExecutionContextDetector
 * and ExecutionContextProcessor.
 *
 * @author Mario Herb
 */
public class MqDomainEventHandler {

    private Logger log = LoggerFactory.getLogger(MqDomainEventHandler.class);

    private final String handlerClassName;
    private final String handlerMethodName;
    private final Class<? extends DomainEvent> domainEventType;
    private final ExecutionContextDetector executionContextDetector;
    private final ExecutionContextProcessor executionContextProcessor;
    private final AtomicBoolean paused = new AtomicBoolean(false);

    /**
     * Initializes a new {@code MqDomainEventHandler} with the provided parameters.
     *
     * @param handlerClassName The class name of the handler
     * @param handlerMethodName The method name of the handler
     * @param domainEventType The type of domain event to handle
     * @param executionContextDetector The detector for execution contexts
     * @param executionContextProcessor The processor for execution contexts
     */
    public MqDomainEventHandler(String handlerClassName,
                                String handlerMethodName,
                                Class<? extends DomainEvent> domainEventType,
                                ExecutionContextDetector executionContextDetector,
                                ExecutionContextProcessor executionContextProcessor
    ) {
        this.handlerClassName = Objects.requireNonNull(handlerClassName, "handlerClassName is required!");
        this.handlerMethodName = Objects.requireNonNull(handlerMethodName, "handlerMethodName is required!");
        this.domainEventType = Objects.requireNonNull(domainEventType , "domainEventType is required!");
        this.executionContextDetector = executionContextDetector;
        this.executionContextProcessor = executionContextProcessor;
    }

    /**
     * Handles the given domain event by detecting execution contexts, processing them, and logging debug information.
     *
     * @param domainEvent The domain event to handle
     */
    public void handle(DomainEvent domainEvent){
        log.debug("Detecting execution contexts for {}", domainEvent);
        var executionContextHandlerName = handlerClassName;
        if(domainEvent instanceof AggregateDomainEvent<?,?>){
            var aggregateRootMirror = Domain
                .aggregateRootMirrorFor(handlerClassName);
            executionContextHandlerName = Domain.repositoryMirrorFor(aggregateRootMirror)
                .getTypeName();
        }
        final var finalHandlerName = executionContextHandlerName;
        var filteredExecutionContexts = executionContextDetector.detectExecutionContexts(domainEvent)
            .stream()
            .filter(ec ->
                finalHandlerName.equals(ec.handler().getClass().getName())
                && handlerMethodName.equals(ec.handlerMethodName())
                && domainEventType.getName().equals(ec.domainEvent().getClass().getName())
            ).toList();
        log.debug("Detected execution contexts for {} - {}", domainEvent, filteredExecutionContexts);
        executionContextProcessor.process(filteredExecutionContexts);
        log.debug("Processed execution contexts for {} - {}", domainEvent, filteredExecutionContexts);
    }

    /**
     * Retrieves the unique identifier for the handler based on the handler class name, method name,
     * and domain event type. The identifier is created by replacing dots in class names and domain event names with underscores,
     * and concatenating them with hyphens.
     *
     * @return The unique handler identifier
     */
    public String getHandlerId() {
        return handlerClassName.replaceAll("\\.", "_")
            +"-"+ handlerMethodName+"-"
            +domainEventType.getName().replaceAll("\\.", "_");
    }

    /**
     * Retrieves the specific type of domain event handled by this handler.
     *
     * @return The class representing the domain event type
     */
    public Class<? extends DomainEvent> getDomainEventType() {
        return domainEventType;
    }

    /**
     * Retrieves the class name of the handler.
     *
     * @return The class name of the handler
     */
    public String getHandlerClassName() {
        return handlerClassName;
    }

    /**
     * Retrieves the handler method name.
     *
     * @return The method name of the handler
     */
    public String getHandlerMethodName() {
        return handlerMethodName;
    }

    /**
     * Pauses the execution of the event handler.
     * Once paused, the handler will not process any incoming events until resumed.
     */
    public void pause(){
        this.paused.set(true);
    }

    /**
     * Resumes the execution of the event handler. Once resumed, the handler will begin processing incoming events.
     */
    public void resume(){
        this.paused.set(false);
    }

    /**
     * Checks if the event handler is currently paused.
     *
     * @return true if the event handler is paused, false otherwise
     */
    public boolean isPaused(){
        return this.paused.get();
    }
}

package io.domainlifecycles.events.mq.consume;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.mirror.api.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MqDomainEventHandler {

    private Logger log = LoggerFactory.getLogger(MqDomainEventHandler.class);

    private final String handlerClassName;
    private final String handlerMethodName;
    private final Class<? extends DomainEvent> domainEventType;
    private final ExecutionContextDetector executionContextDetector;
    private final ExecutionContextProcessor executionContextProcessor;
    private final AtomicBoolean paused = new AtomicBoolean(false);

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

    public String getHandlerId() {
        return handlerClassName.replaceAll("\\.", "_")
            +"-"+ handlerMethodName+"-"
            +domainEventType.getName().replaceAll("\\.", "_");
    }

    public Class<? extends DomainEvent> getDomainEventType() {
        return domainEventType;
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public String getHandlerMethodName() {
        return handlerMethodName;
    }

    public void pause(){
        this.paused.set(true);
    }

    public void resume(){
        this.paused.set(false);
    }

    public boolean isPaused(){
        return this.paused.get();
    }
}

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

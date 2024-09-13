package io.domainlifecycles.events.mq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.api.NonTransactionDefaultDomainEventsConfiguration;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.DummyReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.receive.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

public abstract class AbstractMqDomainEventsConfiguration {

    private DomainEventsConfiguration domainEventsConfiguration;
    private MqDomainEventPublisher mqDomainEventPublisher;
    private MqDomainEventConsumer mqDomainEventConsumer;
    private final ServiceProvider serviceProvider;
    private final ClassProvider classProvider;
    private final HandlerExecutor handlerExecutor;
    private final ObjectMapper objectMapper;
    private final ExecutionContextDetector executionContextDetector;
    private final ExecutionContextProcessor executionContextProcessor;


    public AbstractMqDomainEventsConfiguration(ServiceProvider serviceProvider,
                                               ClassProvider classProvider,
                                               HandlerExecutor handlerExecutor,
                                               ObjectMapper objectMapper) {
        this.serviceProvider = serviceProvider;
        this.classProvider = classProvider;
        this.handlerExecutor = handlerExecutor;
        this.objectMapper = objectMapper;
        this.executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);

    }

    public void initialize(){
        this.mqDomainEventPublisher = provideMqDomainEventPublisher(objectMapper);
        this.mqDomainEventConsumer = provideMqDomainEventConsumer(
            objectMapper,
            executionContextDetector,
            executionContextProcessor,
            classProvider
        );
        var defaultConfig = new NonTransactionDefaultDomainEventsConfiguration(serviceProvider)
            .getDomainEventsConfiguration();
        this.domainEventsConfiguration = defaultConfig.toBuilder()
            .withDomainEventPublisher(mqDomainEventPublisher)
            .withHandlerExecutor(handlerExecutor)
            .withExecutionContextDetector(executionContextDetector)
            .withExecutionContextProcessor(executionContextProcessor)
            .withReceivingDomainEventHandler(new DummyReceivingDomainEventHandler())
            .build();
    }

    abstract protected MqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper);
    abstract protected MqDomainEventConsumer provideMqDomainEventConsumer(
        ObjectMapper objectMapper,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
        );

    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }

    public MqDomainEventPublisher getMqDomainEventPublisher() {
        return mqDomainEventPublisher;
    }

    public MqDomainEventConsumer getMqDomainEventConsumer() {
        return mqDomainEventConsumer;
    }

    public void shutDown(){
        mqDomainEventPublisher.closeAll();
        mqDomainEventConsumer.closeAll();
    }
}

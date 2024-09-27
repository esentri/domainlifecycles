package io.domainlifecycles.events.mq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

public abstract class AbstractMqChannelFactory implements ChannelFactory {

    private final ServiceProvider serviceProvider;
    private final ClassProvider classProvider;
    private final HandlerExecutor handlerExecutor;
    private final ObjectMapper objectMapper;

    public AbstractMqChannelFactory(ServiceProvider serviceProvider,
                                    ClassProvider classProvider,
                                    HandlerExecutor handlerExecutor,
                                    ObjectMapper objectMapper) {
        this.serviceProvider = serviceProvider;
        this.classProvider = classProvider;
        this.handlerExecutor = handlerExecutor;
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper is required!");
    }

    public AbstractMqChannelFactory(ObjectMapper objectMapper){
        this(null, null, null, objectMapper);
    }

    @Override
    public AbstractMqConsumingChannel consumeOnlyChannel(String channelName) {
        return new AbstractMqConsumingChannel(channelName, consumingConfiguration());
    }

    @Override
    public AbstractMqPublishingChannel publishOnlyChannel(String channelName) {
        return new AbstractMqPublishingChannel(channelName, publishingConfiguration());
    }

    @Override
    public AbstractMqProcessingChannel processingChannel(String channelName) {
        return new AbstractMqProcessingChannel(channelName, consumingConfiguration(), publishingConfiguration());
    }

    private AbstractMqConsumingConfiguration consumingConfiguration(){
        Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(Objects.requireNonNull(handlerExecutor,"A HandlerExecutor is required!"));

        return new AbstractMqConsumingConfiguration(provideMqDomainEventConsumer(
            this.objectMapper,
            executionContextDetector,
            executionContextProcessor,
            Objects.requireNonNull(this.classProvider, "A ClassProvider is required!")
        ));
    }

    private AbstractMqPublishingConfiguration publishingConfiguration(){
        return new AbstractMqPublishingConfiguration(provideMqDomainEventPublisher(this.objectMapper));
    }

    abstract protected MqDomainEventPublisher provideMqDomainEventPublisher(
        ObjectMapper objectMapper
    );

    abstract protected MqDomainEventConsumer provideMqDomainEventConsumer(
        ObjectMapper objectMapper,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
    );


}

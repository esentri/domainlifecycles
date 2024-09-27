package io.domainlifecycles.events.activemq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.activemq.consume.ActiveMqDomainEventConsumer;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.mq.api.AbstractMqChannelFactory;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

public class ActiveMqChannelFactory extends AbstractMqChannelFactory {

    private String virtualTopicPrefix = "VirtualTopic.";
    private String virtualTopicConsumerPrefix = "Consumer.";

    private final ConnectionFactory connectionFactory;
    private long receiveTimeoutMs = 100;

    public ActiveMqChannelFactory(ConnectionFactory connectionFactory,
                                  ServiceProvider serviceProvider,
                                  ClassProvider classProvider,
                                  HandlerExecutor handlerExecutor,
                                  ObjectMapper objectMapper,
                                  long receiveTimeoutMs) {
        this(connectionFactory, serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.receiveTimeoutMs = receiveTimeoutMs;
    }

    public ActiveMqChannelFactory(ConnectionFactory connectionFactory,
                                  ServiceProvider serviceProvider,
                                  ClassProvider classProvider,
                                  HandlerExecutor handlerExecutor,
                                  ObjectMapper objectMapper){
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    public ActiveMqChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        String virtualTopicPrefix,
        String virtualTopicConsumerPrefix
    ){
        this(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            objectMapper);
        this.virtualTopicPrefix = virtualTopicPrefix;
        this.virtualTopicConsumerPrefix = virtualTopicConsumerPrefix;
    }

    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        return new ActiveMqDomainEventPublisher(connectionFactory, objectMapper, virtualTopicPrefix);
    }

    @Override
    protected ActiveMqDomainEventConsumer provideMqDomainEventConsumer(
        ObjectMapper objectMapper,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
    ) {
        return new ActiveMqDomainEventConsumer(
            connectionFactory,
            objectMapper,
            executionContextDetector,
            executionContextProcessor,
            classProvider,
            this.virtualTopicConsumerPrefix,
            this.virtualTopicPrefix,
            this.receiveTimeoutMs
        );
    }

    public String getVirtualTopicPrefix() {
        return virtualTopicPrefix;
    }

    public String getVirtualTopicConsumerPrefix() {
        return virtualTopicConsumerPrefix;
    }
}

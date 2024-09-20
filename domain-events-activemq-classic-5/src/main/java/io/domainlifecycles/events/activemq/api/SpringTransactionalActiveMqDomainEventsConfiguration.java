package io.domainlifecycles.events.activemq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.activemq.consume.ActiveMqDomainEventConsumer;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.activemq.publish.SpringTransactionalActiveMqDomainEventPublisher;
import io.domainlifecycles.events.mq.api.AbstractMqDomainEventsConfiguration;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

public class SpringTransactionalActiveMqDomainEventsConfiguration extends AbstractMqDomainEventsConfiguration {

    private final ConnectionFactory connectionFactory;

    private String virtualTopicPrefix = "VirtualTopic.";
    private String virtualTopicConsumerPrefix = "Consumer.";

    private long receiveTimeoutMs = 100;

    public SpringTransactionalActiveMqDomainEventsConfiguration(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        long receiveTimeoutMs){
        this(connectionFactory, serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.receiveTimeoutMs = receiveTimeoutMs;
    }

    public SpringTransactionalActiveMqDomainEventsConfiguration(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory required!");
        initialize();
    }

    public SpringTransactionalActiveMqDomainEventsConfiguration(
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
    protected SpringTransactionalActiveMqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        var activeMqDomainEventPublisher = new ActiveMqDomainEventPublisher(connectionFactory, objectMapper, this.virtualTopicPrefix);
        return new SpringTransactionalActiveMqDomainEventPublisher(true, activeMqDomainEventPublisher);
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
}

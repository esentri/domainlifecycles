package io.domainlifecycles.events.jakarta.jms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.events.jakarta.jms.consume.JakartaJmsDomainEventConsumer;
import io.domainlifecycles.events.jakarta.jms.publish.JakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.mq.api.AbstractMqChannelFactory;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

public class JakartaJmsChannelFactory extends AbstractMqChannelFactory {

    private final ConnectionFactory connectionFactory;
    private long receiveTimeoutMs = 100;

    public JakartaJmsChannelFactory(ConnectionFactory connectionFactory,
                                    ServiceProvider serviceProvider,
                                    ClassProvider classProvider,
                                    HandlerExecutor handlerExecutor,
                                    ObjectMapper objectMapper,
                                    long receiveTimeoutMs) {
        this(connectionFactory, serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.receiveTimeoutMs = receiveTimeoutMs;
    }

    public JakartaJmsChannelFactory(ConnectionFactory connectionFactory,
                                    ServiceProvider serviceProvider,
                                    ClassProvider classProvider,
                                    HandlerExecutor handlerExecutor,
                                    ObjectMapper objectMapper){
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "A ConnectionFactory is required!");
    }

    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
         return new JakartaJmsDomainEventPublisher(connectionFactory, objectMapper);
    }

    @Override
    protected MqDomainEventConsumer provideMqDomainEventConsumer(ObjectMapper objectMapper, ExecutionContextDetector executionContextDetector, ExecutionContextProcessor executionContextProcessor, ClassProvider classProvider) {
        return new JakartaJmsDomainEventConsumer(
            connectionFactory,
            objectMapper,
            executionContextDetector,
            executionContextProcessor,
            classProvider,
            this.receiveTimeoutMs
        );
    }
}

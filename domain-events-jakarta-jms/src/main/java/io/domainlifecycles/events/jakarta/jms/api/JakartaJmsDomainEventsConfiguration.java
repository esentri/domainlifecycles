package io.domainlifecycles.events.jakarta.jms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.jakarta.jms.consume.JakartaJmsDomainEventConsumer;
import io.domainlifecycles.events.jakarta.jms.publish.JakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.mq.api.AbstractMqDomainEventsConfiguration;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

import java.util.Objects;

public class JakartaJmsDomainEventsConfiguration extends AbstractMqDomainEventsConfiguration {

    private final ConnectionFactory connectionFactory;

    public JakartaJmsDomainEventsConfiguration(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory required!");
        initialize();
    }

    @Override
    protected JakartaJmsDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        return new JakartaJmsDomainEventPublisher(connectionFactory, objectMapper);
    }

    @Override
    protected JakartaJmsDomainEventConsumer provideMqDomainEventConsumer(
        ObjectMapper objectMapper,
        ExecutionContextDetector executionContextDetector,
        ExecutionContextProcessor executionContextProcessor,
        ClassProvider classProvider
    ) {
        return new JakartaJmsDomainEventConsumer(
            connectionFactory,
            objectMapper,
            executionContextDetector,
            executionContextProcessor,
            classProvider
        );
    }
}

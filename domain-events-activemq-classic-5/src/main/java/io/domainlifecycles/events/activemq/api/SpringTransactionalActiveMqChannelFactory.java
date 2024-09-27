package io.domainlifecycles.events.activemq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.activemq.publish.ActiveMqDomainEventPublisher;
import io.domainlifecycles.events.activemq.publish.SpringTransactionalActiveMqDomainEventPublisher;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

public class SpringTransactionalActiveMqChannelFactory extends ActiveMqChannelFactory{

    public SpringTransactionalActiveMqChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        long receiveTimeoutMs
    ) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            objectMapper,
            receiveTimeoutMs
        );
    }

    public SpringTransactionalActiveMqChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper
    ) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            objectMapper
        );
    }

    public SpringTransactionalActiveMqChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        String virtualTopicPrefix,
        String virtualTopicConsumerPrefix
    ) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            objectMapper,
            virtualTopicPrefix,
            virtualTopicConsumerPrefix
        );
    }

    @Override
    protected MqDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        var activeMqDomainEventPublisher = (ActiveMqDomainEventPublisher) super.provideMqDomainEventPublisher(objectMapper);
        return new SpringTransactionalActiveMqDomainEventPublisher(true, activeMqDomainEventPublisher);
    }
}

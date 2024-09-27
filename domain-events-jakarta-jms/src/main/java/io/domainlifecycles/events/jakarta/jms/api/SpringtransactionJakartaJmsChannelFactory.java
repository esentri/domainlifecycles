package io.domainlifecycles.events.jakarta.jms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.jakarta.jms.publish.JakartaJmsDomainEventPublisher;
import io.domainlifecycles.events.jakarta.jms.publish.SpringTransactionalJakartaJmsDomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;

public class SpringtransactionJakartaJmsChannelFactory extends JakartaJmsChannelFactory{

    public SpringtransactionJakartaJmsChannelFactory(
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

    public SpringtransactionJakartaJmsChannelFactory(
        ConnectionFactory connectionFactory,
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper) {
        super(
            connectionFactory,
            serviceProvider,
            classProvider,
            handlerExecutor,
            objectMapper
        );
    }

    @Override
    protected SpringTransactionalJakartaJmsDomainEventPublisher provideMqDomainEventPublisher(ObjectMapper objectMapper) {
        var jakartaJmsDomainEventPublisher = (JakartaJmsDomainEventPublisher) super.provideMqDomainEventPublisher(objectMapper);
        return new SpringTransactionalJakartaJmsDomainEventPublisher(true, jakartaJmsDomainEventPublisher);
    }
}

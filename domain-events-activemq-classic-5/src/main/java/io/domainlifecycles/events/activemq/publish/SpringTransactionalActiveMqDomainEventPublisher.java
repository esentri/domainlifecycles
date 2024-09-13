package io.domainlifecycles.events.activemq.publish;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.mq.publish.AbstractSpringTransactionalMqDomainEventSender;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import jakarta.jms.Topic;

public class SpringTransactionalActiveMqDomainEventPublisher extends AbstractSpringTransactionalMqDomainEventSender<Topic> implements MqDomainEventPublisher {

    private final ActiveMqDomainEventPublisher activeMqDomainEventPublisher;

    public SpringTransactionalActiveMqDomainEventPublisher(boolean afterCommit, ActiveMqDomainEventPublisher activeMqDomainEventPublisher) {
        super(afterCommit, activeMqDomainEventPublisher);
        this.activeMqDomainEventPublisher = activeMqDomainEventPublisher;
    }

    @Override
    protected void send(DomainEvent domainEvent) {
        activeMqDomainEventPublisher.publish(domainEvent);
    }

    @Override
    public void closeAll() {
        activeMqDomainEventPublisher.closeAll();
    }
}

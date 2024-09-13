package io.domainlifecycles.events.jakarta.jms.publish;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.mq.publish.AbstractSpringTransactionalMqDomainEventSender;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import jakarta.jms.Topic;

public class SpringTransactionalJakartaJmsDomainEventPublisher extends AbstractSpringTransactionalMqDomainEventSender<Topic> implements MqDomainEventPublisher {

    private final JakartaJmsDomainEventPublisher jakartaJmsDomainEventPublisher;

    public SpringTransactionalJakartaJmsDomainEventPublisher(boolean afterCommit, JakartaJmsDomainEventPublisher jakartaJmsDomainEventPublisher) {
        super(afterCommit, jakartaJmsDomainEventPublisher);
        this.jakartaJmsDomainEventPublisher = jakartaJmsDomainEventPublisher;
    }

    @Override
    protected void send(DomainEvent domainEvent) {
        jakartaJmsDomainEventPublisher.publish(domainEvent);
    }

    @Override
    public void closeAll() {
        jakartaJmsDomainEventPublisher.closeAll();
    }
}

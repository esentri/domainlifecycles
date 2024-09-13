package io.domainlifecycles.events.mq.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.spring.publish.AbstractSpringTransactionalDomainEventPublisher;

public abstract class AbstractSpringTransactionalMqDomainEventSender<TOPIC> extends AbstractSpringTransactionalDomainEventPublisher {

    private final AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher;

    public AbstractSpringTransactionalMqDomainEventSender(
        boolean afterCommit,
        AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher
    ) {
        super(afterCommit);
        this.abstractMqDomainEventPublisher = abstractMqDomainEventPublisher;
    }

    @Override
    protected void send(DomainEvent domainEvent) {
        abstractMqDomainEventPublisher.publish(domainEvent);
    }
}

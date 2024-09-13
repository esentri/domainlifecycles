package io.domainlifecycles.events.publish;

import io.domainlifecycles.domain.types.DomainEvent;

public abstract class AbstractTransactionalDomainEventPublisher implements DomainEventPublisher{

    abstract protected void send(DomainEvent domainEvent);

}

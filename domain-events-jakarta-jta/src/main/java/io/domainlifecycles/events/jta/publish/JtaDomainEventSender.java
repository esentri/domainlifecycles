package io.domainlifecycles.events.jta.publish;

import io.domainlifecycles.domain.types.DomainEvent;

public interface JtaDomainEventSender {

    void send(DomainEvent domainEvent);
}

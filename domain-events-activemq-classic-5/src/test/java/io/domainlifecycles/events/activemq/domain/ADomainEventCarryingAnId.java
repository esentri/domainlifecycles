package io.domainlifecycles.events.activemq.domain;

import io.domainlifecycles.domain.types.DomainEvent;

public record ADomainEventCarryingAnId(AnIdentity identity) implements DomainEvent {
}

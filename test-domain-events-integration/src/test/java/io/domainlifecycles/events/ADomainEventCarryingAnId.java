package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record ADomainEventCarryingAnId(AnIdentity identity) implements DomainEvent {
}

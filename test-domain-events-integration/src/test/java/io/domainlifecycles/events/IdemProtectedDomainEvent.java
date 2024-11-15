package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record IdemProtectedDomainEvent(String id) implements DomainEvent {
}

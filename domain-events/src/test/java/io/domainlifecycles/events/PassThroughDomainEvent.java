package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record PassThroughDomainEvent(String message) implements DomainEvent {
}

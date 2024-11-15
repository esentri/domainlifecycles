package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record UnreceivedDomainEvent(String message) implements DomainEvent {
}

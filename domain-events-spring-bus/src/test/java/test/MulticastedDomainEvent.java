package test;

import io.domainlifecycles.domain.types.DomainEvent;

public record MulticastedDomainEvent(String message) implements DomainEvent {
}

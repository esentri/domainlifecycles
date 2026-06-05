package test;

import io.domainlifecycles.domain.types.DomainEvent;

public record A2ndDomainEvent(String message) implements DomainEvent {
}

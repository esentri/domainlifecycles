package io.domainlifecycles.springboot.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record TestEvent(String message) implements DomainEvent {
}

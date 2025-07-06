package io.domainlifecycles.autoconfig.model.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record ADomainEvent(String message) implements DomainEvent {
}

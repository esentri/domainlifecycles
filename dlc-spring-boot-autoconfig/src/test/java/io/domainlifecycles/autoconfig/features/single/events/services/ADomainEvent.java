package io.domainlifecycles.autoconfig.features.single.events.services;

import io.domainlifecycles.domain.types.DomainEvent;

public record ADomainEvent(String message) implements DomainEvent {
}

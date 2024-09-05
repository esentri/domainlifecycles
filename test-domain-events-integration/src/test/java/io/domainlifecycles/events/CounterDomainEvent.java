package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

public record CounterDomainEvent(String action) implements DomainEvent {
}

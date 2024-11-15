package io.domainlifecycles.events.springoutbox;

import io.domainlifecycles.domain.types.DomainEvent;

public record OutboxTestEvent(String message) implements DomainEvent {
}

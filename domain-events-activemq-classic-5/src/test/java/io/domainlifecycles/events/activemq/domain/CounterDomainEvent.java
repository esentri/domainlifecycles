package io.domainlifecycles.events.activemq.domain;

import io.domainlifecycles.domain.types.DomainEvent;

public record CounterDomainEvent(String action) implements DomainEvent {
}

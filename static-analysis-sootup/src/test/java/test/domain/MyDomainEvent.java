package test.domain;

import io.domainlifecycles.domain.types.DomainEvent;

public record MyDomainEvent(MyAggregateRoot.Id id) implements DomainEvent {
}

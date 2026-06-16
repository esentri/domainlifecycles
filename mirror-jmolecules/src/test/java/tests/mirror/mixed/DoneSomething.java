package tests.mirror.mixed;


import io.domainlifecycles.domain.types.DomainEvent;

public record DoneSomething(AnAggregateRoot.AnAggregateRootId id) implements DomainEvent {
}

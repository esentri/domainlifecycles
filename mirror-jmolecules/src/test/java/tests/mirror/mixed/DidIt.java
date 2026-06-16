package tests.mirror.mixed;

import io.domainlifecycles.domain.types.DomainEvent;

public record DidIt(AnAggregateRoot.AnAggregateRootId id) implements DomainEvent {
}

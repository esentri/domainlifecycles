package testdomain.modulith;

import io.domainlifecycles.domain.types.DomainEvent;

public record MulticastedDomainEvent(String message) implements DomainEvent {
}

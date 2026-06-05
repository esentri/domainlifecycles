package testdomain.general;

import io.domainlifecycles.domain.types.DomainEvent;

public record ADomainEvent(String message) implements DomainEvent {
}

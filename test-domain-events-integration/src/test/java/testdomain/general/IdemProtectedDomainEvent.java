package testdomain.general;

import io.domainlifecycles.domain.types.DomainEvent;

public record IdemProtectedDomainEvent(String id) implements DomainEvent {
}

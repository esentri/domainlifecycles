package tests.mirror.frameworklisteners;

import io.domainlifecycles.domain.types.DomainEvent;

public record OrderPlaced(String orderNumber) implements DomainEvent {
}

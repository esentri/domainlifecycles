package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.DomainEvent;

public record OrderPlaced(OrderId orderId) implements DomainEvent {
}

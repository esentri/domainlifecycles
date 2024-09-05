package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.DomainEvent;

@FunctionalInterface
public interface IdempotencyFunction {

    String uniqueIdentifier(DomainEvent domainEvent);

}

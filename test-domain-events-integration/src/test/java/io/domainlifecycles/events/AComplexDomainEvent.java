package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.List;

public record AComplexDomainEvent(
    AnIdentity id,
    AValueObject valueObject,
    List<AnIdentity> ids,
    List<AValueObject> valueObjects
) implements DomainEvent {
}

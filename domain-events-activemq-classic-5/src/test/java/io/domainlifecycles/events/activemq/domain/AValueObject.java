package io.domainlifecycles.events.activemq.domain;

import io.domainlifecycles.domain.types.ValueObject;
import lombok.Builder;

@Builder
public record AValueObject(
    String value,
    Long valueLong,
    AnIdentity anId
) implements ValueObject {
}

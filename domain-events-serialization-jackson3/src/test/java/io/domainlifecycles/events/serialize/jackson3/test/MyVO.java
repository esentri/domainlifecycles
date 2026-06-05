package io.domainlifecycles.events.serialize.jackson3.test;

import io.domainlifecycles.domain.types.ValueObject;
import lombok.Builder;

@Builder
public record MyVO(String test) implements ValueObject {
}

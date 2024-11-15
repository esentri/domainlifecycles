package io.domainlifecycles.builder.helper;

import lombok.Builder;
import io.domainlifecycles.domain.types.ValueObject;

@Builder
public record TestValueObject(String first, Long second) implements ValueObject {
}

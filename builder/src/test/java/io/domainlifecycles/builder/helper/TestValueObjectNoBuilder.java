package io.domainlifecycles.builder.helper;

import io.domainlifecycles.domain.types.ValueObject;


public record TestValueObjectNoBuilder(String first, Long second) implements ValueObject {
}

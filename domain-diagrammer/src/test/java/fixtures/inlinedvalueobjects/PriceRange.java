package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.ValueObject;

// Two fields, both value objects of two fields.
public record PriceRange(Money from, Money to) implements ValueObject {
}

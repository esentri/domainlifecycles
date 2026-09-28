package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.ValueObject;

// One field.
public record Name(String value) implements ValueObject {
}

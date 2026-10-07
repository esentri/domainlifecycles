package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.ValueObject;

// One field, a value object of three fields.
public record Location(Address address) implements ValueObject {
}

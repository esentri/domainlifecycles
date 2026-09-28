package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.ValueObject;

// Three fields.
public record Address(String street, String zipCode, String city) implements ValueObject {
}

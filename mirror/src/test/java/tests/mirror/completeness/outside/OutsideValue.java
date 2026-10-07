package tests.mirror.completeness.outside;

import io.domainlifecycles.domain.types.ValueObject;

// a domain type outside the scanned packages
public record OutsideValue(String value) implements ValueObject {
}

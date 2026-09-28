package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.ValueObject;

import java.math.BigDecimal;

// Two fields, one of them an enum.
public record Money(BigDecimal amount, Currency currency) implements ValueObject {
}

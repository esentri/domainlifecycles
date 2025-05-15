package helper;

import io.domainlifecycles.domain.types.ValueObject;
import lombok.Builder;

@Builder
public record TestValueObject(String first, Long second) implements ValueObject {
}

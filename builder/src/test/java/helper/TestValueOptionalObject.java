package helper;

import io.domainlifecycles.domain.types.ValueObject;
import lombok.Builder;

import java.util.Optional;

@Builder
public record TestValueOptionalObject(Optional<String> first, Long second) implements ValueObject {
}

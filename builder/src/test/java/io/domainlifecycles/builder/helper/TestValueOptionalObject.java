package io.domainlifecycles.builder.helper;

import io.domainlifecycles.domain.types.ValueObject;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.Builder;

@Builder
public record TestValueOptionalObject(Optional<String> first, Long second) implements ValueObject {
}

package tests.shared.persistence.domain.valueobjectsNested;

import lombok.Builder;
import io.domainlifecycles.domain.types.ValueObject;


@Builder(setterPrefix = "set")
public record NestedSimpleVo(SimpleVo nested) implements ValueObject {
}

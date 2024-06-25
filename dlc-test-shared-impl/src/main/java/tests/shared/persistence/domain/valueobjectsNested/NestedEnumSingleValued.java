package tests.shared.persistence.domain.valueobjectsNested;

import lombok.Builder;
import nitrox.dlc.domain.types.ValueObject;

@Builder(setterPrefix = "set")
public record NestedEnumSingleValued(MyEnum enumValue) implements ValueObject {
}

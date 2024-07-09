package tests.shared.persistence.domain.valueobjectsNested;

import lombok.Builder;
import io.domainlifecycles.domain.types.ValueObject;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

@Builder(setterPrefix = "set")
public record NestedId(VoAggregateNestedId idRef) implements ValueObject {
}

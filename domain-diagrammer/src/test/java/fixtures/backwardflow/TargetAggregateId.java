package fixtures.backwardflow;

import io.domainlifecycles.domain.types.Identity;

public record TargetAggregateId(Long value) implements Identity<Long> {
}

package fixtures.flowmethods;

import io.domainlifecycles.domain.types.Identity;

public record OrderLineId(Long value) implements Identity<Long> {
}

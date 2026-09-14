package fixtures.topcommand;

import io.domainlifecycles.domain.types.Identity;

public record TopAggregateId(Long value) implements Identity<Long> {
}

package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.Identity;

public record TestAggregateId(Long value) implements Identity<Long> {
}

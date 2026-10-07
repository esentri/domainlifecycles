package io.domainlifecycles.staticanalysis.factory;

import io.domainlifecycles.domain.types.Identity;

public record OrderLineId(Long value) implements Identity<Long> {
}

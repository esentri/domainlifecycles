package io.domainlifecycles.events.serialize.jackson3.test;

import io.domainlifecycles.domain.types.Identity;

public record MyId(Long value) implements Identity<Long> {
}

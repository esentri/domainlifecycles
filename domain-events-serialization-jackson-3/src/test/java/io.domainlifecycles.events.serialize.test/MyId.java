package io.domainlifecycles.events.serialize.test;

import io.domainlifecycles.domain.types.Identity;

public record MyId(Long value) implements Identity<Long> {
}

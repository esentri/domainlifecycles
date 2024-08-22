package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.Identity;

public record AnIdentity(Long value) implements Identity<Long> {
}

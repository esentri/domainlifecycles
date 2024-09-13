package io.domainlifecycles.events.activemq.domain;

import io.domainlifecycles.domain.types.Identity;

public record AnIdentity(Long value) implements Identity<Long> {
}

package tests.factory;

import io.domainlifecycles.domain.types.Identity;

public record OrderId(Long value) implements Identity<Long> {
}

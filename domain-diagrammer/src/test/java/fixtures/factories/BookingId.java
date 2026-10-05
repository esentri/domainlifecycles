package fixtures.factories;

import io.domainlifecycles.domain.types.Identity;

public record BookingId(Long value) implements Identity<Long> {
}

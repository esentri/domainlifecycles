package fixtures.factories;

import io.domainlifecycles.domain.types.Identity;

public record AppointmentId(Long value) implements Identity<Long> {
}

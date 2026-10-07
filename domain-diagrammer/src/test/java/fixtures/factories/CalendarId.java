package fixtures.factories;

import io.domainlifecycles.domain.types.Identity;

public record CalendarId(Long value) implements Identity<Long> {
}

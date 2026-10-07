package tests.mirror.factory;

import io.domainlifecycles.domain.types.Identity;

import java.util.UUID;

public record CalendarId(UUID value) implements Identity<UUID> {
}

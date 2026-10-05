package tests.mirror.factory;

import io.domainlifecycles.domain.types.Identity;

import java.util.UUID;

public record AppointmentId(UUID value) implements Identity<UUID> {
}

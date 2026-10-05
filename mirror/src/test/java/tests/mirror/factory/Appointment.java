package tests.mirror.factory;

import io.domainlifecycles.domain.types.Entity;

public class Appointment implements Entity<AppointmentId> {

    private final AppointmentId id;
    private final String title;

    public Appointment(AppointmentId id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public AppointmentId id() {
        return id;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}

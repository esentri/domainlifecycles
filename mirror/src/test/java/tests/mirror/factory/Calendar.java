package tests.mirror.factory;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.FactoryMethod;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public class Calendar implements AggregateRoot<CalendarId> {

    private final CalendarId id;
    private final List<Appointment> appointments;

    @FactoryMethod
    public static Calendar open(CalendarId id) {
        return new Calendar(id, new ArrayList<>());
    }

    @FactoryMethod
    public Appointment planAppointment(String title) {
        var appointment = new Appointment(new AppointmentId(UUID.randomUUID()), title);
        appointments.add(appointment);
        return appointment;
    }

    public Appointment firstAppointment() {
        return appointments.get(0);
    }

    @Override
    public CalendarId id() {
        return id;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}

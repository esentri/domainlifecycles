package fixtures.factories;

import io.domainlifecycles.domain.types.FactoryMethod;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AggregateRootBase<CalendarId> {

    private final CalendarId id;

    private final List<Appointment> appointments = new ArrayList<>();

    public Calendar(CalendarId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    // creates its own instances: no relationship
    @FactoryMethod
    public static Calendar open(CalendarId id) {
        return new Calendar(id, 0);
    }

    @FactoryMethod
    public Appointment planAppointment(String title) {
        var appointment = new Appointment(new AppointmentId((long) appointments.size()), 0);
        appointments.add(appointment);
        return appointment;
    }

    public Appointment firstAppointment() {
        return appointments.get(0);
    }
}

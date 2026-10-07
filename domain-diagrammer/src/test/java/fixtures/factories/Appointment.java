package fixtures.factories;

import io.domainlifecycles.domain.types.FactoryMethod;
import io.domainlifecycles.domain.types.base.EntityBase;

// an entity of the Calendar aggregate
public class Appointment extends EntityBase<AppointmentId> {

    private final AppointmentId id;

    public Appointment(AppointmentId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    // creates another aggregate: a relationship between the frames
    @FactoryMethod
    public Booking book() {
        return new Booking(new BookingId(3L), 0);
    }
}

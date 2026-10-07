package fixtures.factories;

import io.domainlifecycles.domain.types.Factory;

public class CalendarFactory implements Factory {

    private final CalendarIdGenerator idGenerator = new CalendarIdGenerator();

    public Calendar create() {
        return new Calendar(idGenerator.next(), 0);
    }

    public Calendar createFor(String owner) {
        return create();
    }

    public Calendar copy(Calendar calendar) {
        return create();
    }

    public Calendar createEmpty() {
        return create();
    }

    public Booking createBooking() {
        return new Booking(new BookingId(1L), 0);
    }
}

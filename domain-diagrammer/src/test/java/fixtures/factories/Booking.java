package fixtures.factories;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class Booking extends AggregateRootBase<BookingId> {

    private final BookingId id;

    public Booking(BookingId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}

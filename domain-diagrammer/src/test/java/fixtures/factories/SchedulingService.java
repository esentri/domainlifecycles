package fixtures.factories;

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.FactoryMethod;

public class SchedulingService implements DomainService {

    @FactoryMethod
    public Booking book(Calendar calendar) {
        return new Booking(new BookingId(2L), 0);
    }

    public void cancel(Booking booking) {
    }
}

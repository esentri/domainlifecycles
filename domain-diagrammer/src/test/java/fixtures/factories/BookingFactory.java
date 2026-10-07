package fixtures.factories;

import io.domainlifecycles.domain.types.Factory;

// a factory known by its interface
public interface BookingFactory extends Factory {

    Booking create();
}

package fixtures.factories;

public class BookingFactoryImpl implements BookingFactory {

    @Override
    public Booking create() {
        return new Booking(new BookingId(4L), 0);
    }
}

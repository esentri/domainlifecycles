package io.domainlifecycles.jooq.persistence.tests.inheritance;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.inheritance.Bike;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.VehicleId;

import java.util.Optional;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleRepository_ITest extends BasePersistence_ITest {

    private static VehicleRepository vehicleRepository;

    @BeforeAll
    public void init() {
        vehicleRepository = new VehicleRepository(
            persistenceConfiguration.dslContext,
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    @Test
    public void testInsertCar() {
        //given
        Car car = Car.builder()
            .setId(new VehicleId(1l))
            .setBrand(Car.Brand.AUDI)
            .setLengthCm(350)
            .build();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Car inserted = (Car) vehicleRepository.insert(car);
        //then
        Optional<Car> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Car) v);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateCar() {
        //given
        Car car = Car.builder()
            .setId(new VehicleId(1l))
            .setBrand(Car.Brand.AUDI)
            .setLengthCm(350)
            .build();
        Car inserted = (Car) vehicleRepository.insert(car);
        Car insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setLengthCm(550);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Car updated = (Car) vehicleRepository.update(insertedCopy);
        //then
        Optional<Car> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Car) v);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getLengthCm()).isEqualTo(insertedCopy.getLengthCm());
    }

    @Test
    public void testDeleteCar() {
        //given
        Car car = Car.builder()
            .setId(new VehicleId(1l))
            .setBrand(Car.Brand.AUDI)
            .setLengthCm(350)
            .build();
        Car inserted = (Car) vehicleRepository.insert(car);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<Car> deleted = vehicleRepository.deleteById(inserted.getId()).map(v -> (Car) v);
        //then
        Optional<Car> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Car) v);
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertBike() {
        //given
        Bike bike = Bike.builder()
            .setId(new VehicleId(1l))
            .setGears(18)
            .setLengthCm(150)
            .build();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Bike inserted = (Bike) vehicleRepository.insert(bike);
        //then
        Optional<Bike> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Bike) v);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateBike() {
        //given
        Bike bike = Bike.builder()
            .setId(new VehicleId(1l))
            .setGears(18)
            .setLengthCm(150)
            .build();
        Bike inserted = (Bike) vehicleRepository.insert(bike);
        Bike insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setLengthCm(550);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Bike updated = (Bike) vehicleRepository.update(insertedCopy);
        //then
        Optional<Bike> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Bike) v);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getLengthCm()).isEqualTo(insertedCopy.getLengthCm());
    }

    @Test
    public void testDeleteBike() {
        //given
        Bike bike = Bike.builder()
            .setId(new VehicleId(1l))
            .setGears(18)
            .setLengthCm(150)
            .build();
        Bike inserted = (Bike) vehicleRepository.insert(bike);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<Bike> deleted = vehicleRepository.deleteById(inserted.getId()).map(v -> (Bike) v);
        //then
        Optional<Bike> found = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Bike) v);
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testFindAll() {
        //given
        Bike bike = Bike.builder()
            .setId(new VehicleId(1l))
            .setGears(18)
            .setLengthCm(150)
            .build();
        Car car = Car.builder()
            .setId(new VehicleId(2l))
            .setBrand(Car.Brand.AUDI)
            .setLengthCm(350)
            .build();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Bike insertedBike = (Bike) vehicleRepository.insert(bike);
        Car insertedCar = (Car) vehicleRepository.insert(car);
        //then
        Optional<Bike> foundBike = vehicleRepository.findResultById(new VehicleId(1l)).resultValue().map(v -> (Bike) v);
        persistenceEventTestHelper.assertFoundWithResult(foundBike, insertedBike);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedBike);
        Optional<Car> foundCar = vehicleRepository.findResultById(new VehicleId(2l)).resultValue().map(v -> (Car) v);
        persistenceEventTestHelper.assertFoundWithResult(foundCar, insertedCar);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedCar);
        persistenceEventTestHelper.assertEvents();
        var vehicles = vehicleRepository.findAll().collect(Collectors.toList());
        Assertions.assertThat(vehicles).hasSize(2);
        var bikes = vehicleRepository.findAllBikes();
        var cars = vehicleRepository.findAllCars();
        Assertions.assertThat(bikes).hasSize(1);
        Assertions.assertThat(cars).hasSize(1);

    }

}


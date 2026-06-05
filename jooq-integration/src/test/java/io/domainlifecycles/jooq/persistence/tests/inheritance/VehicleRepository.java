package io.domainlifecycles.jooq.persistence.tests.inheritance;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.jooq.Tables;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import tests.shared.persistence.domain.inheritance.Bike;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.Vehicle;
import tests.shared.persistence.domain.inheritance.VehicleId;

import java.util.stream.Stream;

@Slf4j
public class VehicleRepository extends JooqAggregateRepository<Vehicle, VehicleId> {

    public VehicleRepository(DSLContext dslContext, JooqDomainPersistenceProvider domainPersistenceProvider,
                             PersistenceEventPublisher persistenceEventPublisher) {
        super(Vehicle.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }

    public Stream<Vehicle> findAll() {

        return dslContext.select().from(Tables.VEHICLE)
            .stream()
            .map(r -> super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }

    public Stream<Car> findAllCars() {

        return dslContext.select()
            .from(Tables.VEHICLE)
            .where(Tables.VEHICLE.TYPE.eq(Car.class.getSimpleName()))
            .stream()
            .map(r -> (Car) super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }

    public Stream<Bike> findAllBikes() {

        return dslContext.select()
            .from(Tables.VEHICLE)
            .where(Tables.VEHICLE.TYPE.eq(Bike.class.getSimpleName()))
            .stream()
            .map(r -> (Bike) super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }
}

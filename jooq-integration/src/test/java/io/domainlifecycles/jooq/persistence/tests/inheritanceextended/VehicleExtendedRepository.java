package io.domainlifecycles.jooq.persistence.tests.inheritanceextended;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.Tables;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import tests.shared.persistence.domain.inheritanceExtended.BikeWithComponents;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtended;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtendedId;

import java.util.stream.Stream;


@Slf4j
public class VehicleExtendedRepository extends JooqAggregateRepository<VehicleExtended, VehicleExtendedId> {

    public VehicleExtendedRepository(DSLContext dslContext,
                                     JooqDomainPersistenceProvider domainPersistenceProvider,
                                     PersistenceEventPublisher persistenceEventPublisher) {
        super(VehicleExtended.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }

    public Stream<VehicleExtended> findAll() {

        return dslContext.select().from(Tables.VEHICLE_EXTENDED)
            .stream()
            .map(r -> super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }

    public Stream<CarWithEngine> findAllCars() {

        return dslContext.select()
            .from(Tables.VEHICLE_EXTENDED)
            .where(Tables.VEHICLE_EXTENDED.TYPE.eq(CarWithEngine.class.getSimpleName()))
            .stream()
            .map(r -> (CarWithEngine) super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }

    public Stream<BikeWithComponents> findAllBikes() {

        return dslContext.select()
            .from(Tables.VEHICLE_EXTENDED)
            .where(Tables.VEHICLE_EXTENDED.TYPE.eq(BikeWithComponents.class.getSimpleName()))
            .stream()
            .map(r -> (BikeWithComponents) super.getFetcher().fetchDeep((UpdatableRecord<?>) r).resultValue().get());
    }
}

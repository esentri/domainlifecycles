/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.jooq.persistence.tests.inheritance;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.Tables;
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

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

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

package nitrox.dlc.jooq.persistence.tests.inheritanceextended;

import nitrox.dlc.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.inheritanceExtended.BikeComponent;
import tests.shared.persistence.domain.inheritanceExtended.BikeWithComponents;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.Engine;
import tests.shared.persistence.domain.inheritanceExtended.EngineId;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtendedId;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleExtendedRepository_ITest extends BasePersistence_ITest {
    
    private VehicleExtendedRepository vehicleRepository;

    @BeforeAll
    public void init(){
        vehicleRepository = new VehicleExtendedRepository(
            persistenceConfiguration.dslContext,
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
            );
    }
    @Test
    public void testInsertCar() {
        //given
        CarWithEngine car = CarWithEngine.builder()
            .setId(new VehicleExtendedId(1l))
            .setBrand(CarWithEngine.Brand.AUDI)
            .setLengthCm(350)
            .setEngine(Engine.builder()
                .setId(new EngineId(1l))
                .setPs(400)
                .setType(Engine.EngineType.ELECTRIC)
                .build())
            .build();
         persistenceEventTestHelper.resetEventsCaught();
        //when
        CarWithEngine inserted = (CarWithEngine) vehicleRepository.insert(car);
        //then
        Optional<CarWithEngine> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v -> (CarWithEngine)v);
         persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getEngine());
         persistenceEventTestHelper.assertEvents();

    }


    @Test
    public void testUpdateCar() {
        //given
        CarWithEngine car = CarWithEngine.builder()
            .setId(new VehicleExtendedId(1l))
            .setBrand(CarWithEngine.Brand.AUDI)
            .setLengthCm(350)
            .setEngine(Engine.builder()
                .setId(new EngineId(1l))
                .setPs(400)
                .setType(Engine.EngineType.ELECTRIC)
                .build())
            .build();
        CarWithEngine inserted = (CarWithEngine) vehicleRepository.insert(car);
        CarWithEngine insertedCopy =  persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setLengthCm(550);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        CarWithEngine updated = (CarWithEngine) vehicleRepository.update(insertedCopy);
        //then
        Optional<CarWithEngine> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v->(CarWithEngine)v);
         persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
         persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getLengthCm()).isEqualTo(insertedCopy.getLengthCm());
    }

    @Test
    public void testDeleteCar() {
        //given
        CarWithEngine car = CarWithEngine.builder()
            .setId(new VehicleExtendedId(1l))
            .setBrand(CarWithEngine.Brand.AUDI)
            .setLengthCm(350)
            .setEngine(Engine.builder()
                .setId(new EngineId(1l))
                .setPs(400)
                .setType(Engine.EngineType.ELECTRIC)
                .build())
            .build();
        CarWithEngine inserted = (CarWithEngine) vehicleRepository.insert(car);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<CarWithEngine> deleted = vehicleRepository.deleteById(inserted.getId()).map(v -> (CarWithEngine) v);
        //then
        Optional<CarWithEngine> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v -> (CarWithEngine) v);
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
         persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getEngine());
         persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertBike() {
        //given
        var components = new ArrayList<BikeComponent>();
        components.add(BikeComponent.builder()
            .setManufacturer("Shimano")
            .setType(BikeComponent.BikeComponentType.BREAKS)
            .build());
        components.add(BikeComponent.builder()
            .setManufacturer("Canyon")
            .setType(BikeComponent.BikeComponentType.FRAME)
            .build());
        BikeWithComponents bike = BikeWithComponents.builder()
            .setId(new VehicleExtendedId(1l))
            .setGears(18)
            .setLengthCm(150)
            .setBikeComponents(components)
            .build();
         persistenceEventTestHelper.resetEventsCaught();
        //when
        BikeWithComponents inserted = (BikeWithComponents) vehicleRepository.insert(bike);
        //then
        Optional<BikeWithComponents> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v -> (BikeWithComponents) v);
         persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getBikeComponents().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getBikeComponents().get(1), inserted);
         persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateBike() {
        //given
        var components = new ArrayList<BikeComponent>();
        components.add(BikeComponent.builder()
            .setManufacturer("Shimano")
            .setType(BikeComponent.BikeComponentType.BREAKS)
            .build());
        components.add(BikeComponent.builder()
            .setManufacturer("Canyon")
            .setType(BikeComponent.BikeComponentType.FRAME)
            .build());
        BikeWithComponents bike = BikeWithComponents.builder()
            .setId(new VehicleExtendedId(1l))
            .setGears(18)
            .setLengthCm(150)
            .setBikeComponents(components)
            .build();
        BikeWithComponents inserted = (BikeWithComponents) vehicleRepository.insert(bike);
        BikeWithComponents insertedCopy =  persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setLengthCm(550);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        BikeWithComponents updated = (BikeWithComponents) vehicleRepository.update(insertedCopy);
        //then
        Optional<BikeWithComponents> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v->(BikeWithComponents) v);
         persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
         persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getLengthCm()).isEqualTo(insertedCopy.getLengthCm());
    }

    @Test
    public void testDeleteBike() {
        //given
        var components = new ArrayList<BikeComponent>();
        components.add(BikeComponent.builder()
            .setManufacturer("Shimano")
            .setType(BikeComponent.BikeComponentType.BREAKS)
            .build());
        components.add(BikeComponent.builder()
            .setManufacturer("Canyon")
            .setType(BikeComponent.BikeComponentType.FRAME)
            .build());
        BikeWithComponents bike = BikeWithComponents.builder()
            .setId(new VehicleExtendedId(1l))
            .setGears(18)
            .setLengthCm(150)
            .setBikeComponents(components)
            .build();
        BikeWithComponents inserted = (BikeWithComponents) vehicleRepository.insert(bike);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<BikeWithComponents> deleted = vehicleRepository.deleteById(inserted.getId()).map(v -> (BikeWithComponents) v);
        //then
        Optional<BikeWithComponents> found = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v -> (BikeWithComponents) v);
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
         persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getBikeComponents().get(1), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getBikeComponents().get(0), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
         persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testFindAll() {
        //given
        var components = new ArrayList<BikeComponent>();
        components.add(BikeComponent.builder()
            .setManufacturer("Shimano")
            .setType(BikeComponent.BikeComponentType.BREAKS)
            .build());
        components.add(BikeComponent.builder()
            .setManufacturer("Canyon")
            .setType(BikeComponent.BikeComponentType.FRAME)
            .build());
        BikeWithComponents bike = BikeWithComponents.builder()
            .setId(new VehicleExtendedId(1l))
            .setGears(18)
            .setLengthCm(150)
            .setBikeComponents(components)
            .build();
        CarWithEngine car = CarWithEngine.builder()
            .setId(new VehicleExtendedId(2l))
            .setBrand(CarWithEngine.Brand.AUDI)
            .setLengthCm(350)
            .setEngine(Engine.builder()
                .setId(new EngineId(1l))
                .setPs(400)
                .setType(Engine.EngineType.ELECTRIC)
                .build())
            .build();
         persistenceEventTestHelper.resetEventsCaught();
        //when
        BikeWithComponents insertedBike = (BikeWithComponents) vehicleRepository.insert(bike);
        CarWithEngine insertedCar = (CarWithEngine) vehicleRepository.insert(car);
        //then
        Optional<BikeWithComponents> foundBike = vehicleRepository.findResultById(new VehicleExtendedId(1l)).resultValue().map(v -> (BikeWithComponents)v);
         persistenceEventTestHelper.assertFoundWithResult(foundBike, insertedBike);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedBike.getBikeComponents().get(1), insertedBike);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedBike.getBikeComponents().get(0), insertedBike);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedBike);
        Optional<CarWithEngine> foundCar = vehicleRepository.findResultById(new VehicleExtendedId(2l)).resultValue().map(v -> (CarWithEngine)v);
         persistenceEventTestHelper.assertFoundWithResult(foundCar, insertedCar);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedCar.getEngine());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, insertedCar);
         persistenceEventTestHelper.assertEvents();
        var vehicles = vehicleRepository.findAll().collect(Collectors.toList());
        assertThat(vehicles).hasSize(2);
        var bikes = vehicleRepository.findAllBikes();
        var cars = vehicleRepository.findAllCars();
        assertThat(bikes).hasSize(1);
        assertThat(cars).hasSize(1);

    }

}


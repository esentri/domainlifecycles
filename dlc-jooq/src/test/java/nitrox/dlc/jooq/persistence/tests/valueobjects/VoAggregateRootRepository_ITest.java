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

package nitrox.dlc.jooq.persistence.tests.valueobjects;

import nitrox.dlc.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoAggregateRootId;
import tests.shared.persistence.domain.valueobjects.VoEntity;
import tests.shared.persistence.domain.valueobjects.VoEntityId;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregateRootRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateRootRepository_ITest.class);

    private VoAggregateRootRepository voAggregateRootRepository;

    @BeforeAll
    public void init(){
        voAggregateRootRepository = new VoAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertMin() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMin();
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateRoot inserted = voAggregateRootRepository.insert(copy);


        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(2l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertMiddle() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateRoot inserted = voAggregateRootRepository.insert(copy);


        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();
        assertThat(inserted == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(2), inserted);

        Assertions.assertThat(found.get().getVoIdentityRef().getIdRef()).isNotNull();
        persistenceEventTestHelper.assertEvents();

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testInsertMax() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMax();
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateRoot inserted = voAggregateRootRepository.insert(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(3l)).resultValue();
        assertThat(inserted == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();
        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(2).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertMaxWithEntity() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(inserted == r);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        inserted.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));

        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getEntities().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getEntities().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getEntities().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        VoEntity[] entityArray = inserted.getEntities().toArray(i -> new VoEntity[i]);
        for (int i = entityArray.length - 1; i >= 0; i--) {
            VoEntity entity = entityArray[i];
            if (entity.getValueObjectsOneToMany() != null && entity.getValueObjectsOneToMany().size() > 0) {
                VoOneToManyEntity[] voOuterArray = entity.getValueObjectsOneToMany().toArray(j -> new VoOneToManyEntity[j]);
                for (int j = voOuterArray.length - 1; j >= 0; j--) {
                    VoOneToManyEntity voOuter = voOuterArray[j];
                    persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, voOuter, entity);
                    if (voOuter.getOneToManySet() != null && voOuter.getOneToManySet().size() > 0) {
                        VoOneToManyEntity2[] voInnerArray = voOuter.getOneToManySet().toArray(k -> new VoOneToManyEntity2[k]);
                        for (int k = voInnerArray.length - 1; k >= 0; k--) {
                            VoOneToManyEntity2 voInner = voInnerArray[k];
                            persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, voInner, entity);
                        }
                    }
                }
            }
        }

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getValueObjectsOneToMany2().get(2).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxWithEntityDoNothing() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(inserted);


        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        Assertions.assertThat(inserted)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringFields("concurrencyVersion")
            .isEqualTo(r);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        updated.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testDeleteMiddle() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateRootRepository.deleteById(new VoAggregateRootId(1l));

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMax() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMax();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        voAggregateRootRepository.deleteById(new VoAggregateRootId(3l));

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(3l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(2).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMaxWithEntity() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        voAggregateRootRepository.deleteById(new VoAggregateRootId(4l));

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        VoEntity[] entityArray = inserted.getEntities().toArray(i -> new VoEntity[i]);
        for (int i = entityArray.length - 1; i >= 0; i--) {
            VoEntity entity = entityArray[i];
            if (entity.getValueObjectsOneToMany() != null && entity.getValueObjectsOneToMany().size() > 0) {
                VoOneToManyEntity[] voOuterArray = entity.getValueObjectsOneToMany().toArray(j -> new VoOneToManyEntity[j]);
                for (int j = voOuterArray.length - 1; j >= 0; j--) {
                    VoOneToManyEntity voOuter = voOuterArray[j];
                    persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, voOuter, entity);
                    if (voOuter.getOneToManySet() != null && voOuter.getOneToManySet().size() > 0) {
                        VoOneToManyEntity2[] voInnerArray = voOuter.getOneToManySet().toArray(k -> new VoOneToManyEntity2[k]);
                        for (int k = voInnerArray.length - 1; k >= 0; k--) {
                            VoOneToManyEntity2 voInner = voInnerArray[k];
                            persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, voInner, entity);
                        }
                    }
                }
            }
        }

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(2), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(2).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(1), inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getEntities().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getEntities().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getEntities().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMiddleAddVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.getValueObjectsOneToMany().add(SimpleVoOneToMany.builder()
            .setValue("NEU")
            .build());
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();
        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany().get(3), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMaxAddVo3() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMax();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.getValueObjectsOneToMany2().get(0).getOneToMany3Set().add(
            SimpleVoOneToMany3.builder()
                .setValue("NEU3")
                .build()
        );
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(3l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany2().get(0), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[0], updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[1], updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany2().get(0).getOneToMany3Set().toArray(i -> new SimpleVoOneToMany3[i])[2], updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMaxWithEntityRemoveEntity() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);

        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        VoEntity removedEntity = copy.getEntities().remove(0);

        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        updated.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));


        if (removedEntity.getValueObjectsOneToMany() != null && removedEntity.getValueObjectsOneToMany().size() > 0) {
            VoOneToManyEntity[] voOuterArray = removedEntity.getValueObjectsOneToMany().toArray(j -> new VoOneToManyEntity[j]);
            for (int j = voOuterArray.length - 1; j >= 0; j--) {
                VoOneToManyEntity voOuter = voOuterArray[j];
                persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, voOuter, removedEntity);
                if (voOuter.getOneToManySet() != null && voOuter.getOneToManySet().size() > 0) {
                    VoOneToManyEntity2[] voInnerArray = voOuter.getOneToManySet().toArray(k -> new VoOneToManyEntity2[k]);
                    for (int k = voInnerArray.length - 1; k >= 0; k--) {
                        VoOneToManyEntity2 voInner = voInnerArray[k];
                        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, voInner, removedEntity);
                    }
                }
            }
        }

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, removedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxWithEntityAddEntity() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);

        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);


        persistenceEventTestHelper.resetEventsCaught();

        copy.getEntities().add(VoEntity.builder()
            .setId(new VoEntityId(15l))
            .setText("Neu Entity")
            .setRootId(new VoAggregateRootId(4l))
            .setMyComplexVo(ComplexVo.builder()
                .setValueA("NeuA")
                .setValueB(SimpleVo.builder().setValue("NeuB").build())
                .build())
            .setValueObjectsOneToMany(new HashSet<>(TestDataGenerator.newArrayListOf(VoOneToManyEntity.builder().setValue("Neu VO").build())))
            .build()
        );

        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        updated.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));
        Assertions.assertThat(found.get().getValueObjectsOneToMany2()).isNotEmpty();


        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getEntities().get(3));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getEntities().get(3).getValueObjectsOneToMany().toArray(i -> new VoOneToManyEntity[i])[0], updated.getEntities().get(3));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();


        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMiddleAddVoAndDeleteVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.getValueObjectsOneToMany().add(SimpleVoOneToMany.builder()
            .setValue("NEU")
            .build());
        copy.getValueObjectsOneToMany().remove(0);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();

        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany().get(2), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxWithEntityAddVoAndDeleteVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        VoEntity updatedEntity = copy.getEntities().get(0);
        VoOneToManyEntity toRemove = updatedEntity.getValueObjectsOneToMany().toArray(i -> new VoOneToManyEntity[i])[0];
        updatedEntity.getValueObjectsOneToMany().remove(toRemove);
        VoOneToManyEntity added = VoOneToManyEntity.builder()
            .setValue("NEU")
            .build();
        updatedEntity.getValueObjectsOneToMany().add(added);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();

        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        updated.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, toRemove, updatedEntity);
        if (toRemove.getOneToManySet() != null) {
            VoOneToManyEntity2[] removed = toRemove.getOneToManySet().toArray(j -> new VoOneToManyEntity2[j]);
            if (removed.length > 0) {
                for (int k = removed.length - 1; k >= 0; k--) {
                    persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, removed[k], updatedEntity);
                }
            }
        }
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, added, updatedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updatedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxWithEntityDeleteVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoOneToManyEntity vo = copy.getEntities().get(0).getValueObjectsOneToMany().stream().filter(v -> v.getValue().equals("vo1")).toArray(i -> new VoOneToManyEntity[i])[0];
        copy.getEntities().get(0).getValueObjectsOneToMany().remove(vo);
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        copy.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getEntities().get(0).getValueObjectsOneToMany().stream().filter(v -> v.getValue().equals("vo1")).toArray(i -> new VoOneToManyEntity[i])[0], inserted.getEntities().get(0));
        inserted.getEntities().get(0).getValueObjectsOneToMany().stream().filter(v -> v.getValue().equals("vo1")).toArray(i -> new VoOneToManyEntity[i])[0].getOneToManySet().stream().forEach(v ->
            persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, v, inserted.getEntities().get(0))
        );
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getEntities().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxWithEntityAddVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMaxWithEntity();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        VoOneToManyEntity2 newInnerVo = VoOneToManyEntity2.builder().setValue("NEUNEU").build();
        VoOneToManyEntity newOuterVo = VoOneToManyEntity.builder()
            .setValue("NEU")
            .setOneToManySet(new HashSet<>(TestDataGenerator.newArrayListOf(newInnerVo)))
            .build();
        copy.getEntities().get(0).getValueObjectsOneToMany().add(newOuterVo);
        VoEntity updatedEntity = copy.getEntities().get(0);

        //when
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(4l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        updated.getEntities().stream().map(e -> {
            return found.get().getEntities().stream().filter(f -> f.equals(e)).map(f -> new Result(e, f)).findFirst();
        }).forEach(t -> assertThat(t.get().persisted())
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(t.get().found()));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, newOuterVo, updatedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, newInnerVo, updatedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updatedEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMiddleDeleteVo() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        copy.getValueObjectsOneToMany().remove(0);
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMiddleAddVoAndDeleteVoAndUpdateRoot() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMiddle();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when

        copy.getValueObjectsOneToMany().add(SimpleVoOneToMany.builder()
            .setValue("NEU")
            .build());
        copy.getValueObjectsOneToMany().remove(0);
        copy.setText("Neu1111");

        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(1l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getValueObjectsOneToMany()).isNotEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getValueObjectsOneToMany().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getValueObjectsOneToMany().get(2), updated);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMinSimpleVO() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMin();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        copy.setSimpleVo(SimpleVo.builder().setValue("NEW").build());
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(2l)).resultValue();
        log.debug("Geupdatetes VO Aggregate Root: \n" + found.get());
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateMinAddComplexVO() {
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMin();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        VoAggregateRoot copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        copy.setComplexVo(ComplexVo.builder()
            .setValueA("newA")
            .setValueB(
                SimpleVo
                    .builder()
                    .setValue("newB")
                    .build()
            )
            .build());
        VoAggregateRoot updated = voAggregateRootRepository.update(copy);

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(2l)).resultValue();
        log.debug("Geupdatetes VO Aggregate Root: \n" + found.get());
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMin() {
        //given
        //given
        VoAggregateRoot r = TestDataGenerator.buildVoAggregateMin();
        VoAggregateRoot inserted = voAggregateRootRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateRootRepository.deleteById(new VoAggregateRootId(2l));

        //then
        Optional<VoAggregateRoot> found = voAggregateRootRepository.findResultById(new VoAggregateRootId(2l)).resultValue();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

}

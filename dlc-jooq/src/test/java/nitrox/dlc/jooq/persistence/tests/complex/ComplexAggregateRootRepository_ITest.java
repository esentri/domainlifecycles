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

package nitrox.dlc.jooq.persistence.tests.complex;


import nitrox.dlc.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity2Id;
import tests.shared.persistence.domain.complex.TestEntity3;
import tests.shared.persistence.domain.complex.TestEntity3Id;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity4Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComplexAggregateRootRepository_ITest extends BasePersistence_ITest {

    private static ComplexAggregateRootRepository complexAggregateRootRepository;

    @BeforeAll
    public  void init(){
        complexAggregateRootRepository = new ComplexAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }

    @Test
    public void testInsertComplexScenario() {
        //given
        TestRoot tr = TestDataGenerator.buildTestRootComplex();

        TestRoot trCopy = persistenceEventTestHelper.kryo.copy(tr);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRoot inserted = complexAggregateRootRepository.insert(trCopy);

        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        Assertions.assertThat(inserted.concurrencyVersion()).isGreaterThan(tr.concurrencyVersion());

        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1().getTestEntity2A());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1().getTestEntity2B());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioSimpleUpdateRoot() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        insertedCopy.setName("Updated");
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioDoNothing() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isEqualTo(inserted.concurrencyVersion());
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioSimpleUpdateEntity1() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        insertedCopy.getTestEntity1().setName("Updated");
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioDeleteEntity() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        insertedCopy.getTestEntity1().getTestEntity2B().getTestEntity3List().remove(0);
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2B().getTestEntity3List().get(0));
        //addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1().getTestEntity2B());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateComplexScenarioDeleteSubtreeByRemovingReferenceFromList() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        int childrenBefore = insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().size();
        //when
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().remove(1);
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getTestEntity1().getTestEntity2A().getTestEntity3List().size()).isEqualTo(childrenBefore - 1);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioUpdateMany() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        insertedCopy.getTestEntity1().getTestEntity2A().setName("Updated");
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).setName("Updated");
        insertedCopy.getTestEntity1().getTestEntity2B().getTestEntity3List().get(0).setName("Updated");
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1().getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1().getTestEntity2A());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1().getTestEntity2B().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioInsertNewChildren() {
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        TestEntity3Id addedToId = inserted.getTestEntity1().getTestEntity2A().getTestEntity3List().get(0).getId();
        inserted.getTestEntity1().getTestEntity2A().getTestEntity3List().get(0).setTestEntity4List(Arrays.asList(
            TestEntity4.builder()
                .setId(new TestEntity4Id(55l))
                .setName("New1")
                .setTestEntity3Id(addedToId)
                .build(),
            TestEntity4.builder()
                .setId(new TestEntity4Id(56l))
                .setName("New2")
                .setTestEntity3Id(addedToId)
                .build()
        ));
        TestRoot updated = complexAggregateRootRepository.update(inserted);
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
    }

    @Test
    public void testUpdateComplexScenarioInsertUpdateDeleteAtOnce() {
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        inserted.setName("Updated");
        inserted.getTestEntity1().setTestEntity2A(null);
        TestRoot updated = complexAggregateRootRepository.update(inserted);
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
    }

    @Test
    public void testUpdateComplexScenarioDeleteSubtreeBySettingReferenceNullAndUpdateReferencingEntity() {
        //given
        TestRoot built = TestDataGenerator.buildTestRootComplex();
        TestRoot inserted = complexAggregateRootRepository.insert(built);

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntity1().setTestEntity2A(null);
        insertedCopy.getTestEntity1().setName("Updated");

        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2A());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        TestRoot built = TestDataGenerator.buildTestRootComplex();
        TestRoot inserted = complexAggregateRootRepository.insert(built);

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntity1().setTestEntity2B(null);
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().get(0).setName("Test2BTest3_1");
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().add(TestEntity3.builder()
            .setId(new TestEntity3Id(45L))
            .setName("Test2BTest3_2")
            .setTestRootId(insertedCopy.getId())
            .setTestEntity2Id(insertedCopy.getTestEntity1().getTestEntity2A().getId())
            .build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2B().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2B().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2B());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1().getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntity1().getTestEntity2A().getTestEntity3List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        TestRoot built = TestDataGenerator.buildTestRootComplex();
        TestRoot inserted = complexAggregateRootRepository.insert(built);

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntity1().setTestEntity2B(null);
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().get(0).setName("Test2BTest3_1");
        insertedCopy.getTestEntity1().getTestEntity2A().getTestEntity3List().add(TestEntity3.builder()
            .setId(new TestEntity3Id(45L))
            .setName("Test2BTest3_1")
            .setTestRootId(insertedCopy.getId())
            .setTestEntity2Id(insertedCopy.getTestEntity1().getTestEntity2A().getId())
            .build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        //we expect a unique constraint exception
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        });

    }
    @Test
    public void testUpdateComplexScenarioDeleteSubtreeBySettingReferenceNullOnly() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());

        TestRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntity1().setTestEntity2A(null);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRoot updated = complexAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2A());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();

        Assertions.assertThat(updated.getTestEntity1().getTestEntity2A()).isNull();
    }

    @Test
    public void testUpdateComplexScenarioSwitchOptionalForwardReferences() {
        //given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        inserted.getTestEntity1().setTestEntity2B(null);
        TestRoot prepared = complexAggregateRootRepository.update(inserted);
        TestRoot preparedCopy = persistenceEventTestHelper.kryo.copy(prepared);
        persistenceEventTestHelper.resetEventsCaught();
        //when

        preparedCopy.getTestEntity1().setTestEntity2A(null);
        TestEntity3 t2B_3_1 = TestEntity3.builder()
            .setId(new TestEntity3Id(7l))
            .setName("Test2BTest3_1")
            .setTestRootId(new TestRootId(1l))
            .setTestEntity2Id(new TestEntity2Id(4l))
            .build();
        TestEntity3 t2B_3_2 = TestEntity3.builder()
            .setId(new TestEntity3Id(8l))
            .setName("Test2BTest3_2")
            .setTestRootId(new TestRootId(1l))
            .setTestEntity2Id(new TestEntity2Id(4l))
            .build();

        preparedCopy.getTestEntity1().setTestEntity2B(TestEntity2.builder()
            .setId(new TestEntity2Id(4l))
            .setName("TestEntity 2 B")
            .setTestRootId(new TestRootId(1l))
            .setTestEntity3List(TestDataGenerator.newArrayListOf(t2B_3_1, t2B_3_2))
            .build());

        TestRoot updated = complexAggregateRootRepository.update(preparedCopy);
        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, prepared.getTestEntity1().getTestEntity2A());

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntity1().getTestEntity2B());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();

        Assertions.assertThat(updated.getTestEntity1().getTestEntity2A()).isNull();

        Assertions.assertThat(updated.getTestEntity1().getTestEntity2B()).isNotNull();
    }


    @Test
    public void testDeleteComplexScenario() {
        // given
        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        complexAggregateRootRepository.deleteById(new TestRootId(1l));

        //then
        Optional<TestRoot> found = complexAggregateRootRepository.findResultById(new TestRootId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0).getTestEntity6());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0).getTestEntity5List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1).getTestEntity4List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2A().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2A());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1()
            .getTestEntity2B().getTestEntity3List().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1().getTestEntity2B());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntity1());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

}

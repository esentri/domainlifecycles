/*
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

package io.domainlifecycles.jooq.persistence.tests.oneToOneLeadingFK;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneToOneLeadingAggregateRootRepository_ITest extends BasePersistence_ITest {

    private OneToOneLeadingAggregateRootRepository oneToOneLeadingAggregateRootRepository;

    @BeforeAll
    public void init() {
        oneToOneLeadingAggregateRootRepository = new OneToOneLeadingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingOnlyRoot();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntity() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityOneToOneLeading().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneLeading().getName()).isEqualTo(
            insertedCopy.getTestEntityOneToOneLeading().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntity() {
        //given
        TestRootOneToOneLeading tr = TestDataGenerator.buildOneToOneLeadingOnlyRoot();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityOneToOneLeading testEntityOneToOneLeading = TestEntityOneToOneLeading.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityOneToOneLeadingId(3l))
            .build();

        insertedCopy.setTestEntityOneToOneLeading(testEntityOneToOneLeading);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneLeading().getName()).isEqualTo(
            insertedCopy.getTestEntityOneToOneLeading().getName());
    }

    @Test
    public void testUpdateCompleteDeleteEntity() {
        //given
        TestRootOneToOneLeading trs = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityOneToOneLeading(null);
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneLeading()).isNull();

    }

    //TODO warum werden bei Updates nicht version einschränkungen gemacht?
    @Test
    public void testUpdateCompleteDeleteEntityUpdateRoot() {
        //given
        TestRootOneToOneLeading trs = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityOneToOneLeading(null);
        insertedCopy.setName("UPDATE");
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneLeading()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootOneToOneLeading trs = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneLeading> deleted = oneToOneLeadingAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateDeleteComplete() {
        //given
        TestRootOneToOneLeading trs = TestDataGenerator.buildOneToOneLeadingComplete();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityOneToOneLeading(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneLeading updated = oneToOneLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToOneLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneLeading()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootOneToOneLeading trs = TestDataGenerator.buildOneToOneLeadingOnlyRoot();
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneLeading> deleted = oneToOneLeadingAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneLeading> found = oneToOneLeadingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

}

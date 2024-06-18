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

package io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingLeadingFK;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneToOneFollowingLeadingAggregateRootRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = LoggerFactory.getLogger(OneToOneFollowingLeadingAggregateRootRepository_ITest.class);

    private OneToOneFollowingLeadingAggregateRootRepository oneToOneFollowingLeadingAggregateRootRepository;

    @BeforeAll
    public void init(){
        oneToOneFollowingLeadingAggregateRootRepository = new OneToOneFollowingLeadingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntityA() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityAOneToOneFollowingLeading().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityAOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntityB() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityBOneToOneFollowingLeading().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityBOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntityAAndB() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityAOneToOneFollowingLeading().setName("UPDATED");
        insertedCopy.getTestEntityBOneToOneFollowingLeading().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityBOneToOneFollowingLeading().getName());
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityAOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntityA() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityAOneToOneFollowingLeading testEntityAOneToOneFollowingLeading = TestEntityAOneToOneFollowingLeading.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityAOneToOneFollowingLeadingId(3L))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.setTestEntityAOneToOneFollowingLeading(testEntityAOneToOneFollowingLeading);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityAOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntityB() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityBOneToOneFollowingLeading testEntityBOneToOneFollowingLeading = TestEntityBOneToOneFollowingLeading.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityBOneToOneFollowingLeadingId(3L))
            .build();

        insertedCopy.setTestEntityBOneToOneFollowingLeading(testEntityBOneToOneFollowingLeading);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityBOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntityAAndB() {
        //given
        TestRootOneToOneFollowingLeading tr = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityBOneToOneFollowingLeading testEntityBOneToOneFollowingLeading = TestEntityBOneToOneFollowingLeading.builder()
            .setName("TestEntityBAdded")
            .setId(new TestEntityBOneToOneFollowingLeadingId(3L))
            .build();

        TestEntityAOneToOneFollowingLeading testEntityAOneToOneFollowingLeading = TestEntityAOneToOneFollowingLeading.builder()
            .setName("TestEntityAAdded")
            .setId(new TestEntityAOneToOneFollowingLeadingId(3L))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.setTestEntityBOneToOneFollowingLeading(testEntityBOneToOneFollowingLeading);
        insertedCopy.setTestEntityAOneToOneFollowingLeading(testEntityAOneToOneFollowingLeading);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityBOneToOneFollowingLeading().getName());
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading().getName()).isEqualTo(insertedCopy.getTestEntityAOneToOneFollowingLeading().getName());
    }

    @Test
    public void testUpdateCompleteDeleteEntityA() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityAOneToOneFollowingLeading(null);
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading()).isNull();

    }

    @Test
    public void testUpdateCompleteDeleteEntityB() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityBOneToOneFollowingLeading(null);
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading()).isNull();

    }

    @Test
    public void testUpdateCompleteDeleteEntityAAndB() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityBOneToOneFollowingLeading(null);
        insertedCopy.setTestEntityAOneToOneFollowingLeading(null);
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading()).isNull();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneFollowingLeading> deleted = oneToOneFollowingLeadingAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneFollowingLeading> deleted = oneToOneFollowingLeadingAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDeleteCompleteA() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityAOneToOneFollowingLeading(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateDeleteCompleteB() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityBOneToOneFollowingLeading(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateDeleteCompleteAAndB() {
        //given
        TestRootOneToOneFollowingLeading trs = TestDataGenerator.buildOneToOneFollowingLeadingComplete();
        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowingLeading insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityBOneToOneFollowingLeading(null);
        insertedCopy.setTestEntityAOneToOneFollowingLeading(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneFollowingLeading updated = oneToOneFollowingLeadingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowingLeading> found = oneToOneFollowingLeadingAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityAOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityBOneToOneFollowingLeading());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityAOneToOneFollowingLeading()).isNull();
        Assertions.assertThat(updated.getTestEntityBOneToOneFollowingLeading()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }


}

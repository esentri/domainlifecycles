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

package io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingFK;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneToOneFollowingAggregateRootRepository_ITest extends BasePersistence_ITest {


    private OneToOneFollowingAggregateRootRepository oneToOneFollowingAggregateRootRepository;

    @BeforeAll
    public void init() {
        oneToOneFollowingAggregateRootRepository = new OneToOneFollowingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository
            .findResultById(new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingOnlyRoot();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingComplete();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntity() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingComplete();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityOneToOneFollowing().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneFollowing().getName()).isEqualTo(
            insertedCopy.getTestEntityOneToOneFollowing().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntity() {
        //given
        TestRootOneToOneFollowing tr = TestDataGenerator.buildOneToOneFollowingOnlyRoot();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(tr);
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityOneToOneFollowing testEntityOneToOneFollowing = TestEntityOneToOneFollowing.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityOneToOneFollowingId(3l))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.setTestEntityOneToOneFollowing(testEntityOneToOneFollowing);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneFollowing().getName()).isEqualTo(
            insertedCopy.getTestEntityOneToOneFollowing().getName());
    }

    @Test
    public void testUpdateCompleteDeleteEntity() {
        //given
        TestRootOneToOneFollowing trs = TestDataGenerator.buildOneToOneFollowingComplete();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityOneToOneFollowing(null);
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneFollowing()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootOneToOneFollowing trs = TestDataGenerator.buildOneToOneFollowingComplete();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneFollowing> deleted = oneToOneFollowingAggregateRootRepository.deleteById(
            inserted.getId());
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootOneToOneFollowing trs = TestDataGenerator.buildOneToOneFollowingOnlyRoot();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneFollowing> deleted = oneToOneFollowingAggregateRootRepository.deleteById(
            inserted.getId());
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDeleteComplete() {
        //given
        TestRootOneToOneFollowing trs = TestDataGenerator.buildOneToOneFollowingComplete();
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneFollowing insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setTestEntityOneToOneFollowing(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneFollowing updated = oneToOneFollowingAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneFollowing> found = oneToOneFollowingAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToOneFollowing());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getTestEntityOneToOneFollowing()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }


}

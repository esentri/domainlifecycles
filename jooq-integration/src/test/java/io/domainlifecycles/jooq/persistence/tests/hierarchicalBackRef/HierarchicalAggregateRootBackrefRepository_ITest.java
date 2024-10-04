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

package io.domainlifecycles.jooq.persistence.tests.hierarchicalBackRef;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HierarchicalAggregateRootBackrefRepository_ITest extends BasePersistence_ITest {

    private HierarchicalAggregateRootBackrefRepository hierarchicalAggregateRootBackrefRepository;

    @BeforeAll
    public void init() {
        hierarchicalAggregateRootBackrefRepository = new HierarchicalAggregateRootBackrefRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository
            .findResultById(new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel1() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel1();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository
            .findResultById(new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel2() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel2();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository
            .findResultById(new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel3() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository
            .findResultById(new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefOnlyRoot();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel2() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel2();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel3UpdateLevel1() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getChild().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getName()).isEqualTo(insertedCopy.getChild().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntity() {
        //given
        TestRootHierarchicalBackref tr = TestDataGenerator.buildTestRootHierarchicalBackrefOnlyRoot();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(tr);
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestRootHierarchicalBackref testAdded = TestRootHierarchicalBackref.builder()
            .setName("TestAdded")
            .setId(new TestRootHierarchicalBackrefId(3l))
            .setParent(insertedCopy)
            .build();

        insertedCopy.setChild(testAdded);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            new TestRootHierarchicalBackrefId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getName()).isEqualTo(insertedCopy.getChild().getName());
    }

    @Test
    public void testUpdateCompleteLevel1DeleteEntity() {
        //given
        TestRootHierarchicalBackref trs = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel1();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setChild(null);
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootHierarchicalBackref trs = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootHierarchicalBackref> deleted = hierarchicalAggregateRootBackrefRepository.deleteById(
            inserted.getId());
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateDeleteCompleteLevel2() {
        //given
        TestRootHierarchicalBackref trs = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel2();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getChild().setChild(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getChild()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());

    }

    @Test
    public void testDeleteHierarchyCompleteLevel3() {
        //given
        TestRootHierarchicalBackref trs = TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchicalBackref insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setChild(null);

        //when
        TestRootHierarchicalBackref updated = hierarchicalAggregateRootBackrefRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(found.get().getChild()).isNull();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootHierarchicalBackref trs = TestDataGenerator.buildTestRootHierarchicalBackrefOnlyRoot();
        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootHierarchicalBackref> deleted = hierarchicalAggregateRootBackrefRepository.deleteById(
            inserted.getId());
        //then
        Optional<TestRootHierarchicalBackref> found = hierarchicalAggregateRootBackrefRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

}

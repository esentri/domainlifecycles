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

package io.domainlifecycles.jooq.persistence.tests.hierarchical;


import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HierarchicalAggregateRootRepository_ITest extends BasePersistence_ITest {
    
    private HierarchicalAggregateRootRepository hierarchicalAggregateRootRepository;

    @BeforeAll
    public void init(){
        hierarchicalAggregateRootRepository = new HierarchicalAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository
            .findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel1() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalCompleteLevel1();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository
            .findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel2() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalCompleteLevel2();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository
            .findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel3() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalCompleteLevel3();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository
            .findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalOnlyRoot();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel2() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalCompleteLevel2();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel3UpdateLevel1() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalCompleteLevel3();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getChild().setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getName()).isEqualTo(insertedCopy.getChild().getName());
    }

    @Test
    public void testUpdateSimpleInsertEntity() {
        //given
        TestRootHierarchical tr = TestDataGenerator.buildTestRootHierarchicalOnlyRoot();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(tr);
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestRootHierarchical testAdded = TestRootHierarchical.builder()
            .setName("TestAdded")
            .setId(new TestRootHierarchicalId(3l))
            .setParentId(new TestRootHierarchicalId(1l))
            .build();

        insertedCopy.setChild(testAdded);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(new TestRootHierarchicalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getName()).isEqualTo(insertedCopy.getChild().getName());
    }

    @Test
    public void testUpdateCompleteLevel1DeleteEntity() {
        //given
        TestRootHierarchical trs = TestDataGenerator.buildTestRootHierarchicalCompleteLevel1();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setChild(null);
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootHierarchical trs = TestDataGenerator.buildTestRootHierarchicalCompleteLevel3();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootHierarchical> deleted = hierarchicalAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateDeleteCompleteLevel2() {
        //given
        TestRootHierarchical trs = TestDataGenerator.buildTestRootHierarchicalCompleteLevel2();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getChild().setChild(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild().getChild()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());

    }

    @Test
    public void testDeleteHierarchyCompleteLevel3() {
        //given
        TestRootHierarchical trs = TestDataGenerator.buildTestRootHierarchicalCompleteLevel3();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootHierarchical insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setChild(null);

        //when
        TestRootHierarchical updated = hierarchicalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild().getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild().getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getChild());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getChild()).isNull();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootHierarchical trs = TestDataGenerator.buildTestRootHierarchicalOnlyRoot();
        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootHierarchical> deleted = hierarchicalAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootHierarchical> found = hierarchicalAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testIdProperty() {

        var em = Domain.entityMirrorFor(TestRootHierarchical.class.getName());
        assertThat(em.getIdentityField()).isPresent();
        assertThat(em.getIdentityField().get().getName()).isEqualTo("id");
    }

}

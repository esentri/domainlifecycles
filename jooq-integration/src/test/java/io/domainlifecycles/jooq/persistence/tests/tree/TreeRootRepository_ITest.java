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

package io.domainlifecycles.jooq.persistence.tests.tree;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.tree.TreeNode;
import tests.shared.persistence.domain.tree.TreeNodeId;
import tests.shared.persistence.domain.tree.TreeRoot;
import tests.shared.persistence.domain.tree.TreeRootId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TreeRootRepository_ITest extends BasePersistence_ITest {

    private TreeRootRepository treeRootRepository;

    @BeforeAll
    public void init() {
        treeRootRepository = new TreeRootRepository(
            persistenceConfiguration.dslContext,
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot inserted = treeRootRepository.insert(tr);
        //then
        Optional<TreeRoot> found = treeRootRepository
            .findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testInsertLevel1() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel1();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot inserted = treeRootRepository.insert(tr);
        //then
        Optional<TreeRoot> found = treeRootRepository
            .findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel2() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel2();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot inserted = treeRootRepository.insert(tr);
        //then
        Optional<TreeRoot> found = treeRootRepository
            .findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0));
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertLevel3() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel3();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot inserted = treeRootRepository.insert(tr);
        //then
        Optional<TreeRoot> found = treeRootRepository
            .findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getDirectChildNodes().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeOnlyRoot();
        TreeRoot inserted = treeRootRepository.insert(tr);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel2() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel2();
        TreeRoot inserted = treeRootRepository.insert(tr);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteLevel3UpdateLevel1() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel3();
        TreeRoot inserted = treeRootRepository.insert(tr);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getDirectChildNodes().get(0).setNodeName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getDirectChildNodes().get(0).getNodeName()).isEqualTo(
            insertedCopy.getDirectChildNodes().get(0).getNodeName());
    }

    @Test
    public void testUpdateSimpleInsertTopLevelNode() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeOnlyRoot();
        TreeRoot inserted = treeRootRepository.insert(tr);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TreeNode testAdded = TreeNode.builder()
            .setNodeName("TestAdded")
            .setId(new TreeNodeId(10l))
            .setRootId(new TreeRootId(1l))
            .build();

        insertedCopy.getDirectChildNodes().add(testAdded);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleInsertNodeLevel1() {
        //given
        TreeRoot tr = TestDataGenerator.buildTreeLevel1();
        TreeRoot inserted = treeRootRepository.insert(tr);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TreeNode testAdded = TreeNode.builder()
            .setNodeName("TestAdded")
            .setId(new TreeNodeId(10l))
            .setParentNodeId(new TreeNodeId(2l))
            .build();

        insertedCopy.getDirectChildNodes().get(0).getChildNodes().add(testAdded);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(new TreeRootId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            insertedCopy.getDirectChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteLevel1DeleteEntity() {
        //given
        TreeRoot trs = TestDataGenerator.buildTreeLevel1();
        TreeRoot inserted = treeRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getDirectChildNodes().remove(0);
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getDirectChildNodes().size()).isEqualTo(0);

    }

    @Test
    public void testDeleteCompleteLevel3() {
        //given
        TreeRoot trs = TestDataGenerator.buildTreeLevel3();
        TreeRoot inserted = treeRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TreeRoot> deleted = treeRootRepository.deleteById(inserted.getId());
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateDeleteCompleteLevel2() {
        //given
        TreeRoot trs = TestDataGenerator.buildTreeLevel2();
        TreeRoot inserted = treeRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getDirectChildNodes().remove(0);
        insertedCopy.setName("UPDATED");
        //when
        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TreeRoot trs = TestDataGenerator.buildTreeOnlyRoot();
        TreeRoot inserted = treeRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TreeRoot> deleted = treeRootRepository.deleteById(inserted.getId());
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testChangeLevel3() {
        //given
        TreeRoot trs = TestDataGenerator.buildTreeLevel3();
        TreeRoot inserted = treeRootRepository.insert(trs);
        TreeRoot insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        insertedCopy.getDirectChildNodes().get(0).getChildNodes().get(0).setNodeName("UPDATED");
        insertedCopy.getDirectChildNodes().get(0).getChildNodes().get(0).getChildNodes().add(
            TreeNode
                .builder()
                .setId(new TreeNodeId(11l))
                .setNodeName("NEW")
                .setParentNodeId(inserted.getDirectChildNodes().get(0).getChildNodes().get(0).getId())
                .build());
        insertedCopy.getDirectChildNodes().get(0).getChildNodes().get(0).getChildNodes().add(
            TreeNode
                .builder()
                .setId(new TreeNodeId(12l))
                .setNodeName("NEW2")
                .setParentNodeId(inserted.getDirectChildNodes().get(0).getChildNodes().get(0).getId())
                .build());
        insertedCopy.getDirectChildNodes().get(1).getChildNodes().remove(0);

        TreeRoot updated = treeRootRepository.update(insertedCopy);
        //then
        Optional<TreeRoot> found = treeRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(found).isPresent();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getDirectChildNodes().get(0).getChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getDirectChildNodes().get(0).getChildNodes().get(0).getChildNodes().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getDirectChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getDirectChildNodes().get(0).getChildNodes().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

}

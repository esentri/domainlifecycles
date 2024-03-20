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

/*
package nitrox.dlc.test.persistence.tests.oneToManyIdentityEnum;

import nitrox.dlc.test.persistence.BasePersistence_ITest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToManyIdentityEnum.EntityIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumListId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.ValueWithLists;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RootIdEnumListRepository_ITest extends BasePersistence_ITest {
    
    private RootIdEnumListRepository rootIdEnumListRepository;

    @BeforeAll
    public void init(){
        rootIdEnumListRepository = new RootIdEnumListRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository
            .findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertWithEntity() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithEntity();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository
            .findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getEntity());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository
            .findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListOnlyRoot();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleCompleteInsertOnlyEnum() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getEnumList().add(MyEnum.TWO);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleInsertOnlyId() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListOnlyRoot();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getIdList().add(new MyId(33l));
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteInsertEnumAndId() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEnumList().add(MyEnum.ONE);
        insertedCopy.getIdList().add(new MyId(5l));
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateWithEntityAddValueWithLists() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithEntity();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var enumList = List.of(MyEnum.ONE, MyEnum.TWO);
        insertedCopy.getEntity().setValueWithLists(ValueWithLists
            .builder()
                .setEnums(enumList)
            .build());
        insertedCopy.getIdList().add(new MyId(5l));
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateWithEntityAddToIdEntityList() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithEntity();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);


        insertedCopy.getEntity().getIdList().add(new MyId(123l));

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteDeleteEnum() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEnumList().remove(0);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteDeleteEntity() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setEntity(null);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteDeleteValueWithLists() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEntity().setValueWithLists(null);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteUpdateValueWithLists() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var ids = List.of(new MyId(0l));
        insertedCopy.getEntity().setValueWithLists(ValueWithLists
            .builder()
                .setIds(ids)
            .build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteUpdateEntity() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setEntity(EntityIdEnumList
            .builder()
                .setId(new EntityIdEnumList.EntityIdEnumListId(22l))
                .setConcurrencyVersion(22)
            .build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteInsertIdAndDeleteIdAndUpdate() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);



        insertedCopy.getIdList().add(new MyId(55l));
        insertedCopy.getIdList().remove(0);
        insertedCopy.setName("UPDATED");

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).resultValue();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateCompleteDeleteEnums() {
        //given
        RootIdEnumList trs = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getEnumList().removeAll(insertedCopy.getEnumList());
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteComplete() {
        //given
        RootIdEnumList trs = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<RootIdEnumList> deleted = rootIdEnumListRepository.deleteById(inserted.getId());
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId()).resultValue();
        assertThat(deleted).isPresent();
        assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        RootIdEnumList trs = TestDataGenerator.buildRootIdEnumListOnlyRoot();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<RootIdEnumList> deleted = rootIdEnumListRepository.deleteById(inserted.getId());
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId()).resultValue();
        assertThat(deleted).isPresent();
        assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteWithEntity() {
        //given
        RootIdEnumList trs = TestDataGenerator.buildRootIdEnumListWithEntity();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<RootIdEnumList> deleted = rootIdEnumListRepository.deleteById(inserted.getId());
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId()).resultValue();
        assertThat(deleted).isPresent();
        assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getEntity());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDeleteComplete() {
        //given
        RootIdEnumList trs = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEnumList().clear();
        insertedCopy.getIdList().clear();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

}*/


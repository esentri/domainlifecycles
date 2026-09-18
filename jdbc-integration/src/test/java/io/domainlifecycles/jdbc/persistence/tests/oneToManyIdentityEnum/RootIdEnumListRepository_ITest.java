package io.domainlifecycles.jdbc.persistence.tests.oneToManyIdentityEnum;

import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToManyIdentityEnum.EntityIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyUuidId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumListId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.ValueWithLists;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RootIdEnumListRepository_ITest extends JdbcBasePersistence_ITest {

    private RootIdEnumListRepository rootIdEnumListRepository;

    @BeforeAll
    public void init() {
        rootIdEnumListRepository = new RootIdEnumListRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
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
            .findById(new RootIdEnumListId(1l));
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
            .findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted
        .getEntity());
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
            .findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted
            .getEntity());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated
            .getEntity());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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

        var originalEntity = insertedCopy.getEntity();
        insertedCopy.setEntity(null);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, originalEntity);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateCompleteUpdateEntity() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var originalEntity = insertedCopy.getEntity();
        insertedCopy.setEntity(EntityIdEnumList
            .builder()
                .setId(new EntityIdEnumList.EntityIdEnumListId(22l))
                .setConcurrencyVersion(22)
            .build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, originalEntity);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated
            .getEntity());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId());
        assertThat(deleted).isPresent();
        assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get()
            .getEntity());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId());
        assertThat(deleted).isPresent();
        assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get()
        .getEntity());
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
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(inserted.getId());
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertWithDuplicateEnumAndIdValues() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithDuplicates();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        assertThat(found.get().getEnumList()).hasSize(3);
        assertThat(found.get().getIdList()).hasSize(3);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateRemoveOneOfDuplicateEnumAndIdValues() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithDuplicates();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEnumList().remove(tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum.ONE);
        insertedCopy.getIdList().remove(new MyId(7l));

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getEnumList()).containsExactlyInAnyOrder(
            tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum.ONE,
            tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum.TWO);
        assertThat(found.get().getIdList()).containsExactlyInAnyOrder(new MyId(7l), new MyId(9l));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateClearDuplicateEnumAndIdValues() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithDuplicates();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEnumList().clear();
        insertedCopy.getIdList().clear();

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getEnumList()).isEmpty();
        assertThat(found.get().getIdList()).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateEntityClearEnumAndIdList() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListComplete();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getEntity().getEnumList().clear();
        insertedCopy.getEntity().getIdList().clear();

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getEntity().getEnumList()).isEmpty();
        assertThat(found.get().getEntity().getIdList()).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated
            .getEntity());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertUuidIdList() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithUuidIds();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        assertThat(found.get().getUuidIdList()).containsExactlyInAnyOrder(
            new MyUuidId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            new MyUuidId(UUID.fromString("22222222-2222-2222-2222-222222222222")));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateAddUuidId() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithUuidIds();
        rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).orElseThrow();

        var newId = new MyUuidId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
        insertedCopy.getUuidIdList().add(newId);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getUuidIdList()).hasSize(3).contains(newId);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateRemoveUuidId() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithUuidIds();
        rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = rootIdEnumListRepository.findById(new RootIdEnumListId(1l)).orElseThrow();

        insertedCopy.getUuidIdList().remove(
            new MyUuidId(UUID.fromString("11111111-1111-1111-1111-111111111111")));

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getUuidIdList()).containsExactly(
            new MyUuidId(UUID.fromString("22222222-2222-2222-2222-222222222222")));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertValueWithListsList() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithValueWithListsList();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        assertThat(found.get().getValueWithListsList()).hasSize(2);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getValueWithListsList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getValueWithListsList().get(1), inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateAddValueWithListsListElement() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithValueWithListsList();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var newElement = ValueWithLists.builder()
            .setEnums(List.of(tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum.ONE))
            .setIds(List.of(new MyId(30l)))
            .build();
        insertedCopy.getValueWithListsList().add(newElement);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getValueWithListsList()).hasSize(3);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, newElement,
            updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateRemoveValueWithListsListElement() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithValueWithListsList();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var removedElement = insertedCopy.getValueWithListsList().get(0);
        insertedCopy.getValueWithListsList().remove(0);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getValueWithListsList()).hasSize(1);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, removedElement,
            updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateReplaceValueWithListsListElement() {
        //given
        RootIdEnumList tr = TestDataGenerator.buildRootIdEnumListWithValueWithListsList();
        RootIdEnumList inserted = rootIdEnumListRepository.insert(tr);
        RootIdEnumList insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var replacedElement = insertedCopy.getValueWithListsList().get(0);
        var replacementElement = ValueWithLists.builder()
            .setEnums(List.of(tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum.TWO))
            .setIds(List.of(new MyId(40l), new MyId(41l)))
            .build();
        insertedCopy.getValueWithListsList().set(0, replacementElement);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RootIdEnumList updated = rootIdEnumListRepository.update(insertedCopy);
        //then
        Optional<RootIdEnumList> found = rootIdEnumListRepository.findById(new RootIdEnumListId(1l));
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        assertThat(found.get().getValueWithListsList()).hasSize(2);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, replacedElement,
            updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            replacementElement, updated);
        persistenceEventTestHelper.assertEvents();
    }

}

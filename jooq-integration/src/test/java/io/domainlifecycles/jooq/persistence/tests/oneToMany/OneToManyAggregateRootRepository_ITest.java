package io.domainlifecycles.jooq.persistence.tests.oneToMany;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

import java.util.Arrays;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneToManyAggregateRootRepository_ITest extends BasePersistence_ITest {

    private OneToManyAggregateRootRepository oneToManyAggregateRootRepository;

    @BeforeAll
    public void init() {
        oneToManyAggregateRootRepository = new OneToManyAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository
            .findResultById(new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository
            .findResultById(new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getTestEntityOneToManyList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyOnlyRoot();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntity() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityOneToManyList().get(0).setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateSimpleInsertEntity() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyOnlyRoot();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityOneToMany testEntityOneToMany = TestEntityOneToMany.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityOneToManyId(3l))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.setTestEntityOneToManyList(Arrays.asList(testEntityOneToMany));
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteInsertEntity() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityOneToMany testEntityOneToMany = TestEntityOneToMany.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityOneToManyId(15l))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.getTestEntityOneToManyList().add(testEntityOneToMany);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getTestEntityOneToManyList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteDeleteEntity() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityOneToManyList().remove(1);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToManyList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteInsertEntityAndDeleteEntityAndUpdateEntity() {
        //given
        TestRootOneToMany tr = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(tr);
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityOneToMany testEntityOneToMany = TestEntityOneToMany.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityOneToManyId(15l))
            .setTestRootId(insertedCopy.getId())
            .build();

        insertedCopy.getTestEntityOneToManyList().add(testEntityOneToMany);
        insertedCopy.getTestEntityOneToManyList().get(1).setName("UPDATED");
        insertedCopy.getTestEntityOneToManyList().remove(0);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            new TestRootOneToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED,
            updated.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getTestEntityOneToManyList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateCompleteDeleteEntityAndUpdateRoot() {
        //given
        TestRootOneToMany trs = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        insertedCopy.getTestEntityOneToManyList().remove(0);
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootOneToMany trs = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToMany> deleted = oneToManyAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getTestEntityOneToManyList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootOneToMany trs = TestDataGenerator.buildOneToManyOnlyRoot();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToMany> deleted = oneToManyAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
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
        TestRootOneToMany trs = TestDataGenerator.buildOneToManyComplete();
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityOneToManyList().clear();
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToMany updated = oneToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToMany> found = oneToManyAggregateRootRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToManyList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getTestEntityOneToManyList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

}

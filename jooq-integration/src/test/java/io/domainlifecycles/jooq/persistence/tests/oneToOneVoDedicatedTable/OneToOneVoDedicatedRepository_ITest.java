package io.domainlifecycles.jooq.persistence.tests.oneToOneVoDedicatedTable;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicatedId;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.VoDedicated;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneToOneVoDedicatedRepository_ITest extends BasePersistence_ITest {

    private OneToOneVoDedicatedRepository oneToOneVoDedicatedRepository;

    @BeforeAll
    public void init() {
        oneToOneVoDedicatedRepository = new OneToOneVoDedicatedRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertOnlyRoot() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedOnlyRoot();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository
            .findResultById(new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository
            .findResultById(new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getVo(),
            inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleOnlyRoot() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedOnlyRoot();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleComplete() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testUpdateSimpleCompleteOnlyVo() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setVo(VoDedicated.builder().setName("UPDATED").build());
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getVo(),
            inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getVo(),
            updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getVo().name()).isEqualTo(insertedCopy.getVo().name());
    }

    @Test
    public void testUpdateSimpleInsertVo() {
        //given
        TestRootOneToOneVoDedicated tr = TestDataGenerator.buildOneToOneVoDedicatedOnlyRoot();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(tr);
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        VoDedicated vo = VoDedicated.builder()
            .setName("ADDED")
            .build();

        insertedCopy.setVo(vo);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            new TestRootOneToOneVoDedicatedId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getVo(),
            updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getVo().name()).isEqualTo(insertedCopy.getVo().name());
    }

    @Test
    public void testUpdateCompleteDeleteVo() {
        //given
        TestRootOneToOneVoDedicated trs = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setVo(null);
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getVo(),
            inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getVo()).isNull();

    }

    @Test
    public void testUpdateCompleteDeleteVoUpdateRoot() {
        //given
        TestRootOneToOneVoDedicated trs = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setVo(null);
        insertedCopy.setName("UPDATE");
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getVo(),
            inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getVo()).isNull();

    }

    @Test
    public void testDeleteComplete() {
        //given
        TestRootOneToOneVoDedicated trs = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneVoDedicated> deleted = oneToOneVoDedicatedRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getVo(), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();

    }

    @Test
    public void testUpdateDeleteComplete() {
        //given
        TestRootOneToOneVoDedicated trs = TestDataGenerator.buildOneToOneVoDedicatedComplete();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootOneToOneVoDedicated insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.setVo(null);
        insertedCopy.setName("UPDATED");
        //when
        TestRootOneToOneVoDedicated updated = oneToOneVoDedicatedRepository.update(insertedCopy);
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getVo(),
            inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getVo()).isNull();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());

    }

    @Test
    public void testDeleteOnlyRoot() {
        //given
        TestRootOneToOneVoDedicated trs = TestDataGenerator.buildOneToOneVoDedicatedOnlyRoot();
        TestRootOneToOneVoDedicated inserted = oneToOneVoDedicatedRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootOneToOneVoDedicated> deleted = oneToOneVoDedicatedRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootOneToOneVoDedicated> found = oneToOneVoDedicatedRepository.findResultById(
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

}

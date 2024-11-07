package io.domainlifecycles.jooq.persistence.tests.ignoring;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoring;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoringId;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleAggregateRootIgnoringRepository_ITest extends BasePersistence_ITest {

    private SimpleAggregateRootIgnoringRepository simpleAggregateRootRepository;

    @BeforeAll
    public void init() {
        simpleAggregateRootRepository = new SimpleAggregateRootIgnoringRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertSimpleEntity() {
        //given
        TestRootSimpleIgnoring trs = TestDataGenerator.buildTestRootSimpleIgnoring();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimpleIgnoring inserted = simpleAggregateRootRepository.insert(trs);
        //then
        Optional<TestRootSimpleIgnoring> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleIgnoringId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleEntity() {
        //given
        TestRootSimpleIgnoring trs = TestDataGenerator.buildTestRootSimpleIgnoring();
        TestRootSimpleIgnoring inserted = simpleAggregateRootRepository.insert(trs);
        TestRootSimpleIgnoring insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimpleIgnoring updated = simpleAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootSimpleIgnoring> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleIgnoringId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testDeleteSimpleEntity() {
        //given
        TestRootSimpleIgnoring trs = TestDataGenerator.buildTestRootSimpleIgnoring();
        TestRootSimpleIgnoring inserted = simpleAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootSimpleIgnoring> deleted = simpleAggregateRootRepository
            .deleteById(inserted.getId());
        //then
        Optional<TestRootSimpleIgnoring> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleIgnoringId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }


}

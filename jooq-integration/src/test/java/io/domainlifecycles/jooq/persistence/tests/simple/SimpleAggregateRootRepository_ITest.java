package io.domainlifecycles.jooq.persistence.tests.simple;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleAggregateRootRepository_ITest extends BasePersistence_ITest {

    private SimpleAggregateRootRepository simpleAggregateRootRepository;

    @BeforeAll
    public void init() {
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertSimpleEntity() {
        //given
        TestRootSimple trs = TestDataGenerator.buildTestRootSimple();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimple inserted = simpleAggregateRootRepository.insert(trs);
        //then
        Optional<TestRootSimple> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleEntity() {
        //given
        TestRootSimple trs = TestDataGenerator.buildTestRootSimple();
        TestRootSimple inserted = simpleAggregateRootRepository.insert(trs);
        TestRootSimple insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimple updated = simpleAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootSimple> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testDeleteSimpleEntity() {
        //given
        TestRootSimple trs = TestDataGenerator.buildTestRootSimple();
        TestRootSimple inserted = simpleAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootSimple> deleted = simpleAggregateRootRepository
            .deleteById(inserted.getId());
        //then
        Optional<TestRootSimple> found = simpleAggregateRootRepository
            .findResultById(new TestRootSimpleId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }


}

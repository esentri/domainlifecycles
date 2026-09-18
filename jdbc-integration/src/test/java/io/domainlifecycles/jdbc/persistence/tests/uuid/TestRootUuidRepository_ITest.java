package io.domainlifecycles.jdbc.persistence.tests.uuid;

import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.uuid.TestRootUuid;
import tests.shared.persistence.domain.uuid.TestVoUuid;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRootUuidRepository_ITest extends JdbcBasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestRootUuidRepository_ITest.class);

    private TestRootUuidRepository testRootUuidRepository;

    @BeforeAll
    public void init() {
        testRootUuidRepository = new TestRootUuidRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    @Test
    public void testInsertMin() {
        //given
        TestRootUuid r = TestDataGenerator.buildTestRootUuidMin();
        TestRootUuid copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        TestRootUuid inserted = testRootUuidRepository.insert(copy);


        //then
        Optional<TestRootUuid> found = testRootUuidRepository.findResultById(inserted.getId())
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);

        persistenceEventTestHelper.assertEvents();
    }



    @Test
    public void testInsertMax() {
        //given
        TestRootUuid r = TestDataGenerator.buildTestRootUuidMax();
        TestRootUuid copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootUuid inserted = testRootUuidRepository.insert(copy);

        //then
        Optional<TestRootUuid> found = testRootUuidRepository.findResultById(
            r.getId()).resultValue();
        assertThat(inserted == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        Assertions.assertThat(found.get().getVoList()).isNotEmpty();
        log.debug("Neue Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getVoList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getVoList().get(1), inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testDeleteMax() {
        //given
        TestRootUuid r = TestDataGenerator.buildTestRootUuidMax();
        TestRootUuid inserted = testRootUuidRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        testRootUuidRepository.deleteById(r.getId());

        //then
        Optional<TestRootUuid> found = testRootUuidRepository.findResultById(
            r.getId()).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getVoList().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getVoList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMaxAddVo() {
        //given
        TestRootUuid r = TestDataGenerator.buildTestRootUuidMax();
        TestRootUuid inserted = testRootUuidRepository.insert(r);
        TestRootUuid copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.getVoList().add(
            TestVoUuid.builder()
                .name("NEU3")
                .build()
        );
        persistenceEventTestHelper.resetEventsCaught();

        //when
        TestRootUuid updated = testRootUuidRepository.update(copy);

        //then
        Optional<TestRootUuid> found = testRootUuidRepository.findResultById(
            r.id()).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        log.debug("Neue VO Aggregate Root: \n" + found);



        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getVoList().get(2), updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }

}

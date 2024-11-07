package io.domainlifecycles.jooq.persistence.tests.records;

import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;
import tests.shared.persistence.domain.records.RecordVo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecordTestRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RecordTestRepository_ITest.class);

    private RecordTestRepository recordTestRepository;

    @BeforeAll
    public void init() {
        recordTestRepository = new RecordTestRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        ValidationDomainClassExtender.extend("tests");
    }

    @Test
    public void testInsert() {
        //given
        RecordTest trs = TestDataGenerator.buildRecordTest();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RecordTest inserted = recordTestRepository.insert(trs);
        //then
        Optional<RecordTest> found = recordTestRepository.findResultById(new RecordTestId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getMyVoList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getMyVoList().get(1), inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdate() {
        //given
        RecordTest trs = TestDataGenerator.buildRecordTest();
        RecordTest inserted = recordTestRepository.insert(trs);
        RecordTest insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setMyValue("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        RecordTest updated = recordTestRepository.update(insertedCopy);
        //then
        Optional<RecordTest> found = recordTestRepository.findResultById(new RecordTestId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getMyValue()).isEqualTo(insertedCopy.getMyValue());
    }

    @Test
    public void testUpdateRemoveVo() {
        //given
        RecordTest trs = TestDataGenerator.buildRecordTest();
        RecordTest inserted = recordTestRepository.insert(trs);
        RecordTest insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getMyVoList().remove(0);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        RecordTest updated = recordTestRepository.update(insertedCopy);
        //then
        Optional<RecordTest> found = recordTestRepository.findResultById(new RecordTestId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getMyVoList().get(0), inserted);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getMyVoList()).isEqualTo(insertedCopy.getMyVoList());
    }

    @Test
    public void testDelete() {
        //given
        RecordTest trs = TestDataGenerator.buildRecordTest();
        RecordTest inserted = recordTestRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<RecordTest> deleted = recordTestRepository.deleteById(inserted.getId());
        //then
        Optional<RecordTest> found = recordTestRepository.findResultById(new RecordTestId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getMyVoList().get(1), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getMyVoList().get(0), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void buildVoWithAssertionError() {
        var ex = assertThrows(DomainAssertionException.class, () -> RecordVo.builder()
            .setValue2(1l)
            .build());
        assertThat(ex.getMessage()).contains("value1");

    }

}

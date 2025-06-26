package io.domainlifecycles.autoconfig.features.single.persistence;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class PersistenceAutoConfigTest {

    private PersistenceEventTestHelper persistenceEventTestHelper;
    private SimpleAggregateRootRepository simpleAggregateRootRepository;

    @Autowired
    DSLContext dslContext;

    @Autowired
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @BeforeAll
    public void init() {
        persistenceEventTestHelper = new PersistenceEventTestHelper();
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            dslContext, persistenceEventTestHelper.testEventPublisher, jooqDomainPersistenceProvider
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
            .findResultById(new TestRootSimpleId(1L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }
}

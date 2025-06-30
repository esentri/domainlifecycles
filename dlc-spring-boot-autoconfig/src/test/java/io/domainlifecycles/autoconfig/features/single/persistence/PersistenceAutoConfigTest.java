package io.domainlifecycles.autoconfig.features.single.persistence;

import io.domainlifecycles.autoconfig.features.single.persistence.model.simple.TestRootSimple;
import io.domainlifecycles.autoconfig.features.single.persistence.model.simple.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles({"test"})
@Import(PersistenceAutoConfigTest.TestConfiguration.class)
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
        TestRootSimple trs = TestRootSimple.builder()
            .setId(new TestRootSimpleId(1L))
            .setName("TestRoot")
            .build();
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

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {
        @Bean
        DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
            return new InnerClassDomainObjectBuilderProvider();
        }
    }
}

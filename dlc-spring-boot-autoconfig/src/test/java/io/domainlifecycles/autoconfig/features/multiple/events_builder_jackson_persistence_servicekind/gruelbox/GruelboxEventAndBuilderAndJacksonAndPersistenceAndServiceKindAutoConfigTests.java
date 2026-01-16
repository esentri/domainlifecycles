package io.domainlifecycles.autoconfig.features.multiple.events_builder_jackson_persistence_servicekind.gruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.time.Duration;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;
import tools.jackson.databind.ObjectMapper;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;

@SpringBootTest(classes = TestApplicationGruelboxEventAndBuilderAndJacksonAndPersistenceAndServiceKindAutoConfiguration.class)
@Import(GruelboxEventAndBuilderAndJacksonAndPersistenceAndServiceKindAutoConfigTestConfiguration.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
@DirtiesContext(classMode = AFTER_CLASS)
@Execution(SAME_THREAD)
public class GruelboxEventAndBuilderAndJacksonAndPersistenceAndServiceKindAutoConfigTests {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired
    private DlcJacksonModule dlcJacksonModule;

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired
    private EntityIdentityProvider entityIdentityProvider;

    @Autowired(required = false)
    private DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    private ResponseEntityBuilder responseEntityBuilder;

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void testTransactionOutbox() {
        var val = new ADomainEvent("GruelboxEvent");
        transactionTemplate.executeWithoutResult((status) ->
            outbox.with()
                .ordered("topic1")
                .delayForAtLeast(Duration.ZERO)
                .schedule(AnApplicationService.class)
                .onADomainEvent(val)
        );

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    public void testTestRootSimpleJacksonMapping() {
        TestRootSimple testRootSimple = TestRootSimple.builder().setId(new TestRootSimpleId(1L)).setName("TEST").build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testRootSimple);

        TestRootSimple mappedTestRootSimple = objectMapper.readValue(json, TestRootSimple.class);
        Assertions.assertThat(mappedTestRootSimple).isEqualTo(testRootSimple);
    }

    @Test
    @Transactional
    public void testInsertSimpleEntity() {

        //given
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        SimpleAggregateRootRepository simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            dslContext, persistenceEventTestHelper.testEventPublisher, jooqDomainPersistenceProvider
        );

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

    @Test
    public void testBuild() {
        var aggregateRootTestBuilder = TestRootSimple.builder().setId(new TestRootSimpleId(5L)).setName("Test-Name");
        var innerBuilder = new InnerClassDomainObjectBuilder<>(aggregateRootTestBuilder);
        var built = innerBuilder.build();
        assertThat(built).isNotNull();
    }

    @Test
    void testBuilderProviderIsPresent() {
        assertThat(domainObjectBuilderProvider).isNotNull();
    }

    @Test
    public void testDlcJacksonModuleIsPresent() {
        assertThat(dlcJacksonModule).isNotNull();
    }

    @Test
    void testNoOtherBeansPresent() {
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}

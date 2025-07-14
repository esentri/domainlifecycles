package io.domainlifecycles.autoconfig.features.multiple.events_builder_jackson_persistence.spring;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.autoconfig.model.events.ADomainService;
import io.domainlifecycles.autoconfig.model.events.AQueryHandler;
import io.domainlifecycles.autoconfig.model.events.ARepository;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.autoconfig.model.events.AnOutboundService;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class SpringEventAndBuilderAndJacksonAndPersistenceAutoConfigTests {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AQueryHandler queryHandler;

    @Autowired
    private AnOutboundService outboundService;

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
    @DirtiesContext
    public void testIntegrationCommit() {
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).contains(evt);
                softly.assertThat(aRepository.received).contains(evt);
                softly.assertThat(anApplicationService.received).contains(evt);
                softly.assertThat(queryHandler.received).contains(evt);
                softly.assertThat(outboundService.received).contains(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testTestRootSimpleJacksonMapping() throws JsonProcessingException {
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

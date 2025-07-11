package io.domainlifecycles.autoconfig.features.single.persistence.config.annotation;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.autoconfig.features.single.persistence.PersistenceAutoConfigTestConfiguration;
import io.domainlifecycles.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Import(PersistenceAutoConfigTestConfiguration.class)
@ActiveProfiles({"test"})
public class PersistenceAutoConfigAnnotationValuesTest {

    private PersistenceEventTestHelper persistenceEventTestHelper;
    private SimpleAggregateRootRepository simpleAggregateRootRepository;

    @Autowired
    DSLContext dslContext;

    @Autowired
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired
    EntityIdentityProvider entityIdentityProvider;

    @Autowired(required = false)
    ServiceProvider serviceProvider;

    @Autowired(required = false)
    TransactionalHandlerExecutor transactionalHandlerExecutor;

    @Autowired(required = false)
    ClassProvider classProvider;

    @Autowired(required = false)
    DomainEventTypeBasedRouter router;

    @Autowired(required = false)
    ChannelRoutingConfiguration routingConfiguration;

    @Autowired(required = false)
    PublishingChannel publishingChannel;

    @Autowired(required = false)
    DlcJacksonModule dlcJacksonModule;

    @Autowired(required = false)
    DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    ResponseEntityBuilder responseEntityBuilder;

    @BeforeAll
    public void init() {
        persistenceEventTestHelper = new PersistenceEventTestHelper();
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            dslContext, persistenceEventTestHelper.testEventPublisher, jooqDomainPersistenceProvider
        );
    }

    @Test
    @Transactional
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

    @Test
    void testNoOtherBeansPresent() {
        assertThat(serviceProvider).isNull();
        assertThat(transactionalHandlerExecutor).isNull();
        assertThat(classProvider).isNull();
        assertThat(router).isNull();
        assertThat(routingConfiguration).isNull();
        assertThat(publishingChannel).isNull();
        assertThat(dlcJacksonModule).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}

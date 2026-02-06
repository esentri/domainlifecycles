package io.domainlifecycles.boot3.autoconfig.features.single.persistence.config.property;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.boot3.autoconfig.features.single.persistence.PersistenceAutoConfigTestConfiguration;
import io.domainlifecycles.boot3.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson2.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationPersistencePropertyValuesAutoConfig.class)
@Import(PersistenceAutoConfigTestConfiguration.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class PersistenceAutoConfigPropertyValuesTest {

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

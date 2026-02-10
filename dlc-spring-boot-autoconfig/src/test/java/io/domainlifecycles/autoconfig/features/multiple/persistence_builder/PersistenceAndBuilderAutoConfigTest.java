package io.domainlifecycles.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationPersistenceAndBuilderAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class PersistenceAndBuilderAutoConfigTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired(required = false)
    private ServiceProvider serviceProvider;

    @Autowired(required = false)
    private TransactionalHandlerExecutor transactionalHandlerExecutor;

    @Autowired(required = false)
    private ClassProvider classProvider;

    @Autowired(required = false)
    private DomainEventTypeBasedRouter router;

    @Autowired(required = false)
    private ChannelRoutingConfiguration routingConfiguration;

    @Autowired(required = false)
    private PublishingChannel publishingChannel;

    @Autowired(required = false)
    private DlcJacksonModule dlcJacksonModule;

    @Autowired(required = false)
    private DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    private ResponseEntityBuilder responseEntityBuilder;

    @Autowired(required = false)
    private List<ServiceKind> allServiceKinds;

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
    public void testBuild() {
        var aggregateRootTestBuilder = TestRootSimple.builder().setId(new TestRootSimpleId(5L)).setName("Test-Name");
        var innerBuilder = new InnerClassDomainObjectBuilder<>(aggregateRootTestBuilder);
        var built = innerBuilder.build();
        assertThat(built).isNotNull();
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
        assertThat(allServiceKinds).isNull();
    }
}

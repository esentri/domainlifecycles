package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.boot3.autoconfig.features.single.persistence.SimpleAggregateRootRepository;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson2.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationPersistenceAndBuilderAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class PersistenceAndBuilderAutoConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

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

    @Test
    void testTheTransactionCacheFollowsSpringsTransactions() {
        assertThat(jooqDomainPersistenceProvider.transactionCacheProvider)
            .isInstanceOf(SpringTransactionCacheProvider.class);
    }

    @Test
    void testOnlyTheJooqTransactionCacheProviderIsPresent_When_JdbcIntegrationIsOnTheClasspathToo() {
        //jdbc-integration is on the test classpath as well, but DlcJdbcPersistenceAutoConfiguration backs off
        //once the jOOQ DomainPersistenceProvider exists - its transaction cache provider included, which nothing
        //would use
        assertThat(applicationContext.getBeansOfType(TransactionCacheProvider.class))
            .containsOnlyKeys("dlcTransactionCacheProvider");
        assertThat(applicationContext.getBean(TransactionCacheProvider.class))
            .isSameAs(applicationContext.getBean("dlcTransactionCacheProvider"));
    }

    @Test
    void testNoConnectionIsLeftOpen_When_TheRepositoryIsUsedWithoutATransaction() {
        //given
        SimpleAggregateRootRepository repository = repositoryForConnectionChecks();
        TestRootSimpleId id = new TestRootSimpleId(4712L);
        //committed right away without a transaction, so a run aborted halfway may have left it behind
        repository.deleteById(id);

        //when: every operation gets a connection of its own from the pool
        repository.insert(TestRootSimple.builder().setId(id).setName("WithoutTransaction").build());
        TestRootSimple found = repository.findById(id).orElseThrow();
        found.setName("WithoutTransactionUpdated");
        repository.update(found);
        repository.deleteById(id);

        //then: ... and hands it back
        assertThat(activeConnections()).isZero();
        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void testNoConnectionIsLeftOpen_When_ATransactionIsCommittedOrRolledBack() {
        //given
        SimpleAggregateRootRepository repository = repositoryForConnectionChecks();
        TestRootSimpleId id = new TestRootSimpleId(4713L);
        repository.deleteById(id);
        repository.insert(TestRootSimple.builder().setId(id).setName("BeforeTransaction").build());
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        TransactionCacheProvider<?> cacheProvider =
            applicationContext.getBean("dlcTransactionCacheProvider", TransactionCacheProvider.class);

        //when
        transaction.executeWithoutResult(status -> {
            TestRootSimple found = repository.findById(id).orElseThrow();
            assertThat(cacheProvider.currentTransactionCache()).isPresent();
            found.setName("Committed");
            repository.update(found);
        });
        assertThat(activeConnections()).isZero();
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
        transaction.executeWithoutResult(status -> {
            TestRootSimple found = repository.findById(id).orElseThrow();
            found.setName("RolledBack");
            repository.update(found);
            status.setRollbackOnly();
        });

        //then
        assertThat(activeConnections()).isZero();
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
        assertThat(repository.findById(id).orElseThrow().getName()).isEqualTo("Committed");
        repository.deleteById(id);
    }

    private SimpleAggregateRootRepository repositoryForConnectionChecks() {
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        persistenceEventTestHelper.resetEventsCaught();
        return new SimpleAggregateRootRepository(
            dslContext, persistenceEventTestHelper.testEventPublisher, jooqDomainPersistenceProvider);
    }

    private int activeConnections() {
        assertThat(dataSource).isInstanceOf(HikariDataSource.class);
        return ((HikariDataSource) dataSource).getHikariPoolMXBean().getActiveConnections();
    }
}

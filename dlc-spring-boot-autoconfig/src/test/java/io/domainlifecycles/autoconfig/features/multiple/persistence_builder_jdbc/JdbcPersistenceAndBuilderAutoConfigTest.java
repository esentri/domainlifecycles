package io.domainlifecycles.autoconfig.features.multiple.persistence_builder_jdbc;

import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import io.domainlifecycles.test.autoconfig.jdbc.SimpleJdbcAggregateRootRepository;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers {@code DlcJdbcPersistenceAutoConfiguration}, the plain-JDBC counterpart of
 * {@code PersistenceAndBuilderAutoConfigTest}: excludes {@code DlcJooqPersistenceAutoConfiguration} (see
 * {@link TestApplicationJdbcPersistenceAndBuilderAutoConfig}) so this is the only persistence autoconfig
 * active, reusing the exact same database/migration/domain model as the jOOQ test - {@code jdbc-integration}
 * needs no generated record classes, so nothing else has to change to point it at the same schema.
 */
@SpringBootTest(classes = TestApplicationJdbcPersistenceAndBuilderAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JdbcPersistenceAndBuilderAutoConfigTest {

    @Autowired
    private JdbcConnectionProvider jdbcConnectionProvider;

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired
    private JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Test
    @Transactional
    void testInsertSimpleEntity() {
        //given
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        SimpleJdbcAggregateRootRepository repository = new SimpleJdbcAggregateRootRepository(
            persistenceEventTestHelper.testEventPublisher, jdbcDomainPersistenceProvider
        );

        TestRootSimple trs = TestRootSimple.builder()
            .setId(new TestRootSimpleId(2L))
            .setName("TestRootJdbc")
            .build();
        persistenceEventTestHelper.resetEventsCaught();

        //when
        TestRootSimple inserted = repository.insert(trs);

        //then
        Optional<TestRootSimple> found = repository.findResultById(new TestRootSimpleId(2L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    void testBuilderProviderIsPresent() {
        assertThat(domainObjectBuilderProvider).isNotNull();
    }

    @Test
    void testDlcOwnJdbcWiringIsActiveByDefault() {
        //the transaction cache of jdbc-integration follows Spring's transactions, wired by
        //DlcJdbcPersistenceAutoConfiguration automatically - without a provider, the cache would be off
        assertThat(jdbcDomainPersistenceProvider.transactionCacheProvider)
            .isInstanceOf(SpringTransactionCacheProvider.class);
    }

    @Test
    void testTheJdbcTransactionCacheProviderIsPresent_When_JdbcIsTheOnlyPersistenceBackend() {
        assertThat(applicationContext.getBeansOfType(TransactionCacheProvider.class))
            .containsOnlyKeys("dlcJdbcTransactionCacheProvider");
    }

    @Test
    void testNoConnectionIsLeftOpen_When_TheRepositoryIsUsedWithoutATransaction() {
        //given
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        persistenceEventTestHelper.resetEventsCaught();
        SimpleJdbcAggregateRootRepository repository = new SimpleJdbcAggregateRootRepository(
            persistenceEventTestHelper.testEventPublisher, jdbcDomainPersistenceProvider
        );
        TestRootSimpleId id = new TestRootSimpleId(4711L);
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
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        persistenceEventTestHelper.resetEventsCaught();
        SimpleJdbcAggregateRootRepository repository = new SimpleJdbcAggregateRootRepository(
            persistenceEventTestHelper.testEventPublisher, jdbcDomainPersistenceProvider
        );
        TestRootSimpleId id = new TestRootSimpleId(4713L);
        repository.deleteById(id);
        repository.insert(TestRootSimple.builder().setId(id).setName("BeforeTransaction").build());
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        TransactionCacheProvider<?> cacheProvider =
            applicationContext.getBean("dlcJdbcTransactionCacheProvider", TransactionCacheProvider.class);

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

    private int activeConnections() {
        assertThat(dataSource).isInstanceOf(HikariDataSource.class);
        return ((HikariDataSource) dataSource).getHikariPoolMXBean().getActiveConnections();
    }
}

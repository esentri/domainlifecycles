package io.domainlifecycles.jdbc.persistence.tests.simple;

import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.simple.TestRootSimple;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JDBC-side counterpart of jooq-integration's {@code SimpleAggregateRootRepository_TransactionCache_ITest} -
 * covers the same design section (3.3.1 of {@code persistence/docs/transaction-cache-design.md}) through
 * {@code jdbc-integration}'s independent repository/fetcher implementation, since the cache mechanics
 * themselves are shared, but the code paths exercising them are not.
 * <p>
 * Plain JDBC has no transaction listener of its own, so this test opens and closes the scopes itself, via
 * {@link ThreadBoundTransactionCacheProvider#open()} - as an application driving its own JDBC transactions does. It
 * exercises the real repository/fetcher code path (not mocks) for the invalidation rule and the SELECT-avoidance
 * behavior with a precisely controlled scope.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleAggregateRootRepository_TransactionCache_ITest extends JdbcBasePersistence_ITest {

    private SimpleAggregateRootRepository simpleAggregateRootRepository;

    private final AtomicInteger selectCount = new AtomicInteger();

    @SuppressWarnings("unchecked")
    private ThreadBoundTransactionCacheProvider<JdbcRecord> cacheProvider() {
        return (ThreadBoundTransactionCacheProvider<JdbcRecord>)
            persistenceConfiguration.domainPersistenceProvider.transactionCacheProvider;
    }

    @BeforeAll
    public void init() {
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    /**
     * Replaces the connection {@code JdbcTestPersistenceConfiguration.connectionProvider} currently hands
     * out with a counting proxy, via reflection - there is no jOOQ-style {@code ExecuteListener} hook for
     * plain JDBC, and the connection provider is a simple {@code () -> currentConnection} lambda baked into
     * an already-constructed {@code JdbcDomainPersistenceProvider}, so the field backing it is swapped
     * directly instead.
     */
    @BeforeEach
    public void wrapConnectionForSelectCounting() throws Exception {
        selectCount.set(0);
        Field field = persistenceConfiguration.getClass().getDeclaredField("currentConnection");
        field.setAccessible(true);
        Connection real = (Connection) field.get(persistenceConfiguration);
        Connection counting = (Connection) Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class<?>[]{Connection.class},
            (proxyInstance, method, args) -> {
                if (("prepareStatement".equals(method.getName()) || "prepareCall".equals(method.getName()))
                    && args != null && args.length > 0 && args[0] instanceof String sql
                    && sql.trim().regionMatches(true, 0, "select", 0, "select".length())) {
                    selectCount.incrementAndGet();
                }
                return method.invoke(real, args);
            });
        field.set(persistenceConfiguration, counting);
    }

    @Test
    public void updateOnACacheMissLeavesNoStaleEntryBehind() {
        TestRootSimple inserted;
        try (var scope = cacheProvider().open()) {
            inserted = simpleAggregateRootRepository.insert(TestDataGenerator.buildTestRootSimple());
        }
        AggregateCacheKey key = AggregateCacheSupport.keyFor(
            persistenceConfiguration.domainPersistenceProvider, inserted);

        try (var scope = cacheProvider().open()) {
            //nothing was fetched into this fresh scope yet, so update() below must go through its
            //take()-miss -> fallback fetch -> populate -> invalidate path
            var toUpdate = persistenceEventTestHelper.kryo.copy(inserted);
            toUpdate.setName("UPDATED");

            simpleAggregateRootRepository.update(toUpdate);

            var cache = cacheProvider().currentTransactionCache().orElseThrow();
            Assertions.assertThat(cache.take(key))
                .as("the fallback fetch inside update() must not leave a stale, pre-write entry behind")
                .isEmpty();
        }
    }

    @Test
    public void deleteByIdOnACacheMissLeavesNoStaleEntryBehind() {
        TestRootSimple inserted;
        try (var scope = cacheProvider().open()) {
            inserted = simpleAggregateRootRepository.insert(TestDataGenerator.buildTestRootSimple());
        }
        AggregateCacheKey key = AggregateCacheSupport.keyFor(
            persistenceConfiguration.domainPersistenceProvider, inserted);

        try (var scope = cacheProvider().open()) {
            simpleAggregateRootRepository.deleteById(inserted.getId());

            var cache = cacheProvider().currentTransactionCache().orElseThrow();
            Assertions.assertThat(cache.take(key))
                .as("the fallback fetch inside deleteById() must not leave a stale entry for the now-deleted "
                    + "aggregate behind")
                .isEmpty();
        }
    }

    @Test
    public void updateOnACacheHitStillLeavesNoStaleEntryBehind() {
        TestRootSimple inserted;
        try (var scope = cacheProvider().open()) {
            inserted = simpleAggregateRootRepository.insert(TestDataGenerator.buildTestRootSimple());
        }
        AggregateCacheKey key = AggregateCacheSupport.keyFor(
            persistenceConfiguration.domainPersistenceProvider, inserted);

        try (var scope = cacheProvider().open()) {
            //populates the cache entry that update() below is then expected to consume as a hit
            simpleAggregateRootRepository.findResultById(inserted.getId());
            Assertions.assertThat(cacheProvider().currentTransactionCache().orElseThrow().take(key))
                .as("the finder call above should have populated the cache")
                .isPresent();
            //take() above already removed it again - repopulate for the actual scenario under test
            simpleAggregateRootRepository.findResultById(inserted.getId());

            var toUpdate = persistenceEventTestHelper.kryo.copy(inserted);
            toUpdate.setName("UPDATED");
            simpleAggregateRootRepository.update(toUpdate);

            var cache = cacheProvider().currentTransactionCache().orElseThrow();
            Assertions.assertThat(cache.take(key)).isEmpty();
        }
    }

    @Test
    public void findThenUpdateWithinAnOpenScopeIssuesOnlyOneSelect() {
        TestRootSimple inserted;
        try (var scope = cacheProvider().open()) {
            inserted = simpleAggregateRootRepository.insert(TestDataGenerator.buildTestRootSimple());
        }

        try (var scope = cacheProvider().open()) {
            selectCount.set(0);

            simpleAggregateRootRepository.findResultById(inserted.getId());
            var toUpdate = persistenceEventTestHelper.kryo.copy(inserted);
            toUpdate.setName("UPDATED");
            simpleAggregateRootRepository.update(toUpdate);

            Assertions.assertThat(selectCount.get())
                .as("update() should have reused the finder's cached fetch instead of issuing its own SELECT")
                .isEqualTo(1);
        }
    }

    @Test
    public void findThenUpdateWithoutAnOpenScopeIssuesTwoSelects() {
        TestRootSimple inserted;
        try (var scope = cacheProvider().open()) {
            inserted = simpleAggregateRootRepository.insert(TestDataGenerator.buildTestRootSimple());
        }
        //no scope open here - the transaction cache feature is dormant, exactly as if it did not exist
        selectCount.set(0);

        simpleAggregateRootRepository.findResultById(inserted.getId());
        var toUpdate = persistenceEventTestHelper.kryo.copy(inserted);
        toUpdate.setName("UPDATED");
        simpleAggregateRootRepository.update(toUpdate);

        Assertions.assertThat(selectCount.get())
            .as("without an open transaction cache scope, update() must always fetch its own current state")
            .isEqualTo(2);
    }
}

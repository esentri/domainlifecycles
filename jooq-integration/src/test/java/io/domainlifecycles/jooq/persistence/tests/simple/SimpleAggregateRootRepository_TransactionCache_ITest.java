package io.domainlifecycles.jooq.persistence.tests.simple;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.assertj.core.api.Assertions;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecuteType;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.simple.TestRootSimple;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Covers design section 3.3.1 of {@code persistence/docs/transaction-cache-design.md}: a cache-miss inside
 * {@code update()}/{@code deleteById()} triggers a fallback fetch that (via {@code InternalAggregateFetcher}'s
 * populate hook) re-adds an entry for the very key that fetch's own caller is about to write - this must be
 * invalidated unconditionally, or it would linger as a stale, pre-write snapshot for the rest of the
 * transaction.
 * <p>
 * {@code BasePersistence_ITest} drives the connection directly (raw JDBC {@code commit()}/{@code rollback()}
 * are never called on it, and jOOQ's own {@code dslContext.transaction(...)} API is never used), so the
 * {@code TransactionCacheJooqBinder} - already covered on its own in {@code TransactionCacheJooqBinderTest} -
 * never opens a scope here. This test therefore opens/closes the {@link ThreadBoundTransactionCacheProvider}
 * scope directly, so it can exercise the real fetch/repository code path (not mocks) for the invalidation
 * rule itself.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleAggregateRootRepository_TransactionCache_ITest extends BasePersistence_ITest {

    private SimpleAggregateRootRepository simpleAggregateRootRepository;

    @SuppressWarnings("unchecked")
    private ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> cacheProvider() {
        return (ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>)
            persistenceConfiguration.domainPersistenceProvider.transactionCacheProvider;
    }

    private final AtomicInteger selectCount = new AtomicInteger();

    private ExecuteListenerProvider[] originalExecuteListenerProviders;

    @BeforeAll
    public void init() {
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @BeforeEach
    public void countSelects() {
        var configuration = persistenceConfiguration.dslContext.configuration();
        originalExecuteListenerProviders = configuration.executeListenerProviders();
        selectCount.set(0);
        ExecuteListener countingListener = new ExecuteListener() {
            @Override
            public void executeStart(ExecuteContext ctx) {
                if (ctx.type() == ExecuteType.READ) {
                    selectCount.incrementAndGet();
                }
            }
        };
        configuration.setAppending(new DefaultExecuteListenerProvider(countingListener));
    }

    @AfterEach
    public void restoreExecuteListeners() {
        persistenceConfiguration.dslContext.configuration().set(originalExecuteListenerProviders);
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

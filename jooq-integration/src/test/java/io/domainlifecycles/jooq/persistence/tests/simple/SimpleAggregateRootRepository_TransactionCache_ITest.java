package io.domainlifecycles.jooq.persistence.tests.simple;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.assertj.core.api.Assertions;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.simple.TestRootSimple;

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

    @BeforeAll
    public void init() {
        simpleAggregateRootRepository = new SimpleAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
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
}

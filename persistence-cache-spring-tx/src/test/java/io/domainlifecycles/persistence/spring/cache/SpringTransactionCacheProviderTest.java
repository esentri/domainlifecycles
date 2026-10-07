/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.persistence.spring.cache;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.SavepointManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionTemplate;

import io.domainlifecycles.persistence.cache.TransactionCache;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Runs the provider in Spring's real transaction handling - propagation, suspension, savepoints - on a transaction
 * manager without a database.
 */
class SpringTransactionCacheProviderTest {

    private static final AggregateCacheKey KEY = new AggregateCacheKey("some.Aggregate", new TestId(1L));

    private final SpringTransactionCacheProvider<Object> provider = new SpringTransactionCacheProvider<>();

    private final InMemoryTransactionManager transactionManager = new InMemoryTransactionManager();

    private TransactionTemplate transaction(int propagation) {
        var template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(propagation);
        return template;
    }

    private TransactionTemplate transaction() {
        return transaction(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    @Test
    void thereIsNoCacheOutsideATransaction() {
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void thereIsNoCacheWhereOnlySynchronizationIsActiveWithoutATransaction() {
        transaction(TransactionDefinition.PROPAGATION_SUPPORTS)
            .executeWithoutResult(status -> assertThat(provider.currentTransactionCache()).isEmpty());
    }

    @Test
    void aTransactionKeepsOneCacheForAllItsOperations() {
        transaction().executeWithoutResult(status -> {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(KEY, new FetcherResult<>(null, null));

            assertThat(provider.currentTransactionCache()).containsSame(cache);
            assertThat(cache.take(KEY)).isPresent();
        });
    }

    @Test
    void aCommittedTransactionLeavesNothingForTheNextOne() {
        var cache = transaction().execute(status -> {
            var current = provider.currentTransactionCache().orElseThrow();
            current.put(KEY, new FetcherResult<>(null, null));
            return current;
        });

        assertThat(cache.take(KEY)).as("emptied on completion").isEmpty();
        assertThat(provider.currentTransactionCache()).isEmpty();
        transaction().executeWithoutResult(status -> {
            assertThat(provider.currentTransactionCache()).isPresent().get().isNotSameAs(cache);
            assertThat(provider.currentTransactionCache().orElseThrow().take(KEY)).isEmpty();
        });
    }

    @Test
    void aRolledBackTransactionLeavesNothingForTheNextOne() {
        var cache = transaction().execute(status -> {
            var current = provider.currentTransactionCache().orElseThrow();
            current.put(KEY, new FetcherResult<>(null, null));
            status.setRollbackOnly();
            return current;
        });

        assertThat(cache.take(KEY)).as("emptied on completion").isEmpty();
        transaction().executeWithoutResult(
            status -> assertThat(provider.currentTransactionCache().orElseThrow().take(KEY)).isEmpty());
    }

    @Test
    void aSuspendedTransactionGetsItsOwnCacheBackOnResume() {
        transaction().executeWithoutResult(outer -> {
            var outerCache = provider.currentTransactionCache().orElseThrow();
            outerCache.put(KEY, new FetcherResult<>(null, null));

            transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(inner -> {
                var innerCache = provider.currentTransactionCache().orElseThrow();
                assertThat(innerCache).isNotSameAs(outerCache);
                assertThat(innerCache.take(KEY)).as("nothing of the suspended transaction").isEmpty();
                innerCache.put(KEY, new FetcherResult<>(null, null));
                inner.setRollbackOnly();
            });

            assertThat(provider.currentTransactionCache()).containsSame(outerCache);
            assertThat(outerCache.take(KEY)).as("what the outer transaction loaded").isPresent();
        });
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aTransactionSuspendedWithoutACacheYetGetsOneOfItsOwnAfterwards() {
        transaction().executeWithoutResult(outer -> {
            var innerCache = transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).execute(inner -> {
                var cache = provider.currentTransactionCache().orElseThrow();
                cache.put(KEY, new FetcherResult<>(null, null));
                return cache;
            });

            var outerCache = provider.currentTransactionCache().orElseThrow();
            assertThat(outerCache).isNotSameAs(innerCache);
            assertThat(outerCache.take(KEY)).isEmpty();
        });
    }

    @Test
    void aRollbackToASavepointClearsTheCacheOfTheTransaction() {
        transaction().executeWithoutResult(outer -> {
            var cache = provider.currentTransactionCache().orElseThrow();

            assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_NESTED).executeWithoutResult(nested -> {
                assertThat(provider.currentTransactionCache()).as("the same transaction").containsSame(cache);
                cache.put(KEY, new FetcherResult<>(null, null));
                throw new IllegalStateException("roll back to the savepoint");
            })).isInstanceOf(IllegalStateException.class);

            assertThat(provider.currentTransactionCache()).containsSame(cache);
            assertThat(cache.take(KEY)).as("loaded since the savepoint").isEmpty();
        });
    }

    @Test
    void aReleasedSavepointKeepsTheCache() {
        transaction().executeWithoutResult(outer -> {
            var cache = provider.currentTransactionCache().orElseThrow();

            transaction(TransactionDefinition.PROPAGATION_NESTED)
                .executeWithoutResult(nested -> cache.put(KEY, new FetcherResult<>(null, null)));

            assertThat(cache.take(KEY)).isPresent();
        });
    }

    @Test
    void thereIsNoCacheWhileATransactionIsSuspendedForCodeRunningWithoutOne() {
        transaction().executeWithoutResult(outer -> {
            var outerCache = provider.currentTransactionCache().orElseThrow();

            transaction(TransactionDefinition.PROPAGATION_NOT_SUPPORTED)
                .executeWithoutResult(none -> assertThat(provider.currentTransactionCache()).isEmpty());

            assertThat(provider.currentTransactionCache()).containsSame(outerCache);
        });
    }

    @Test
    void transactionsSuspendedOverSeveralLevelsGetTheirOwnCachesBack() {
        transaction().executeWithoutResult(first -> {
            var firstCache = provider.currentTransactionCache().orElseThrow();
            transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(second -> {
                var secondCache = provider.currentTransactionCache().orElseThrow();
                transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(third -> {
                    var thirdCache = provider.currentTransactionCache().orElseThrow();
                    assertThat(thirdCache).isNotSameAs(secondCache).isNotSameAs(firstCache);
                });
                assertThat(provider.currentTransactionCache()).containsSame(secondCache);
            });
            assertThat(provider.currentTransactionCache()).containsSame(firstCache);
        });
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aFailingParticipatingTransactionCaughtByTheOuterOneLeavesNothingForTheNextOne() {
        var cache = new AtomicReference<TransactionCache<Object>>();

        assertThatThrownBy(() -> transaction().executeWithoutResult(outer -> {
            cache.set(provider.currentTransactionCache().orElseThrow());
            assertThatThrownBy(() -> transaction().executeWithoutResult(participating -> {
                assertThat(provider.currentTransactionCache()).as("the same transaction").containsSame(cache.get());
                cache.get().put(KEY, new FetcherResult<>(null, null));
                throw new IllegalStateException("fails");
            })).isInstanceOf(IllegalStateException.class);
            assertThat(provider.currentTransactionCache()).containsSame(cache.get());
        })).isInstanceOf(UnexpectedRollbackException.class);

        assertThat(cache.get().take(KEY)).as("emptied on the rollback").isEmpty();
        transaction().executeWithoutResult(
            next -> assertThat(provider.currentTransactionCache()).isPresent().get().isNotSameAs(cache.get()));
    }

    @Test
    void aCommitFailingInAnotherSynchronizationLeavesNothingForTheNextOne() {
        var cache = new AtomicReference<TransactionCache<Object>>();

        assertThatThrownBy(() -> transaction().executeWithoutResult(status -> {
            cache.set(provider.currentTransactionCache().orElseThrow());
            cache.get().put(KEY, new FetcherResult<>(null, null));
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    throw new IllegalStateException("commit fails");
                }
            });
        })).isInstanceOf(IllegalStateException.class);

        assertThat(cache.get().take(KEY)).isEmpty();
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aTransactionNeverReusesTheCacheOfOneThatEndedWithoutCompletingIt() {
        // a transaction whose synchronizations are cleared without completing them, e.g. because the transaction
        // manager completed it on another thread: the cache stays bound to this thread
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        var stale = provider.currentTransactionCache().orElseThrow();
        stale.put(KEY, new FetcherResult<>(null, null));
        TransactionSynchronizationManager.clearSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(false);

        try {
            assertThat(provider.currentTransactionCache()).as("no transaction").isEmpty();
            transaction().executeWithoutResult(next -> {
                var cache = provider.currentTransactionCache().orElseThrow();
                assertThat(cache).isNotSameAs(stale);
                assertThat(cache.take(KEY)).isEmpty();
            });
            assertThat(provider.currentTransactionCache()).isEmpty();
        } finally {
            TransactionSynchronizationManager.unbindResourceIfPossible(provider);
        }
    }

    @Test
    void aTransactionCompletedOnAnotherThreadLeavesNothingForTheNextOne() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));
        var synchronizations = TransactionSynchronizationManager.getSynchronizations();
        TransactionSynchronizationManager.clearSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(false);

        // e.g. a JTA transaction manager rolling back on a timeout
        var completion = new Thread(() -> synchronizations.forEach(
            synchronization -> synchronization.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK)));
        completion.start();
        completion.join();

        try {
            assertThat(cache.take(KEY)).as("emptied on completion").isEmpty();
            transaction().executeWithoutResult(
                next -> assertThat(provider.currentTransactionCache()).isPresent().get().isNotSameAs(cache));
        } finally {
            TransactionSynchronizationManager.unbindResourceIfPossible(provider);
        }
    }

    @Test
    void transactionsRunningInParallelNeverShareACache() throws Exception {
        var caches = new ConcurrentLinkedQueue<TransactionCache<Object>>();
        var allInTheirTransaction = new CountDownLatch(4);
        var executor = Executors.newFixedThreadPool(4);
        try {
            var runs = new ArrayList<Future<?>>();
            for (int i = 0; i < 4; i++) {
                runs.add(executor.submit(() -> transaction().executeWithoutResult(status -> {
                    var cache = provider.currentTransactionCache().orElseThrow();
                    caches.add(cache);
                    allInTheirTransaction.countDown();
                    try {
                        allInTheirTransaction.await(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    assertThat(provider.currentTransactionCache()).containsSame(cache);
                })));
            }
            for (var run : runs) {
                run.get(10, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
        }

        assertThat(caches).hasSize(4).doesNotHaveDuplicates();
    }

    @Test
    void thereIsNoCacheWithoutTheSavepointCallbacksOfSpring6Dot2() {
        // Spring before 6.2 does not report a rollback to a savepoint
        var withoutSavepointCallbacks = new SpringTransactionCacheProvider<Object>(256, false);

        transaction().executeWithoutResult(
            status -> assertThat(withoutSavepointCallbacks.currentTransactionCache()).isEmpty());
    }

    @Test
    void anApplicationWritingBypassingTheRepositoriesCanEmptyTheCache() {
        transaction().executeWithoutResult(status -> {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(KEY, new FetcherResult<>(null, null));

            provider.currentTransactionCache().ifPresent(TransactionCache::clear);

            assertThat(provider.currentTransactionCache()).containsSame(cache);
            assertThat(cache.take(KEY)).isEmpty();
        });
    }

    @Test
    void theLeastRecentlyUsedEntryIsEvictedBeyondTheMaximalSize() {
        var small = new SpringTransactionCacheProvider<Object>(1);
        var other = new AggregateCacheKey("some.Aggregate", new TestId(2L));

        transaction().executeWithoutResult(status -> {
            var cache = small.currentTransactionCache().orElseThrow();
            cache.put(KEY, new FetcherResult<>(null, null));
            cache.put(other, new FetcherResult<>(null, null));

            assertThat(cache.take(KEY)).isEmpty();
            assertThat(cache.take(other)).isPresent();
        });
    }

    @Test
    void clearingEmptiesTheCacheOfTheTransactionAndKeepsItInUse() {
        transaction().executeWithoutResult(status -> {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(KEY, new FetcherResult<>(null, null));

            provider.clearCurrentTransactionCache();

            assertThat(provider.currentTransactionCache()).containsSame(cache);
            assertThat(cache.take(KEY)).isEmpty();
        });
    }

    @Test
    void clearingCreatesNoCacheAndDoesNothingWithoutATransaction() {
        provider.clearCurrentTransactionCache();

        transaction().executeWithoutResult(status -> {
            provider.clearCurrentTransactionCache();
            assertThat(TransactionSynchronizationManager.getResource(provider)).as("no cache created by clearing")
                .isNull();
        });
    }

    @Test
    void theMaximalSizeMustBePositive() {
        assertThatThrownBy(() -> new SpringTransactionCacheProvider<>(0)).isInstanceOf(IllegalArgumentException.class);
    }

    private record TestId(Long value) implements Identity<Long> {
    }

    /**
     * A transaction manager without a database: Spring's own propagation handling on top of transactions that only
     * exist on the current thread, with savepoints for nested transactions.
     */
    private static final class InMemoryTransactionManager extends AbstractPlatformTransactionManager {

        private final ThreadLocal<InMemoryTransaction> current = new ThreadLocal<>();

        private InMemoryTransactionManager() {
            setNestedTransactionAllowed(true);
        }

        @Override
        protected Object doGetTransaction() {
            return new TransactionHandle(current.get());
        }

        @Override
        protected boolean isExistingTransaction(Object transaction) {
            return ((TransactionHandle) transaction).existing != null;
        }

        @Override
        protected void doBegin(Object transaction, TransactionDefinition definition) {
            var begun = new InMemoryTransaction();
            ((TransactionHandle) transaction).existing = begun;
            current.set(begun);
        }

        @Override
        protected Object doSuspend(Object transaction) {
            var suspended = current.get();
            current.remove();
            ((TransactionHandle) transaction).existing = null;
            return suspended;
        }

        @Override
        protected void doResume(Object transaction, Object suspendedResources) {
            current.set((InMemoryTransaction) suspendedResources);
        }

        @Override
        protected void doCommit(DefaultTransactionStatus status) {
            // nothing to write
        }

        @Override
        protected void doRollback(DefaultTransactionStatus status) {
            // nothing to undo
        }

        @Override
        protected void doSetRollbackOnly(DefaultTransactionStatus status) {
            ((TransactionHandle) status.getTransaction()).existing.rollbackOnly = true;
        }

        @Override
        protected void doCleanupAfterCompletion(Object transaction) {
            current.remove();
        }
    }

    private static final class TransactionHandle implements SavepointManager, SmartTransactionObject {

        private InMemoryTransaction existing;

        private TransactionHandle(InMemoryTransaction existing) {
            this.existing = existing;
        }

        @Override
        public Object createSavepoint() {
            return new Object();
        }

        @Override
        public void rollbackToSavepoint(Object savepoint) {
            // nothing to undo
        }

        @Override
        public void releaseSavepoint(Object savepoint) {
            // nothing to release
        }

        @Override
        public boolean isRollbackOnly() {
            return existing != null && existing.rollbackOnly;
        }

        @Override
        public void flush() {
            // nothing to flush
        }
    }

    private static final class InMemoryTransaction {

        private boolean rollbackOnly;
    }
}

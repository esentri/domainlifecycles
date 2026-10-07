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

package io.domainlifecycles.persistence.jta.cache;

import com.atomikos.icatch.jta.TransactionSynchronizationRegistryImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.domainlifecycles.persistence.cache.TransactionCache;
import jakarta.transaction.Synchronization;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JtaTransactionCacheProviderTest {

    private static final AggregateCacheKey KEY = new AggregateCacheKey("some.Aggregate", new TestId(1L));

    private UserTransactionManager transactionManager;

    private JtaTransactionCacheProvider<Object> provider;

    @BeforeAll
    void startTransactionManager() throws Exception {
        System.setProperty("com.atomikos.icatch.log_base_dir", "build/atomikos");
        System.setProperty("com.atomikos.icatch.output_dir", "build/atomikos");
        transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(true);
        transactionManager.init();
        provider = new JtaTransactionCacheProvider<>(new TransactionSynchronizationRegistryImp());
    }

    @AfterAll
    void stopTransactionManager() {
        transactionManager.close();
    }

    @AfterEach
    void endAnyTransactionLeftOpen() throws Exception {
        if (transactionManager.getTransaction() != null) {
            transactionManager.rollback();
        }
    }

    @Test
    void thereIsNoCacheOutsideATransaction() {
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aTransactionKeepsOneCacheForAllItsOperations() throws Exception {
        transactionManager.begin();

        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));

        assertThat(provider.currentTransactionCache()).containsSame(cache);
        assertThat(cache.take(KEY)).isPresent();
        transactionManager.commit();
    }

    @Test
    void aCommittedTransactionLeavesNothingForTheNextOne() throws Exception {
        transactionManager.begin();
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));
        transactionManager.commit();

        assertThat(cache.take(KEY)).as("emptied on completion").isEmpty();
        assertThat(provider.currentTransactionCache()).isEmpty();
        transactionManager.begin();
        assertThat(provider.currentTransactionCache()).isPresent().get().isNotSameAs(cache);
        assertThat(provider.currentTransactionCache().orElseThrow().take(KEY)).isEmpty();
        transactionManager.commit();
    }

    @Test
    void aRolledBackTransactionLeavesNothingForTheNextOne() throws Exception {
        transactionManager.begin();
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));
        transactionManager.rollback();

        assertThat(cache.take(KEY)).as("emptied on completion").isEmpty();
        transactionManager.begin();
        assertThat(provider.currentTransactionCache().orElseThrow().take(KEY)).isEmpty();
        transactionManager.commit();
    }

    @Test
    void aSuspendedTransactionGetsItsOwnCacheBackOnResume() throws Exception {
        transactionManager.begin();
        var outerCache = provider.currentTransactionCache().orElseThrow();
        outerCache.put(KEY, new FetcherResult<>(null, null));

        var outer = transactionManager.suspend();
        assertThat(provider.currentTransactionCache()).as("no transaction while suspended").isEmpty();
        transactionManager.begin();
        var innerCache = provider.currentTransactionCache().orElseThrow();
        assertThat(innerCache).isNotSameAs(outerCache);
        assertThat(innerCache.take(KEY)).as("nothing of the suspended transaction").isEmpty();
        innerCache.put(KEY, new FetcherResult<>(null, null));
        transactionManager.rollback();
        transactionManager.resume(outer);

        assertThat(provider.currentTransactionCache()).containsSame(outerCache);
        assertThat(outerCache.take(KEY)).as("what the outer transaction loaded").isPresent();
        transactionManager.commit();
    }

    @Test
    void thereIsNoCacheInATransactionMarkedForRollback() throws Exception {
        transactionManager.begin();
        transactionManager.setRollbackOnly();

        assertThat(provider.currentTransactionCache()).isEmpty();
        transactionManager.rollback();
    }

    @Test
    void transactionsSuspendedOverSeveralLevelsGetTheirOwnCachesBack() throws Exception {
        transactionManager.begin();
        var firstCache = provider.currentTransactionCache().orElseThrow();
        var first = transactionManager.suspend();
        transactionManager.begin();
        var secondCache = provider.currentTransactionCache().orElseThrow();
        var second = transactionManager.suspend();
        transactionManager.begin();
        var thirdCache = provider.currentTransactionCache().orElseThrow();
        assertThat(thirdCache).isNotSameAs(secondCache).isNotSameAs(firstCache);
        transactionManager.commit();

        transactionManager.resume(second);
        assertThat(provider.currentTransactionCache()).containsSame(secondCache);
        transactionManager.commit();
        transactionManager.resume(first);
        assertThat(provider.currentTransactionCache()).containsSame(firstCache);
        transactionManager.commit();
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aTransactionSuspendedWithoutACacheYetGetsOneOfItsOwnAfterwards() throws Exception {
        transactionManager.begin();
        var outer = transactionManager.suspend();
        transactionManager.begin();
        var innerCache = provider.currentTransactionCache().orElseThrow();
        innerCache.put(KEY, new FetcherResult<>(null, null));
        transactionManager.commit();
        transactionManager.resume(outer);

        var outerCache = provider.currentTransactionCache().orElseThrow();
        assertThat(outerCache).isNotSameAs(innerCache);
        assertThat(outerCache.take(KEY)).isEmpty();
        transactionManager.commit();
    }

    @Test
    void aCommitFailingInAnotherSynchronizationLeavesNothingForTheNextOne() throws Exception {
        transactionManager.begin();
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));
        transactionManager.getTransaction().registerSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {
                throw new IllegalStateException("commit fails");
            }

            @Override
            public void afterCompletion(int status) {
                // nothing to do
            }
        });

        assertThatThrownBy(() -> transactionManager.commit()).isInstanceOf(Exception.class);

        assertThat(cache.take(KEY)).as("emptied on the rollback").isEmpty();
        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aTransactionTimingOutLeavesNothingForTheNextOne() throws Exception {
        transactionManager.setTransactionTimeout(1);
        try {
            transactionManager.begin();
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(KEY, new FetcherResult<>(null, null));

            // the transaction manager rolls the timed out transaction back - when exactly, and on which thread, is up
            // to it: Atomikos reports it as active until the commit is attempted
            Thread.sleep(2500);
            assertThatThrownBy(() -> transactionManager.commit()).isInstanceOf(Exception.class);

            assertThat(cache.take(KEY)).as("emptied on the rollback").isEmpty();
        } finally {
            transactionManager.setTransactionTimeout(0);
        }
        transactionManager.begin();
        assertThat(provider.currentTransactionCache().orElseThrow().take(KEY)).isEmpty();
        transactionManager.commit();
    }

    @Test
    void aTransactionMarkedForRollbackAfterwardsHandsOutItsCacheNoMore() throws Exception {
        transactionManager.begin();
        provider.currentTransactionCache().orElseThrow().put(KEY, new FetcherResult<>(null, null));

        transactionManager.setRollbackOnly();

        assertThat(provider.currentTransactionCache()).isEmpty();
        transactionManager.rollback();
    }

    @Test
    void transactionsRunningInParallelNeverShareACache() throws Exception {
        var caches = new ConcurrentLinkedQueue<TransactionCache<Object>>();
        var allInTheirTransaction = new CountDownLatch(4);
        var executor = Executors.newFixedThreadPool(4);
        try {
            var runs = new ArrayList<Future<?>>();
            for (int i = 0; i < 4; i++) {
                runs.add(executor.submit(() -> {
                    transactionManager.begin();
                    var cache = provider.currentTransactionCache().orElseThrow();
                    caches.add(cache);
                    allInTheirTransaction.countDown();
                    allInTheirTransaction.await(10, TimeUnit.SECONDS);
                    assertThat(provider.currentTransactionCache()).containsSame(cache);
                    transactionManager.commit();
                    return null;
                }));
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
    void clearingEmptiesTheCacheOfTheTransactionAndKeepsItInUse() throws Exception {
        transactionManager.begin();
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(KEY, new FetcherResult<>(null, null));

        provider.clearCurrentTransactionCache();

        assertThat(provider.currentTransactionCache()).containsSame(cache);
        assertThat(cache.take(KEY)).isEmpty();
        transactionManager.commit();
    }

    @Test
    void clearingCreatesNoCacheAndDoesNothingWithoutATransaction() throws Exception {
        provider.clearCurrentTransactionCache();

        transactionManager.begin();
        var registry = new TransactionSynchronizationRegistryImp();
        provider.clearCurrentTransactionCache();
        assertThat(registry.getResource(provider)).as("no cache created by clearing").isNull();
        transactionManager.commit();
    }

    @Test
    void theMaximalSizeMustBePositive() {
        assertThatThrownBy(() -> new JtaTransactionCacheProvider<>(new TransactionSynchronizationRegistryImp(), 0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void theLeastRecentlyUsedEntryIsEvictedBeyondTheMaximalSize() throws Exception {
        var small = new JtaTransactionCacheProvider<>(new TransactionSynchronizationRegistryImp(), 1);
        transactionManager.begin();
        var cache = small.currentTransactionCache().orElseThrow();
        var other = new AggregateCacheKey("some.Aggregate", new TestId(2L));

        cache.put(KEY, new FetcherResult<>(null, null));
        cache.put(other, new FetcherResult<>(null, null));

        assertThat(cache.take(KEY)).isEmpty();
        assertThat(cache.take(other)).isPresent();
        transactionManager.commit();
    }

    private record TestId(Long value) implements Identity<Long> {
    }
}

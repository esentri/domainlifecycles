package io.domainlifecycles.jooq.cache;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheScope;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.jooq.Configuration;
import org.jooq.TransactionContext;
import org.jooq.TransactionListenerProvider;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DefaultTransactionListenerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionCacheJooqBinderTest {

    private static final AggregateCacheKey KEY = new AggregateCacheKey("some.Aggregate", new TestId(1L));

    // jOOQ passes one and the same context to all events of a transaction
    private static TransactionContext ctx() {
        return mock(TransactionContext.class);
    }

    private final ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> cacheProvider =
        new ThreadBoundTransactionCacheProvider<>();

    private final TransactionCacheJooqBinder binder = new TransactionCacheJooqBinder(cacheProvider);

    @AfterEach
    void closeAnyScopeLeftOpen() {
        cacheProvider.currentTransactionCache().ifPresent(cache -> ((TransactionCacheScope) cache).close());
    }

    @Test
    public void beginStartOpensAScope() {
        binder.beginStart(ctx());

        assertThat(cacheProvider.currentTransactionCache()).isPresent();
    }

    @Test
    public void commitEndClosesTheScope() {
        var transaction = ctx();

        binder.beginStart(transaction);
        binder.commitEnd(transaction);

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void rollbackEndClosesTheScope() {
        var transaction = ctx();

        binder.beginStart(transaction);
        binder.rollbackEnd(transaction);

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void onlyTheOutermostCommitEndClosesTheScope() {
        var outer = ctx();
        var nested = ctx();

        binder.beginStart(outer);
        binder.beginStart(nested);
        binder.commitEnd(nested);
        assertThat(cacheProvider.currentTransactionCache()).isPresent();

        binder.commitEnd(outer);
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void aNestedCommitKeepsTheEntries() {
        var outer = ctx();
        var nested = ctx();

        binder.beginStart(outer);
        binder.beginStart(nested);
        var scope = cacheProvider.currentTransactionCache().orElseThrow();
        scope.put(KEY, new FetcherResult<>(null, null));
        binder.commitEnd(nested);

        assertThat(scope.take(KEY)).isPresent();
    }

    @Test
    public void aNestedRollbackClearsTheScopeButKeepsItOpen() {
        var outer = ctx();
        var nested = ctx();

        binder.beginStart(outer);
        binder.beginStart(nested);
        var scope = cacheProvider.currentTransactionCache().orElseThrow();
        scope.put(KEY, new FetcherResult<>(null, null));
        binder.rollbackEnd(nested);

        assertThat(cacheProvider.currentTransactionCache()).containsSame(scope);
        assertThat(scope.take(KEY)).as("loaded since the savepoint").isEmpty();
        binder.commitEnd(outer);
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void aFailingCommitReportedAsRolledBackAfterwardsLeavesNoScope() {
        var transaction = ctx();

        binder.beginStart(transaction);
        binder.commitEnd(transaction);
        binder.rollbackEnd(transaction);

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
        var next = ctx();
        binder.beginStart(next);
        assertThat(cacheProvider.currentTransactionCache()).as("a scope of its own").isPresent();
        binder.commitEnd(next);
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void aNestedTransactionFailingToCommitKeepsTheScopeOfTheOuterOneButClearsIt() {
        var outer = ctx();
        var nested = ctx();

        binder.beginStart(outer);
        binder.beginStart(nested);
        var scope = cacheProvider.currentTransactionCache().orElseThrow();
        scope.put(KEY, new FetcherResult<>(null, null));
        binder.commitEnd(nested);
        binder.rollbackEnd(nested);

        assertThat(cacheProvider.currentTransactionCache()).as("the outer transaction still runs").containsSame(scope);
        assertThat(scope.take(KEY)).as("loaded in the rolled back nested transaction").isEmpty();
        binder.commitEnd(outer);
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void aTransactionFailingToBeginLeavesNoScope() {
        var transaction = ctx();

        // jOOQ reports a transaction it failed to begin as rolled back
        binder.beginStart(transaction);
        binder.rollbackEnd(transaction);

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void anEndOfAnInnerTransactionNotReportedEndsItWithTheOuterOne() {
        var outer = ctx();
        var nested = ctx();

        binder.beginStart(outer);
        binder.beginStart(nested);
        binder.commitEnd(outer);

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void transactionsRunningInParallelNeverShareAScope() throws Exception {
        var scopes = new ConcurrentLinkedQueue<Object>();
        var allInTheirTransaction = new CountDownLatch(4);
        var executor = Executors.newFixedThreadPool(4);
        try {
            var runs = new ArrayList<Future<?>>();
            for (int i = 0; i < 4; i++) {
                runs.add(executor.submit(() -> {
                    var transaction = ctx();
                    binder.beginStart(transaction);
                    var scope = cacheProvider.currentTransactionCache().orElseThrow();
                    scopes.add(scope);
                    allInTheirTransaction.countDown();
                    allInTheirTransaction.await(10, TimeUnit.SECONDS);
                    assertThat(cacheProvider.currentTransactionCache()).containsSame(scope);
                    binder.commitEnd(transaction);
                    assertThat(cacheProvider.currentTransactionCache()).isEmpty();
                    return null;
                }));
            }
            for (var run : runs) {
                run.get(10, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
        }

        assertThat(scopes).hasSize(4).doesNotHaveDuplicates();
    }

    @Test
    public void clearingEmptiesTheScopeOfTheTransactionAndKeepsItInUse() {
        var transaction = ctx();
        binder.beginStart(transaction);
        var scope = cacheProvider.currentTransactionCache().orElseThrow();
        scope.put(KEY, new FetcherResult<>(null, null));

        cacheProvider.clearCurrentTransactionCache();

        assertThat(cacheProvider.currentTransactionCache()).containsSame(scope);
        assertThat(scope.take(KEY)).isEmpty();
        binder.commitEnd(transaction);
    }

    private record TestId(Long value) implements Identity<Long> {
    }

    @Test
    public void registerOnAppendsTheBinderWhenNoneIsRegisteredYet() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders()).thenReturn(new TransactionListenerProvider[0]);

        TransactionCacheJooqBinder.registerOn(configuration, cacheProvider);

        verify(configuration).setAppending(any(TransactionListenerProvider.class));
    }

    @Test
    public void registerOnDoesNothingWhenAlreadyRegisteredForTheSameProvider() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var existingBinder = new TransactionCacheJooqBinder(cacheProvider);
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders())
            .thenReturn(new TransactionListenerProvider[]{new DefaultTransactionListenerProvider(existingBinder)});

        TransactionCacheJooqBinder.registerOn(configuration, cacheProvider);

        verify(configuration, never()).setAppending(any(TransactionListenerProvider.class));
    }

    @Test
    public void registerOnAppendsASecondBinderForADifferentProvider() {
        var firstCacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var secondCacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var existingBinder = new TransactionCacheJooqBinder(firstCacheProvider);
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders())
            .thenReturn(new TransactionListenerProvider[]{new DefaultTransactionListenerProvider(existingBinder)});

        TransactionCacheJooqBinder.registerOn(configuration, secondCacheProvider);

        verify(configuration).setAppending(any(TransactionListenerProvider.class));
    }
}

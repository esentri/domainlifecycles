package io.domainlifecycles.persistence.cache;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThreadBoundTransactionCacheProviderTest {

    private record TestId(Long value) implements Identity<Long> {
    }

    private static AggregateCacheKey key(long value) {
        return new AggregateCacheKey("some.Aggregate", new TestId(value));
    }

    private static FetcherResult<AggregateRoot<?>, String> result() {
        return new FetcherResult<>(null, null);
    }

    @Test
    public void currentTransactionCacheIsEmptyWhenNoScopeIsOpen() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();

        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void currentTransactionCacheIsPresentWhileAScopeIsOpen() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();

        try (var scope = provider.open()) {
            assertThat(provider.currentTransactionCache()).isPresent();
        }
    }

    @Test
    public void putThenTakeReturnsTheEntryAndRemovesIt() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var key = key(1L);
        var stored = result();

        try (var scope = provider.open()) {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(key, stored);

            var taken = cache.take(key);

            assertThat(taken).containsSame(stored);
            assertThat(cache.take(key)).isEmpty();
        }
    }

    @Test
    public void takeOnAMissingKeyReturnsEmpty() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();

        try (var scope = provider.open()) {
            var cache = provider.currentTransactionCache().orElseThrow();

            assertThat(cache.take(key(1L))).isEmpty();
        }
    }

    @Test
    public void invalidateRemovesAnEntryWithoutReturningIt() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var key = key(1L);

        try (var scope = provider.open()) {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(key, result());

            cache.invalidate(key);

            assertThat(cache.take(key)).isEmpty();
        }
    }

    @Test
    public void invalidateOnAMissingKeyIsANoOp() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();

        try (var scope = provider.open()) {
            var cache = provider.currentTransactionCache().orElseThrow();

            assertThatNoException().isThrownBy(() -> cache.invalidate(key(1L)));
        }
    }

    @Test
    public void closeClearsEntriesAndUnbindsTheThread() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();

        var scope = provider.open();
        var cache = provider.currentTransactionCache().orElseThrow();
        cache.put(key(1L), result());

        scope.close();

        assertThat(provider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void closeIsIdempotent() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var scope = provider.open();

        scope.close();

        assertThatNoException().isThrownBy(scope::close);
    }

    @Test
    public void openWhileAScopeIsAlreadyOpenDiscardsTheStaleScope() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var staleScope = provider.open();
        var staleCache = provider.currentTransactionCache().orElseThrow();
        staleCache.put(key(1L), result());

        //forgotten close() on staleScope - a fresh open() must not see its entries
        var freshScope = provider.open();

        var freshCache = provider.currentTransactionCache().orElseThrow();
        assertThat(freshCache.take(key(1L))).isEmpty();

        freshScope.close();
    }

    @Test
    public void closingASupersededStaleScopeDoesNotAffectTheNewerScope() {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var staleScope = provider.open();

        var freshScope = provider.open();
        var freshCache = provider.currentTransactionCache().orElseThrow();
        freshCache.put(key(1L), result());

        //closing the discarded, superseded scope handle must not clear the newer, still active scope
        staleScope.close();

        assertThat(provider.currentTransactionCache()).isPresent();
        assertThat(provider.currentTransactionCache().orElseThrow().take(key(1L))).isPresent();

        freshScope.close();
    }

    @Test
    public void evictsTheLeastRecentlyUsedEntryOnceMaxSizeIsExceeded() {
        var provider = new ThreadBoundTransactionCacheProvider<String>(2);

        try (var scope = provider.open()) {
            var cache = provider.currentTransactionCache().orElseThrow();
            cache.put(key(1L), result());
            cache.put(key(2L), result());
            //accessing key 1 marks it as more recently used than key 2
            assertThat(cache.take(key(1L))).isPresent();
            cache.put(key(1L), result());

            //key 2 is now the least recently used entry and gets evicted when a third entry is added
            cache.put(key(3L), result());

            assertThat(cache.take(key(2L))).isEmpty();
            assertThat(cache.take(key(1L))).isPresent();
            assertThat(cache.take(key(3L))).isPresent();
        }
    }

    @Test
    public void constructorRejectsANonPositiveMaxSize() {
        assertThatThrownBy(() -> new ThreadBoundTransactionCacheProvider<String>(0))
            .isInstanceOf(DLCPersistenceException.class);
        assertThatThrownBy(() -> new ThreadBoundTransactionCacheProvider<String>(-1))
            .isInstanceOf(DLCPersistenceException.class);
    }

    /**
     * The provider is shared by one long-lived instance across every transaction/thread in a real
     * application (bound only via {@link ThreadLocal}), so its isolation guarantee has to hold under
     * genuine concurrent access, not just across sequential calls on a single thread - two real threads
     * racing each other are the only way to actually exercise that.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void twoThreadsGetCompletelyIsolatedScopesUnderRealConcurrency() throws Exception {
        var provider = new ThreadBoundTransactionCacheProvider<String>();
        var sharedKey = key(1L);

        var bothScopesOpen = new CyclicBarrier(2);
        var threadAHasPopulated = new CountDownLatch(1);
        var threadBHasClosed = new CountDownLatch(1);

        AtomicReference<Optional<FetcherResult<?, String>>> whatThreadBSeesForThreadAsKey =
            new AtomicReference<>();
        AtomicBoolean threadAScopeSurvivesThreadBClosing = new AtomicBoolean();
        AtomicReference<Exception> failureOnAnyThread = new AtomicReference<>();

        Thread threadA = new Thread(() -> {
            try (var scope = provider.open()) {
                provider.currentTransactionCache().orElseThrow().put(sharedKey, result());
                bothScopesOpen.await();
                threadAHasPopulated.countDown();

                //thread B's own scope (opened and closed entirely on its own thread) must not interfere
                //with this thread's still-open one, the same way one transaction's scope must survive an
                //unrelated, concurrently completing transaction
                threadBHasClosed.await();
                threadAScopeSurvivesThreadBClosing.set(provider.currentTransactionCache().isPresent());
            } catch (Exception e) {
                failureOnAnyThread.set(e);
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                try (var scope = provider.open()) {
                    bothScopesOpen.await();
                    threadAHasPopulated.await();

                    //thread A just populated the identical key on its own thread - this thread's own,
                    //independently bound scope must not see it
                    whatThreadBSeesForThreadAsKey.set(provider.currentTransactionCache().orElseThrow().take(sharedKey));
                }
                threadBHasClosed.countDown();
            } catch (Exception e) {
                failureOnAnyThread.set(e);
            }
        });

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        assertThat(failureOnAnyThread.get()).isNull();
        assertThat(whatThreadBSeesForThreadAsKey.get())
            .as("a key put on one thread's scope must be invisible to a different thread's own scope")
            .isEmpty();
        assertThat(threadAScopeSurvivesThreadBClosing.get())
            .as("closing one thread's scope must not affect a different thread's still-open scope")
            .isTrue();
    }
}

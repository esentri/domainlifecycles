package io.domainlifecycles.persistence.cache;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.Test;

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
}

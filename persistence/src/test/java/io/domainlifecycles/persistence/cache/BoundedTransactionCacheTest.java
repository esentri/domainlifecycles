package io.domainlifecycles.persistence.cache;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoundedTransactionCacheTest {

    private record TestId(Long value) implements Identity<Long> {
    }

    private static AggregateCacheKey key(long id) {
        return new AggregateCacheKey("some.Aggregate", new TestId(id));
    }

    @Test
    void takeReturnsAnEntryAndRemovesIt() {
        var cache = new BoundedTransactionCache<Object>(256);
        var result = new FetcherResult<>(null, null);
        cache.put(key(1), result);

        assertThat(cache.take(key(1))).containsSame(result);
        assertThat(cache.take(key(1))).isEmpty();
    }

    @Test
    void putReplacesAnEntry() {
        var cache = new BoundedTransactionCache<Object>(256);
        var replacing = new FetcherResult<>(null, null);
        cache.put(key(1), new FetcherResult<>(null, null));

        cache.put(key(1), replacing);

        assertThat(cache.take(key(1))).containsSame(replacing);
    }

    @Test
    void invalidateRemovesAnEntryAndIgnoresAMissingOne() {
        var cache = new BoundedTransactionCache<Object>(256);
        cache.put(key(1), new FetcherResult<>(null, null));

        cache.invalidate(key(1));
        cache.invalidate(key(2));

        assertThat(cache.take(key(1))).isEmpty();
    }

    @Test
    void clearRemovesAllEntriesAndKeepsTheCacheUsable() {
        var cache = new BoundedTransactionCache<Object>(256);
        cache.put(key(1), new FetcherResult<>(null, null));
        cache.put(key(2), new FetcherResult<>(null, null));

        cache.clear();

        assertThat(cache.take(key(1))).isEmpty();
        assertThat(cache.take(key(2))).isEmpty();
        cache.put(key(3), new FetcherResult<>(null, null));
        assertThat(cache.take(key(3))).isPresent();
    }

    @Test
    void theLeastRecentlyUsedEntryIsEvictedBeyondTheMaximalSize() {
        var cache = new BoundedTransactionCache<Object>(2);
        cache.put(key(1), new FetcherResult<>(null, null));
        cache.put(key(2), new FetcherResult<>(null, null));
        // key 1 replaced, so key 2 is the least recently used one
        cache.put(key(1), new FetcherResult<>(null, null));

        cache.put(key(3), new FetcherResult<>(null, null));

        assertThat(cache.take(key(2))).isEmpty();
        assertThat(cache.take(key(1))).isPresent();
        assertThat(cache.take(key(3))).isPresent();
    }

    @Test
    void theMaximalSizeMustBePositive() {
        assertThatThrownBy(() -> new BoundedTransactionCache<>(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void concurrentAccessLosesNoEntryAndBreaksNothing() throws Exception {
        // a transaction manager may complete a transaction - and clear its cache - on another thread
        var cache = new BoundedTransactionCache<Object>(100_000);
        var start = new CountDownLatch(1);
        var executor = Executors.newFixedThreadPool(4);
        try {
            var runs = new ArrayList<Future<?>>();
            for (int thread = 0; thread < 4; thread++) {
                int offset = thread * 10_000;
                runs.add(executor.submit(() -> {
                    start.await();
                    for (int i = 0; i < 10_000; i++) {
                        cache.put(key(offset + i), new FetcherResult<>(null, null));
                    }
                    return null;
                }));
            }
            start.countDown();
            for (var run : runs) {
                run.get(30, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
        }

        for (int i = 0; i < 40_000; i++) {
            assertThat(cache.take(key(i))).as("entry %d", i).isPresent();
        }
    }
}

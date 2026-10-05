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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.persistence.cache;

import io.domainlifecycles.persistence.fetcher.FetcherResult;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The cache of one transaction, holding at most a given number of aggregates and evicting the least recently used
 * one beyond that - an evicted entry only causes a normal database fetch later on.
 * <p>
 * For a {@link TransactionCacheProvider} that keeps one such cache per transaction, e.g. as a resource of the
 * transaction. Synchronized, since a transaction manager may complete a transaction on another thread than the one it
 * ran on, e.g. on a timeout.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this cache is used with
 * @author Mario Herb
 */
public final class BoundedTransactionCache<BASE_RECORD_TYPE> implements TransactionCache<BASE_RECORD_TYPE> {

    private final Map<AggregateCacheKey, FetcherResult<?, BASE_RECORD_TYPE>> entries;

    /**
     * Creates an empty cache.
     *
     * @param maxSize the maximum number of aggregates held
     */
    public BoundedTransactionCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0, but was " + maxSize);
        }
        this.entries = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<AggregateCacheKey, FetcherResult<?, BASE_RECORD_TYPE>> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public synchronized Optional<FetcherResult<?, BASE_RECORD_TYPE>> take(AggregateCacheKey key) {
        return Optional.ofNullable(entries.remove(key));
    }

    @Override
    public synchronized void put(AggregateCacheKey key, FetcherResult<?, BASE_RECORD_TYPE> result) {
        entries.put(key, result);
    }

    @Override
    public synchronized void invalidate(AggregateCacheKey key) {
        entries.remove(key);
    }

    /**
     * Removes all entries - once the transaction completes, rolls back to a savepoint, or wrote an aggregate bypassing
     * DLC's repositories.
     */
    @Override
    public synchronized void clear() {
        entries.clear();
    }
}

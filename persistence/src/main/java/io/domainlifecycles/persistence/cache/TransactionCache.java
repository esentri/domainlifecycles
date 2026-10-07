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

import java.util.Optional;

/**
 * Holds aggregate roots that were already fetched earlier within the same transaction, so that
 * {@code DomainStructureAwareRepository.update()}/{@code deleteById()}/{@code increaseVersion()} can reuse
 * that state instead of fetching it again from the database.
 * <p>
 * Scoped to a single transaction; see {@link TransactionCacheProvider} for how a cache instance is obtained.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this cache is used with
 * @author Mario Herb
 */
public interface TransactionCache<BASE_RECORD_TYPE> {

    /**
     * Returns the cache entry for the given key, if present, and removes it from the cache.
     * <p>
     * Entries are consumed on read (get-and-remove), never reused for a second, independent operation - see
     * {@code AggregateCacheSupport} for why.
     *
     * @param key the key to look up
     * @return the cache entry, if one was present
     */
    Optional<FetcherResult<?, BASE_RECORD_TYPE>> take(AggregateCacheKey key);

    /**
     * Adds or replaces the cache entry for the given key.
     *
     * @param key    the key to store the entry under
     * @param result the fetched result to cache
     */
    void put(AggregateCacheKey key, FetcherResult<?, BASE_RECORD_TYPE> result);

    /**
     * Removes the cache entry for the given key, if present. A no-op if no entry is present.
     *
     * @param key the key to remove
     */
    void invalidate(AggregateCacheKey key);

    /**
     * Removes all entries, keeping the cache in use - see {@link TransactionCacheProvider#clearCurrentTransactionCache()}
     * for when an application does so.
     */
    void clear();
}

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

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.clone.EntityCloner;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;

import java.util.Optional;

/**
 * Internal support used by {@code InternalAggregateFetcher} (to populate the transaction cache after a real
 * fetch) and {@code DomainStructureAwareRepository} (to consume/invalidate a cache entry around a write). Not
 * meant to be used directly by application code.
 * <p>
 * A cache entry is a {@link FetcherResult} carrying a <b>cloned</b> aggregate root graph (only entities are
 * cloned, via {@link EntityCloner} - value object instances are immutable in DLC and are therefore reused by
 * reference, never duplicated) together with the <b>same, unmodified</b> {@code FetcherContext} instance from
 * the original fetch. Reusing the context unmodified is safe precisely because value object instances are not
 * duplicated by the clone: identity-based lookups in the context still resolve correctly against the cloned
 * root, since the value object references it holds are the very same instances. The context is still needed
 * unmodified for one thing: deleting a record-mapped value object requires the record instance originally
 * fetched for it, because its technical id is never mapped back into the domain (see
 * {@code BasePersister.deleteRecordMappedValueObject()}).
 *
 * @author Mario Herb
 */
public final class AggregateCacheSupport {

    private AggregateCacheSupport() {
        //not meant to be instantiated
    }

    /**
     * Builds the cache key for an already fetched aggregate root instance.
     *
     * @param domainPersistenceProvider the domain persistence provider to resolve the root's identity with
     * @param root                      the aggregate root instance
     * @return the cache key identifying this aggregate root instance
     */
    public static AggregateCacheKey keyFor(DomainPersistenceProvider<?> domainPersistenceProvider,
                                           AggregateRoot<?> root) {
        return new AggregateCacheKey(root.getClass().getName(), domainPersistenceProvider.getId(root));
    }

    /**
     * Builds the cache key for an aggregate root identified only by its id - used where a write operation
     * (e.g. {@code deleteById}) has not fetched the root instance itself yet. The aggregate root type is
     * resolved from the id's own type, the same way {@code JooqAggregateFetcher}/{@code JdbcAggregateFetcher}
     * already resolve an aggregate root's table/record by id.
     *
     * @param id the identity of the aggregate root
     * @return the cache key identifying the aggregate root instance with this id
     */
    public static AggregateCacheKey keyFor(Identity<?> id) {
        var rootTypeName = Domain.entityMirrorForIdentityTypeName(id.getClass().getName()).getTypeName();
        return new AggregateCacheKey(rootTypeName, id);
    }

    /**
     * Looks up and removes (get-and-remove) the cache entry for the given key, if a transaction cache is
     * currently active and holds one.
     *
     * @param domainPersistenceProvider the domain persistence provider to resolve the current transaction
     *                                  cache from
     * @param key                       the cache key to look up
     * @param <A>                       the aggregate root type
     * @param <BASE_RECORD_TYPE>        the base record type
     * @return the cached fetch result, if a hit occurred
     */
    @SuppressWarnings("unchecked")
    public static <A extends AggregateRoot<?>, BASE_RECORD_TYPE> Optional<FetcherResult<A, BASE_RECORD_TYPE>> take(
        DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider,
        AggregateCacheKey key
    ) {
        return domainPersistenceProvider.transactionCacheProvider.currentTransactionCache()
            .flatMap(cache -> cache.take(key))
            .map(result -> (FetcherResult<A, BASE_RECORD_TYPE>) result);
    }

    /**
     * Removes the cache entry for the given key, if a transaction cache is currently active. A no-op
     * otherwise, and a no-op if no entry is present for the key.
     *
     * @param domainPersistenceProvider the domain persistence provider to resolve the current transaction
     *                                  cache from
     * @param key                       the cache key to remove
     */
    public static void invalidate(DomainPersistenceProvider<?> domainPersistenceProvider, AggregateCacheKey key) {
        domainPersistenceProvider.transactionCacheProvider.currentTransactionCache()
            .ifPresent(cache -> cache.invalidate(key));
    }

    /**
     * Populates the transaction cache with the result of a real fetch, if a transaction cache is currently
     * active. A no-op otherwise, and a no-op if the fetch did not yield a result (e.g. the aggregate does
     * not exist).
     *
     * @param domainPersistenceProvider the domain persistence provider to resolve the current transaction
     *                                  cache from
     * @param entityCloner              used to deep-clone the fetched aggregate root's entities before caching
     * @param result                    the result of the real fetch
     * @param <A>                       the aggregate root type
     * @param <BASE_RECORD_TYPE>        the base record type
     */
    @SuppressWarnings("unchecked")
    public static <A extends AggregateRoot<?>, BASE_RECORD_TYPE> void populate(
        DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider,
        EntityCloner entityCloner,
        FetcherResult<A, BASE_RECORD_TYPE> result
    ) {
        domainPersistenceProvider.transactionCacheProvider.currentTransactionCache().ifPresent(cache ->
            result.resultValue().ifPresent(root -> {
                A clonedRoot = (A) entityCloner.clone(root);
                var key = keyFor(domainPersistenceProvider, root);
                cache.put(key, new FetcherResult<>(clonedRoot, result.fetchedContext()));
            })
        );
    }
}

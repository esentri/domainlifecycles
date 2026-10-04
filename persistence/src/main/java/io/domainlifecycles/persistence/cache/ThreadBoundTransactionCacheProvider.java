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

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link TransactionCacheProvider} binding a {@link TransactionCache} to the calling thread for the duration of
 * one transaction, for transactions without a transaction manager of their own: a scope is opened and closed around
 * the transaction by jOOQ's {@code TransactionCacheJooqBinder} for jOOQ's own transactions, or by the application for
 * plain JDBC transactions it drives itself (see {@link TransactionCacheScope}). Spring-managed and JTA transactions
 * keep their cache as a resource of the transaction instead ({@code SpringTransactionCacheProvider},
 * {@code JtaTransactionCacheProvider}).
 * <p>
 * Each scope's entries are held in a size-bounded LRU map (see {@link #ThreadBoundTransactionCacheProvider(int)})
 * so that a transaction fetching a large number of aggregates (e.g. a batch job or report) cannot grow the
 * cache without bound; an evicted entry simply causes a cache miss - and therefore a normal database fetch -
 * later on, never incorrect behavior.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this provider is used with
 * @author Mario Herb
 */
public final class ThreadBoundTransactionCacheProvider<BASE_RECORD_TYPE>
    implements TransactionCacheProvider<BASE_RECORD_TYPE> {

    private static final Logger log = LoggerFactory.getLogger(ThreadBoundTransactionCacheProvider.class);

    private static final int DEFAULT_MAX_SIZE = 256;

    private final int maxSize;

    private final ThreadLocal<Scope> currentScope = new ThreadLocal<>();

    /**
     * Creates a new ThreadBoundTransactionCacheProvider with the default maximum number of cached entries
     * per transaction (256).
     */
    public ThreadBoundTransactionCacheProvider() {
        this(DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a new ThreadBoundTransactionCacheProvider.
     *
     * @param maxSize the maximum number of entries held per transaction; once exceeded, the least recently
     *                used entry is evicted
     */
    public ThreadBoundTransactionCacheProvider(int maxSize) {
        if (maxSize <= 0) {
            throw DLCPersistenceException.fail(
                "transactionCacheMaxSize must be greater than 0, but was '%d'!", maxSize);
        }
        this.maxSize = maxSize;
    }

    /**
     * Opens a new transaction cache scope for the calling thread.
     * <p>
     * If a scope is already open on the calling thread - which should not normally happen, but could be the
     * result of a previous transaction not having reached its native commit/rollback callback, e.g. because
     * of an unexpected error path - that stale scope is discarded immediately (its entries are dropped, it
     * is unbound from the thread) before the new scope is created. This bounds the damage of a forgotten
     * {@code close()} to wasted memory between two transactions - it can never cause data fetched for one
     * transaction to be served, as if current, to a later, unrelated transaction on the same (reused) thread.
     *
     * @return a handle for the newly opened scope; must be closed via try-with-resources by the caller
     */
    public TransactionCacheScope open() {
        Scope stale = currentScope.get();
        if (stale != null) {
            log.warn("stale transaction cache scope discarded, {} entries", stale.entries.size());
        }
        Scope scope = new Scope(maxSize);
        currentScope.set(scope);
        return scope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache() {
        return Optional.ofNullable(currentScope.get());
    }

    private final class Scope implements TransactionCache<BASE_RECORD_TYPE>, TransactionCacheScope {

        private final Map<AggregateCacheKey, FetcherResult<?, BASE_RECORD_TYPE>> entries;

        private Scope(int maxSize) {
            this.entries = new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(
                    Map.Entry<AggregateCacheKey, FetcherResult<?, BASE_RECORD_TYPE>> eldest) {
                    return size() > maxSize;
                }
            };
        }

        @Override
        public Optional<FetcherResult<?, BASE_RECORD_TYPE>> take(AggregateCacheKey key) {
            return Optional.ofNullable(entries.remove(key));
        }

        @Override
        public void put(AggregateCacheKey key, FetcherResult<?, BASE_RECORD_TYPE> result) {
            entries.put(key, result);
        }

        @Override
        public void invalidate(AggregateCacheKey key) {
            entries.remove(key);
        }

        @Override
        public void clear() {
            entries.clear();
        }

        @Override
        public void close() {
            //only the scope that is still current may clear/unbind - a scope superseded by a later open()
            //(see the self-healing above) was already cleared and unbound at that point, and must not
            //interfere with whatever newer scope is now bound to the thread
            if (currentScope.get() == this) {
                entries.clear();
                currentScope.remove();
            }
        }
    }
}

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

import java.util.Optional;

/**
 * Supplies the {@link TransactionCache} that is active for the currently running transaction, if any.
 * <p>
 * Returns an empty {@link Optional} whenever no transaction cache scope is currently open on the calling
 * thread - this is the fail-safe default (never fail-open): callers that get an empty result simply fall
 * back to fetching from the database, exactly as if the transaction cache feature did not exist.
 * <p>
 * A write compares the aggregate against the state the cache holds for it. An implementation must therefore only
 * ever hand out a cache holding what the running transaction itself loaded and what is still valid within it:
 * <ul>
 *     <li>one cache per transaction, never shared between two transactions - also not between transactions that
 *     run one after the other on the same thread or on the same, pooled connection,</li>
 *     <li>no cache outside an actual transaction,</li>
 *     <li>the cache lives as long as its transaction, across all its reads and writes, and is emptied once the
 *     transaction completes - committed or rolled back,</li>
 *     <li>a rollback to a savepoint empties the cache, since entries loaded since may hold state that was never
 *     committed,</li>
 *     <li>a transaction suspended for another one keeps its cache apart from the other one's, and gets it back when
 *     it resumes.</li>
 * </ul>
 * Where an implementation cannot learn these boundaries reliably, it must hand out no cache at all. The
 * implementations DLC provides follow Spring's transactions ({@code SpringTransactionCacheProvider}), JTA transactions
 * ({@code JtaTransactionCacheProvider}), and scopes opened explicitly around a transaction
 * ({@link ThreadBoundTransactionCacheProvider}); {@link NoOpTransactionCacheProvider} hands out none.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this provider is used with
 * @author Mario Herb
 */
public interface TransactionCacheProvider<BASE_RECORD_TYPE> {

    /**
     * Returns the {@link TransactionCache} active for the current transaction on the calling thread, if any.
     *
     * @return the current transaction cache, or empty if no transaction cache scope is currently open
     */
    Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache();

    /**
     * Empties the cache of the current transaction, if there is one - keeping it in use for the rest of the
     * transaction.
     * <p>
     * The cache only knows about the writes of DLC's repositories. Whatever changes the state of an aggregate the
     * running transaction already loaded in another way makes the cached state stale, and a later write of that
     * aggregate would compare against it: an application calls this method after
     * <ul>
     *     <li>writing such an aggregate with its own SQL or jOOQ statements, e.g. a bulk update,</li>
     *     <li>calling a stored procedure, or a trigger fired by another write, changing it,</li>
     *     <li>rolling back to a savepoint of a transaction it drives itself, unless its provider notices that.</li>
     * </ul>
     * Emptying the cache never changes the result of a write: a write without a cache entry reads the current state
     * of the aggregate, exactly as without the feature.
     */
    default void clearCurrentTransactionCache() {
        currentTransactionCache().ifPresent(TransactionCache::clear);
    }
}

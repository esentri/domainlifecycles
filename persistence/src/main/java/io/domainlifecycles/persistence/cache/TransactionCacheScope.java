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

/**
 * A handle for one open {@link ThreadBoundTransactionCacheProvider} scope, bound to the transaction that
 * opened it.
 * <p>
 * Obtained from {@link ThreadBoundTransactionCacheProvider#open()} by a binder following the transaction boundaries
 * (jOOQ's {@code TransactionCacheJooqBinder}) - or, for plain JDBC
 * transactions the application drives itself, by the application around each transaction, closed once the
 * transaction ends and {@link #clear() cleared} after a rollback to a savepoint.
 * <p>
 * {@link #close()} is idempotent: closing an already closed (or superseded, see
 * {@link ThreadBoundTransactionCacheProvider#open()}) scope is a no-op.
 *
 * @author Mario Herb
 */
public interface TransactionCacheScope extends AutoCloseable {

    /**
     * Removes all cache entries collected so far, keeping the scope open - e.g. once its transaction rolled back to
     * a savepoint, after which entries collected since may hold state that was never committed.
     */
    void clear();

    /**
     * Closes this scope, clearing the cache entries collected during the transaction it was opened for.
     */
    @Override
    void close();
}

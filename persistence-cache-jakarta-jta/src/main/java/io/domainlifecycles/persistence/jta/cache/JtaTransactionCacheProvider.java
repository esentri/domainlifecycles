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

package io.domainlifecycles.persistence.jta.cache;

import io.domainlifecycles.persistence.cache.BoundedTransactionCache;
import io.domainlifecycles.persistence.cache.TransactionCache;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;

import java.util.Objects;
import java.util.Optional;

/**
 * Provides the transaction cache of the JTA transaction currently active, without Spring - e.g. in a Jakarta EE
 * application server or with a standalone JTA transaction manager.
 * <p>
 * The cache of a transaction is a resource of that transaction in the {@link TransactionSynchronizationRegistry}:
 * created on first use, emptied by an interposed {@link Synchronization} once the transaction completes - committed
 * or rolled back. No connection hook is needed, and the transaction manager itself keeps transactions apart: a
 * transaction suspended for another one gets its own cache back when it resumes, and the other one never sees it.
 * <p>
 * Outside an active transaction - none at all, or one already marked for rollback or completing - there is no cache,
 * so every write reads the current state of the aggregate.
 * <p>
 * Each cache holds at most a configurable number of aggregates (256 by default), evicting the least recently used
 * one beyond that - an evicted entry only causes a normal database fetch later on.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this provider is used with
 * @author Mario Herb
 */
public final class JtaTransactionCacheProvider<BASE_RECORD_TYPE> implements TransactionCacheProvider<BASE_RECORD_TYPE> {

    private static final int DEFAULT_MAX_SIZE = 256;

    private final TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    private final int maxSize;

    /**
     * Creates a provider holding at most 256 aggregates per transaction.
     *
     * @param transactionSynchronizationRegistry the registry of the JTA transaction manager in use
     */
    public JtaTransactionCacheProvider(TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        this(transactionSynchronizationRegistry, DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a provider.
     *
     * @param transactionSynchronizationRegistry the registry of the JTA transaction manager in use
     * @param maxSize                            the maximum number of aggregates held per transaction
     */
    public JtaTransactionCacheProvider(TransactionSynchronizationRegistry transactionSynchronizationRegistry,
                                       int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0, but was " + maxSize);
        }
        this.transactionSynchronizationRegistry = Objects.requireNonNull(transactionSynchronizationRegistry);
        this.maxSize = maxSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache() {
        if (transactionSynchronizationRegistry.getTransactionKey() == null
            || transactionSynchronizationRegistry.getTransactionStatus() != Status.STATUS_ACTIVE) {
            return Optional.empty();
        }
        var cache = (BoundedTransactionCache<BASE_RECORD_TYPE>) transactionSynchronizationRegistry.getResource(this);
        if (cache == null) {
            var created = new BoundedTransactionCache<BASE_RECORD_TYPE>(maxSize);
            transactionSynchronizationRegistry.putResource(this, created);
            transactionSynchronizationRegistry.registerInterposedSynchronization(new Synchronization() {
                @Override
                public void beforeCompletion() {
                    // nothing to do before the transaction completes
                }

                @Override
                public void afterCompletion(int status) {
                    created.clear();
                }
            });
            cache = created;
        }
        return Optional.of(cache);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates no cache for a transaction that has none yet - and empties the cache also of a transaction already
     * marked for rollback.
     */
    @Override
    public void clearCurrentTransactionCache() {
        if (transactionSynchronizationRegistry.getTransactionKey() == null) {
            return;
        }
        var cache = (BoundedTransactionCache<?>) transactionSynchronizationRegistry.getResource(this);
        if (cache != null) {
            cache.clear();
        }
    }
}

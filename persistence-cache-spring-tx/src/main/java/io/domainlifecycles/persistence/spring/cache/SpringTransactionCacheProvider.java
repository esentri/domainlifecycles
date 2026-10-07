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

package io.domainlifecycles.persistence.spring.cache;

import io.domainlifecycles.persistence.cache.BoundedTransactionCache;
import io.domainlifecycles.persistence.cache.TransactionCache;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

/**
 * Provides the transaction cache of the Spring-managed transaction currently active - {@code @Transactional} or a
 * {@code TransactionTemplate}, with a {@code DataSourceTransactionManager} as well as a {@code JtaTransactionManager}.
 * <p>
 * The cache of a transaction is a resource of that transaction in Spring's {@link TransactionSynchronizationManager},
 * created on first use and tied to the transaction by a {@link TransactionSynchronization}:
 * <ul>
 *     <li>it lives as long as the transaction, across all its reads and writes,</li>
 *     <li>a transaction suspended for another one ({@code PROPAGATION_REQUIRES_NEW}) puts its cache aside - Spring
 *     only unbinds its own resources on suspension - so that the other one, finding no cache, creates one of its
 *     own; the suspended transaction gets its cache back when it resumes,</li>
 *     <li>a rollback to a savepoint ({@code PROPAGATION_NESTED}) clears the cache, since entries loaded since the
 *     savepoint may hold state that was never committed (Spring 6.2 or later notifies of savepoint rollbacks),</li>
 *     <li>the cache is emptied and unbound once the transaction completes, whether committed or rolled back.</li>
 * </ul>
 * No hook on a connection is needed: Spring raises no event when a transaction begins, but the first access to the
 * cache within a transaction - while loading or before writing an aggregate - always finds that transaction active.
 * <p>
 * Outside a transaction - none at all, or where Spring only activates transaction synchronization without one, e.g.
 * for {@code PROPAGATION_SUPPORTS} - there is no cache, so every write reads the current state of the aggregate.
 * <p>
 * A cache is only handed out to the transaction it was created for: one still bound to the thread although its
 * transaction ended without completing it there - e.g. completed by a JTA transaction manager on another thread after
 * a timeout - is dropped. Without the savepoint callbacks of Spring 6.2, a rollback to a savepoint could not be
 * noticed, so there is no cache at all then.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this provider is used with
 * @author Mario Herb
 */
public final class SpringTransactionCacheProvider<BASE_RECORD_TYPE> implements TransactionCacheProvider<BASE_RECORD_TYPE> {

    private static final int DEFAULT_MAX_SIZE = 256;

    private static final Logger log = LoggerFactory.getLogger(SpringTransactionCacheProvider.class);

    private static final boolean SAVEPOINT_CALLBACKS_SUPPORTED = savepointCallbacksSupported();

    private final int maxSize;

    private final boolean savepointCallbacksSupported;

    /**
     * Creates a provider holding at most 256 aggregates per transaction.
     */
    public SpringTransactionCacheProvider() {
        this(DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a provider.
     *
     * @param maxSize the maximum number of aggregates held per transaction
     */
    public SpringTransactionCacheProvider(int maxSize) {
        this(maxSize, SAVEPOINT_CALLBACKS_SUPPORTED);
    }

    SpringTransactionCacheProvider(int maxSize, boolean savepointCallbacksSupported) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0, but was " + maxSize);
        }
        this.maxSize = maxSize;
        this.savepointCallbacksSupported = savepointCallbacksSupported;
        if (!savepointCallbacksSupported) {
            log.warn("The transaction cache is off: this Spring version does not report a rollback to a savepoint "
                + "(TransactionSynchronization#savepointRollback, Spring 6.2 or later).");
        }
    }

    private static boolean savepointCallbacksSupported() {
        try {
            TransactionSynchronization.class.getMethod("savepointRollback", Object.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache() {
        if (!savepointCallbacksSupported
            || !TransactionSynchronizationManager.isSynchronizationActive()
            || !TransactionSynchronizationManager.isActualTransactionActive()) {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        var synchronization = (CacheSynchronization<BASE_RECORD_TYPE>) TransactionSynchronizationManager.getResource(this);
        if (synchronization != null && !synchronization.belongsToTheCurrentTransaction()) {
            // left bound by a transaction that ended without completing it on this thread
            TransactionSynchronizationManager.unbindResourceIfPossible(this);
            synchronization = null;
        }
        if (synchronization == null) {
            synchronization = new CacheSynchronization<>(this, new BoundedTransactionCache<>(maxSize));
            TransactionSynchronizationManager.bindResource(this, synchronization);
            TransactionSynchronizationManager.registerSynchronization(synchronization);
        }
        return Optional.of(synchronization.cache);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates no cache for a transaction that has none yet.
     */
    @Override
    public void clearCurrentTransactionCache() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        @SuppressWarnings("unchecked")
        var synchronization = (CacheSynchronization<BASE_RECORD_TYPE>) TransactionSynchronizationManager.getResource(this);
        if (synchronization != null) {
            synchronization.cache.clear();
        }
    }

    /**
     * Ties the cache of one transaction to its lifecycle, bound as resource of the transaction under the provider.
     */
    private static final class CacheSynchronization<BASE_RECORD_TYPE> implements TransactionSynchronization {

        private final Object resourceKey;

        private final BoundedTransactionCache<BASE_RECORD_TYPE> cache;

        private volatile boolean completed;

        private CacheSynchronization(Object resourceKey, BoundedTransactionCache<BASE_RECORD_TYPE> cache) {
            this.resourceKey = resourceKey;
            this.cache = cache;
        }

        /**
         * Whether this synchronization is registered with the transaction running on the current thread, and that
         * transaction has not completed yet.
         */
        private boolean belongsToTheCurrentTransaction() {
            return !completed && TransactionSynchronizationManager.getSynchronizations().stream()
                .anyMatch(registered -> registered == this);
        }

        @Override
        public void suspend() {
            TransactionSynchronizationManager.unbindResourceIfPossible(resourceKey);
        }

        @Override
        public void resume() {
            TransactionSynchronizationManager.bindResource(resourceKey, this);
        }

        @Override
        public void savepointRollback(Object savepoint) {
            cache.clear();
        }

        @Override
        public void afterCompletion(int status) {
            completed = true;
            // a no-op if the transaction completes on another thread than the one it ran on
            TransactionSynchronizationManager.unbindResourceIfPossible(resourceKey);
            cache.clear();
        }
    }
}

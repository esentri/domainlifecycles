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

import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheScope;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Opens and closes a {@link ThreadBoundTransactionCacheProvider} scope around a Spring-managed transaction,
 * for persistence technologies whose own native transaction hooks a Spring-managed transaction bypasses -
 * jOOQ's {@code TransactionListener} never fires for a transaction Spring's own {@code
 * DataSourceTransactionManager} began and will commit/roll back, and plain JDBC's {@code Connection}
 * proxying is bypassed the same way, since Spring commits/rolls back the physical connection directly.
 * <p>
 * Unlike those native hooks, this binder has no "transaction begin" event to react to - Spring raises no
 * such callback of its own - so {@link #ensureScopeOpenForCurrentTransaction()} is designed to be called
 * opportunistically, from whatever point a persistence-technology-specific integration already touches on
 * every operation regardless of transactional state (jOOQ's {@code ConnectionProvider.acquire()}, this
 * project's own {@code JdbcConnectionProvider.getConnection()}): it opens the scope on the first call within
 * a given Spring transaction and is a no-op on every subsequent call for that same transaction, so it is
 * always safe to call unconditionally on every such touch point.
 * <p>
 * Outside of an active Spring transaction (no transaction at all, or one governed by a different technology
 * entirely), this binder does nothing, leaving that case to whatever other binder is in play.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this binder is used with
 * @author Mario Herb
 */
public final class SpringTransactionCacheBinder<BASE_RECORD_TYPE> {

    private final ThreadBoundTransactionCacheProvider<BASE_RECORD_TYPE> transactionCacheProvider;

    /**
     * Creates a new SpringTransactionCacheBinder.
     *
     * @param transactionCacheProvider the transaction cache provider whose scope this binder opens and
     *                                 closes around the current Spring transaction
     */
    public SpringTransactionCacheBinder(ThreadBoundTransactionCacheProvider<BASE_RECORD_TYPE> transactionCacheProvider) {
        this.transactionCacheProvider = transactionCacheProvider;
    }

    /**
     * Opens a transaction cache scope for the currently active Spring transaction, unless one is already
     * open for it - a no-op if no Spring transaction is currently active on the calling thread. Safe to call
     * on every persistence operation, transactional or not.
     */
    public void ensureScopeOpenForCurrentTransaction() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        if (TransactionSynchronizationManager.hasResource(this)) {
            return;
        }
        TransactionCacheScope scope = transactionCacheProvider.open();
        TransactionSynchronizationManager.bindResource(this, scope);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                TransactionSynchronizationManager.unbindResourceIfPossible(SpringTransactionCacheBinder.this);
                scope.close();
            }
        });
    }
}

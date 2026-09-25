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

package io.domainlifecycles.jooq.cache;

import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheScope;
import org.jooq.Configuration;
import org.jooq.TransactionContext;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DefaultTransactionListenerProvider;

import java.util.Arrays;

/**
 * Opens and closes a {@link ThreadBoundTransactionCacheProvider} scope around jOOQ's own, Spring-independent
 * transaction lifecycle ({@link TransactionListener}), so that the transaction cache feature works
 * automatically - with or without Spring, since jOOQ's {@code SpringTransactionProvider} raises the very
 * same {@link TransactionListener} events.
 * <p>
 * A depth counter ensures that only the outermost transaction opens/closes the scope: nested
 * {@code dslContext.transaction(...)} calls (savepoints) must not tear down state that an outer transaction
 * is still relying on. {@link TransactionContext}/{@code Transaction} expose no public nesting flag of their
 * own to lean on instead, so this binder tracks nesting itself.
 *
 * @author Mario Herb
 */
public final class TransactionCacheJooqBinder implements TransactionListener {

    private final ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider;

    private final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    private final ThreadLocal<TransactionCacheScope> openScope = new ThreadLocal<>();

    /**
     * Creates a new TransactionCacheJooqBinder.
     *
     * @param transactionCacheProvider the transaction cache provider whose scope this binder opens and
     *                                 closes around jOOQ's transaction lifecycle
     */
    public TransactionCacheJooqBinder(ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider) {
        this.transactionCacheProvider = transactionCacheProvider;
    }

    /**
     * Registers a {@link TransactionCacheJooqBinder} for the given transaction cache provider on the given
     * jOOQ {@link Configuration}, unless one is already registered for that exact provider - so that
     * constructing several {@code JooqAggregateRepository} instances against the same, typically shared,
     * {@link Configuration} does not register (and therefore does not open/close the scope) more than once.
     *
     * @param configuration            the jOOQ configuration to register the binder on
     * @param transactionCacheProvider the transaction cache provider to bind to the configuration's
     *                                 transaction lifecycle
     */
    public static void registerOn(Configuration configuration,
                                  ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider) {
        boolean alreadyRegistered = Arrays.stream(configuration.transactionListenerProviders())
            .map(TransactionListenerProvider::provide)
            .anyMatch(listener -> listener instanceof TransactionCacheJooqBinder binder
                && binder.transactionCacheProvider == transactionCacheProvider);
        if (!alreadyRegistered) {
            configuration.setAppending(
                new DefaultTransactionListenerProvider(new TransactionCacheJooqBinder(transactionCacheProvider)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginStart(TransactionContext ctx) {
        int currentDepth = depth.get();
        if (currentDepth == 0) {
            openScope.set(transactionCacheProvider.open());
        }
        depth.set(currentDepth + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitEnd(TransactionContext ctx) {
        closeIfOutermost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollbackEnd(TransactionContext ctx) {
        closeIfOutermost();
    }

    private void closeIfOutermost() {
        int currentDepth = depth.get();
        if (currentDepth <= 1) {
            depth.set(0);
            TransactionCacheScope scope = openScope.get();
            if (scope != null) {
                openScope.remove();
                scope.close();
            }
        } else {
            depth.set(currentDepth - 1);
        }
    }
}

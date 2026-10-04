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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Opens and closes a {@link ThreadBoundTransactionCacheProvider} scope around jOOQ's own, Spring-independent
 * transaction lifecycle ({@link TransactionListener}), so that the transaction cache feature works
 * automatically - with or without Spring, since jOOQ's {@code SpringTransactionProvider} raises the very
 * same {@link TransactionListener} events.
 * <p>
 * Only the outermost transaction opens/closes the scope: nested {@code dslContext.transaction(...)} calls
 * (savepoints) must not tear down state that an outer transaction is still relying on. jOOQ exposes no nesting flag
 * of its own, so this binder follows the transactions running on the thread itself - each identified by the
 * {@link TransactionContext} jOOQ passes to all events of one transaction, so that an end reported twice (a failing
 * commit is reported as rolled back afterwards) never ends an outer transaction. A nested transaction that rolls
 * back to its savepoint clears the scope, though: entries loaded since the savepoint may hold state that was never
 * committed.
 *
 * @author Mario Herb
 */
public final class TransactionCacheJooqBinder implements TransactionListener {

    private final ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider;

    /**
     * The transactions jOOQ runs on the current thread, innermost first - each identified by its
     * {@link TransactionContext}, which jOOQ passes unchanged to every event of one transaction.
     */
    private final ThreadLocal<Deque<TransactionContext>> openTransactions = ThreadLocal.withInitial(ArrayDeque::new);

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
        var transactions = openTransactions.get();
        if (transactions.isEmpty()) {
            openScope.set(transactionCacheProvider.open());
        }
        transactions.push(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitEnd(TransactionContext ctx) {
        end(ctx, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollbackEnd(TransactionContext ctx) {
        end(ctx, true);
    }

    /**
     * Ends the given transaction: once none is left, the scope is closed; a nested transaction rolled back to its
     * savepoint clears it, since entries loaded since may hold state that was never committed. jOOQ reports a
     * transaction whose commit failed as rolled back afterwards - that second end leaves the transactions still
     * running alone, but clears the scope as well.
     */
    private void end(TransactionContext ctx, boolean rolledBack) {
        var transactions = openTransactions.get();
        if (containsSame(transactions, ctx)) {
            TransactionContext ended;
            do {
                ended = transactions.pop();
            } while (ended != ctx);
        }
        TransactionCacheScope scope = openScope.get();
        if (scope == null) {
            return;
        }
        if (transactions.isEmpty()) {
            openScope.remove();
            openTransactions.remove();
            scope.close();
        } else if (rolledBack) {
            scope.clear();
        }
    }

    private static boolean containsSame(Deque<TransactionContext> transactions, TransactionContext ctx) {
        return transactions.stream().anyMatch(transaction -> transaction == ctx);
    }
}

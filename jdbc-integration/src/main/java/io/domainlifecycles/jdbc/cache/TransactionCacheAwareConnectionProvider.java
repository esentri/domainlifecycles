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

package io.domainlifecycles.jdbc.cache;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheScope;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * Decorates another {@link JdbcConnectionProvider}, opening and closing a
 * {@link ThreadBoundTransactionCacheProvider} scope around plain JDBC's transaction boundary, so that the
 * transaction cache feature works automatically - with or without Spring, since a
 * {@code DataSourceTransactionManager} ultimately calls {@code commit()}/{@code rollback()} on the very same
 * connection.
 * <p>
 * Plain {@code java.sql} has no transaction listener SPI of its own (unlike jOOQ's
 * {@code org.jooq.TransactionListener}), so this decorator wraps every {@link Connection} the delegate
 * returns in a dynamic proxy that intercepts {@code commit()}/{@code rollback()}. No depth counter is
 * needed: a JDBC savepoint ({@code Connection.setSavepoint()}) never itself calls {@code commit()}/
 * {@code rollback()} on the connection, so an actual call to either of those methods is already, reliably,
 * the outermost transaction boundary.
 * <p>
 * As a second, independent signal, a scope is also (re-)opened whenever the delegate starts returning a
 * <em>different</em> {@link Connection} instance than the previous call - covering callers that manage a
 * connection's transaction boundary themselves and never call {@code commit()}/{@code rollback()} on the
 * exact object this provider handed out (e.g. a test harness driving the underlying connection directly).
 * Should a previous scope still be open at that point - because neither signal fired for it, e.g. its
 * transaction ended via a bypassed {@code rollback()} - {@link ThreadBoundTransactionCacheProvider#open()}'s
 * self-healing discards it, so this can only ever cost wasted memory between transactions, never incorrect
 * data (see {@link ThreadBoundTransactionCacheProvider#open()}).
 *
 * @author Mario Herb
 */
public final class TransactionCacheAwareConnectionProvider implements JdbcConnectionProvider {

    private final JdbcConnectionProvider delegate;

    private final ThreadBoundTransactionCacheProvider<JdbcRecord> transactionCacheProvider;

    private final ThreadLocal<TransactionCacheScope> openScope = new ThreadLocal<>();

    private final ThreadLocal<Connection> lastSeenRealConnection = new ThreadLocal<>();

    /**
     * Creates a new TransactionCacheAwareConnectionProvider.
     *
     * @param delegate                 the connection provider to decorate
     * @param transactionCacheProvider the transaction cache provider whose scope this decorator opens and
     *                                 closes around the connection's transaction boundary
     */
    public TransactionCacheAwareConnectionProvider(JdbcConnectionProvider delegate,
                                                    ThreadBoundTransactionCacheProvider<JdbcRecord> transactionCacheProvider) {
        this.delegate = delegate;
        this.transactionCacheProvider = transactionCacheProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
        Connection real = delegate.getConnection();
        if (openScope.get() == null || lastSeenRealConnection.get() != real) {
            openScope.set(transactionCacheProvider.open());
        }
        lastSeenRealConnection.set(real);
        return (Connection) Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class<?>[]{Connection.class},
            (proxy, method, args) -> invoke(real, method, args)
        );
    }

    private Object invoke(Connection real, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            result = method.invoke(real, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        boolean isTransactionBoundary = args == null
            && ("commit".equals(method.getName()) || "rollback".equals(method.getName()));
        if (isTransactionBoundary) {
            TransactionCacheScope scope = openScope.get();
            if (scope != null) {
                openScope.remove();
                scope.close();
            }
        }
        return result;
    }
}

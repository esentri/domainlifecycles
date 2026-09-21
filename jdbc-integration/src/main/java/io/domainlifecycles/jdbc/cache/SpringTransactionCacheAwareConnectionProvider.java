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
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheBinder;

import java.sql.Connection;

/**
 * Decorates another {@link JdbcConnectionProvider}, ensuring a transaction cache scope is open for the
 * currently active Spring transaction (see {@link SpringTransactionCacheBinder}) before every
 * {@link #getConnection()} - the one call every operation makes regardless of transactional state, unlike
 * {@link TransactionCacheAwareConnectionProvider}'s {@code commit()}/{@code rollback()} proxying, which a
 * Spring-managed transaction bypasses entirely (Spring's {@code DataSourceTransactionManager} commits/rolls
 * back the physical {@link Connection} directly), which makes {@code getConnection()} the one reliable hook
 * for cache activation under Spring.
 * <p>
 * Intended for use alongside (not instead of) {@link TransactionCacheAwareConnectionProvider}: this
 * decorator's {@link SpringTransactionCacheBinder#ensureScopeOpenForCurrentTransaction()} is a no-op outside
 * of an active Spring transaction, so plain, non-Spring JDBC usage continues to work exactly as before, via
 * {@link TransactionCacheAwareConnectionProvider} alone.
 *
 * @author Mario Herb
 */
public final class SpringTransactionCacheAwareConnectionProvider implements JdbcConnectionProvider {

    private final JdbcConnectionProvider delegate;

    private final SpringTransactionCacheBinder<JdbcRecord> springTransactionCacheBinder;

    /**
     * Creates a new SpringTransactionCacheAwareConnectionProvider.
     *
     * @param delegate                     the connection provider to decorate
     * @param springTransactionCacheBinder the binder to ensure a transaction cache scope is open for the
     *                                     currently active Spring transaction, if any, before every
     *                                     {@link #getConnection()}
     */
    public SpringTransactionCacheAwareConnectionProvider(
        JdbcConnectionProvider delegate, SpringTransactionCacheBinder<JdbcRecord> springTransactionCacheBinder) {
        this.delegate = delegate;
        this.springTransactionCacheBinder = springTransactionCacheBinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
        springTransactionCacheBinder.ensureScopeOpenForCurrentTransaction();
        return delegate.getConnection();
    }
}

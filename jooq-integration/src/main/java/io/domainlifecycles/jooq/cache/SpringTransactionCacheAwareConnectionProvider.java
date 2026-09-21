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

import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheBinder;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.UpdatableRecord;

import java.sql.Connection;

/**
 * Decorates another {@link ConnectionProvider}, ensuring a transaction cache scope is open for the currently
 * active Spring transaction (see {@link SpringTransactionCacheBinder}) before every {@link #acquire()}. jOOQ
 * calls {@link ConnectionProvider#acquire()} for every operation regardless of whether it was ever asked to
 * manage the transaction itself - unlike {@link Configuration}'s {@code TransactionListener}s (see {@link
 * TransactionCacheJooqBinder}), which never fire under a purely Spring-managed ({@code @Transactional})
 * transaction, since jOOQ's own transaction lifecycle is never otherwise engaged there - which makes {@code
 * acquire()} the one reliable hook for cache activation in that case.
 * <p>
 * Intended for the Spring Boot auto-configuration, alongside (not instead of) {@link TransactionCacheJooqBinder}:
 * this decorator's {@link SpringTransactionCacheBinder#ensureScopeOpenForCurrentTransaction()} is a no-op
 * outside of an active Spring transaction, so standalone jOOQ usage (no Spring, or an explicit
 * {@code dslContext.transaction(...)} call) continues to work exactly as before, via
 * {@link TransactionCacheJooqBinder} alone.
 *
 * @author Mario Herb
 */
public final class SpringTransactionCacheAwareConnectionProvider implements ConnectionProvider {

    private final ConnectionProvider delegate;

    private final SpringTransactionCacheBinder<UpdatableRecord<?>> springTransactionCacheBinder;

    /**
     * Creates a new SpringTransactionCacheAwareConnectionProvider.
     *
     * @param delegate                      the connection provider to decorate
     * @param springTransactionCacheBinder  the binder to ensure a transaction cache scope is open for the
     *                                      currently active Spring transaction, if any, before every
     *                                      {@link #acquire()}
     */
    public SpringTransactionCacheAwareConnectionProvider(
        ConnectionProvider delegate, SpringTransactionCacheBinder<UpdatableRecord<?>> springTransactionCacheBinder) {
        this.delegate = delegate;
        this.springTransactionCacheBinder = springTransactionCacheBinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection acquire() {
        springTransactionCacheBinder.ensureScopeOpenForCurrentTransaction();
        return delegate.acquire();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Connection connection) {
        delegate.release(connection);
    }
}

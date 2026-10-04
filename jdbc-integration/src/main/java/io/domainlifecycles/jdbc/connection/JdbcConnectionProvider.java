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

package io.domainlifecycles.jdbc.connection;

import java.sql.Connection;

/**
 * Supplies the JDBC {@link Connection} that persistence operations should run on.
 * <p>
 * This is the plain JDBC analogue of jOOQ's {@code DSLContext}/{@code Configuration}: where jOOQ resolves its
 * own connection lazily (and transaction-awarely) from a configured {@code ConnectionProvider} every time a
 * query runs, this module's persister, fetcher and id providers likewise call {@link #getConnection()} at the
 * point of use rather than caching a single {@code Connection} at construction time - so that an
 * implementation backed by a transaction manager (e.g. Spring's {@code DataSourceUtils}, in a future
 * integration) can hand out the connection bound to the currently active transaction.
 * <p>
 * Every connection obtained via {@link #getConnection()} is handed back via {@link #releaseConnection(Connection)}
 * once the operation is done - the analogue of jOOQ's {@code ConnectionProvider.acquire()}/{@code release()}. An
 * implementation handing out a new connection outside a transaction, e.g. one backed by Spring's
 * {@code DataSourceUtils}, closes it there, while a connection bound to a running transaction stays open.
 * <p>
 * For standalone use (and for this module's own tests), {@link SingleJdbcConnectionProvider} always returns
 * one fixed connection.
 *
 * @author Mario Herb
 */
public interface JdbcConnectionProvider {

    /**
     * Returns the connection persistence operations should currently run on.
     *
     * @return the connection
     */
    Connection getConnection();

    /**
     * Hands back a connection obtained via {@link #getConnection()}, once the operation it was obtained for is
     * done. Does nothing by default, for implementations handing out one connection they manage themselves.
     *
     * @param connection the connection obtained via {@link #getConnection()}
     */
    default void releaseConnection(Connection connection) {
    }
}

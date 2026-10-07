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
import java.util.Objects;

/**
 * A {@link JdbcConnectionProvider} that always returns one fixed connection, supplied at construction time.
 * <p>
 * Suitable for standalone use and tests, where the caller manages one connection's lifecycle (and its
 * transaction boundaries) directly. It is not transaction-manager-aware: repeated calls always return the
 * very same {@link Connection} instance, regardless of any surrounding transaction demarcation.
 *
 * @author Mario Herb
 */
public final class SingleJdbcConnectionProvider implements JdbcConnectionProvider {

    private final Connection connection;

    /**
     * Constructs a new instance of {@code SingleJdbcConnectionProvider}.
     *
     * @param connection the connection to always return
     */
    public SingleJdbcConnectionProvider(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
        return connection;
    }
}

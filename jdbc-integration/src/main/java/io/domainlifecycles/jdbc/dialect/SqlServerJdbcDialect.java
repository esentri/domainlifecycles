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

package io.domainlifecycles.jdbc.dialect;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link JdbcDialect} for Microsoft SQL Server (2012 and later, where {@code SEQUENCE} objects were
 * introduced).
 * <p>
 * SQL Server supports the same ANSI {@code NEXT VALUE FOR} syntax as H2, so this differs from {@link
 * H2JdbcDialect} only in name.
 *
 * @author Mario Herb
 */
public final class SqlServerJdbcDialect implements JdbcDialect {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "SQL Server";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        return JdbcDialect.executeScalarLongQuery(connection, "SELECT NEXT VALUE FOR " + sequenceName);
    }
}

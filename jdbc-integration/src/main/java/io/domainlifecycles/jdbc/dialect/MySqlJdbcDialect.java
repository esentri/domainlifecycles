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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * {@link JdbcDialect} for MySQL.
 * <p>
 * Unlike every other dialect this module supports, standard MySQL has no native {@code SEQUENCE} object at
 * all (MariaDB added one in 10.3, but MySQL itself never has, even as of 8.x). Wherever this module's naming
 * conventions call for a sequence (e.g. {@code TEST_ROOT_SIMPLE_ID_SEQ}), a MySQL schema must instead provide
 * a one-row, one-column table of that same name:
 * <pre>{@code
 * CREATE TABLE test_root_simple_id_seq (next_val BIGINT NOT NULL);
 * INSERT INTO test_root_simple_id_seq (next_val) VALUES (0);
 * }</pre>
 * The next value is then obtained via the standard MySQL sequence emulation idiom - an {@code UPDATE} that
 * increments the row while capturing the new value through {@code LAST_INSERT_ID(expr)} (which sets the
 * session-scoped "last insert id" to the value of {@code expr}, safely per-connection even under concurrent
 * updates on the same row, since the row-level lock the {@code UPDATE} takes serialises concurrent
 * increments), followed by reading that value back with {@code SELECT LAST_INSERT_ID()}.
 * <p>
 * Every identifier is backtick-quoted (see {@link #quoteIdentifier(String)}): MySQL has an unusually large
 * reserved word list, including several that look like perfectly ordinary column names (e.g. {@code
 * YEAR_MONTH}, reserved for {@code INTERVAL} expressions) and would otherwise fail with a syntax error the
 * moment a domain field happened to be named that way.
 *
 * @author Mario Herb
 */
public final class MySqlJdbcDialect implements JdbcDialect {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "MySQL";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int affected = statement.executeUpdate(
                "UPDATE " + sequenceName + " SET next_val = LAST_INSERT_ID(next_val + 1)");
            if (affected == 0) {
                throw new SQLException("Sequence table '" + sequenceName + "' has no row to increment. Create "
                    + "it with a single row, e.g. INSERT INTO " + sequenceName + " (next_val) VALUES (0).");
            }
            try (ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()")) {
                if (!resultSet.next()) {
                    throw new SQLException("SELECT LAST_INSERT_ID() returned no value.");
                }
                return resultSet.getLong(1);
            }
        }
    }
}

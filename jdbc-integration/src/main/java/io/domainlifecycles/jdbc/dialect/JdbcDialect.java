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
 * Encapsulates the small set of SQL differences between database products that this module's otherwise
 * database-agnostic SQL generation needs to know about. jOOQ absorbs these differences internally; plain
 * JDBC does not, so they are made explicit here instead.
 * <p>
 * Most dialects implement "next sequence value" as a single scalar {@code SELECT} against a native
 * {@code SEQUENCE} object (see {@link #executeScalarLongQuery(Connection, String)} for a shared helper those
 * implementations can delegate to); {@link MySqlJdbcDialect} is the one exception, since standard MySQL has
 * no native sequence object at all and must emulate one across two statements instead - which is why this
 * method executes whatever the dialect needs directly against a {@link Connection}, rather than merely
 * returning a SQL string for a caller to run.
 *
 * @author Mario Herb
 */
public interface JdbcDialect {

    /**
     * Returns a short, descriptive name of this dialect (e.g. {@code "H2"}), used in log and error messages.
     *
     * @return the dialect name
     */
    String name();

    /**
     * Returns the next value of the given sequence (or sequence-like construct).
     *
     * @param connection   the connection to execute against
     * @param sequenceName the physical name of the sequence
     * @return the next sequence value
     * @throws SQLException if the sequence does not exist, or its next value could otherwise not be obtained
     */
    long nextSequenceValue(Connection connection, String sequenceName) throws SQLException;

    /**
     * Executes {@code sql} and returns the single {@code long} value of its first row/column - a shared
     * helper for the (majority of) dialects whose {@link #nextSequenceValue(Connection, String)} is a single
     * scalar {@code SELECT} statement.
     *
     * @param connection the connection to execute against
     * @param sql        the scalar query to execute
     * @return the single long value the query returned
     * @throws SQLException if the query fails, or returns no rows
     */
    static long executeScalarLongQuery(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new SQLException("Query '" + sql + "' returned no value.");
            }
            return resultSet.getLong(1);
        }
    }
}

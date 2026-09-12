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

package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Reads the next value of a named database sequence, shared by {@link JdbcEntityIdentityProvider} (entity ids,
 * keyed by identity type name) and {@link JdbcValueObjectIdProvider} (value object ids, keyed by table name) -
 * the two providers use different naming conventions for the sequence name itself, but both ultimately need
 * to execute the same kind of "next value" query and tolerate the sequence name being reported back in
 * lowercase by some databases.
 *
 * @author Mario Herb
 */
final class JdbcSequenceIdGenerator {

    private JdbcSequenceIdGenerator() {
    }

    /**
     * Returns the next value of the sequence with the given name.
     *
     * @param connection   the connection to query
     * @param dialect      the dialect providing the "next value" SQL syntax
     * @param sequenceName the sequence name, tried as given and, if that fails, in all-lowercase
     * @return the next sequence value
     * @throws DLCPersistenceException if no sequence with that name (in either casing) exists
     */
    static long nextValue(Connection connection, JdbcDialect dialect, String sequenceName) {
        try {
            return executeNextValue(connection, dialect, sequenceName);
        } catch (SQLException primaryFailure) {
            try {
                return executeNextValue(connection, dialect, sequenceName.toLowerCase());
            } catch (SQLException fallbackFailure) {
                throw DLCPersistenceException.fail(
                    "Sequence '%s' not found. Please create the sequence in your database!", primaryFailure,
                    sequenceName);
            }
        }
    }

    private static long executeNextValue(Connection connection, JdbcDialect dialect, String sequenceName)
        throws SQLException {
        var sql = dialect.nextSequenceValueSql(sequenceName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw DLCPersistenceException.fail("Sequence '%s' returned no value.", sequenceName);
            }
            return resultSet.getLong(1);
        }
    }
}

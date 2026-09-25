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
 * {@link JdbcDialect} for PostgreSQL.
 * <p>
 * {@code nextval(regclass)} resolves its string argument the same way an unquoted identifier would (folding
 * it to lower case before lookup), so a sequence created by an unquoted {@code CREATE SEQUENCE
 * test_root_simple_id_seq} is found even though this module's naming conventions build the name in upper
 * case (e.g. {@code TEST_ROOT_SIMPLE_ID_SEQ}); the additional lower-case retry {@link
 * io.domainlifecycles.jdbc.imp.JdbcSequenceIdGenerator} performs on failure exists for databases without that
 * folding behaviour (H2 folds unquoted identifiers to upper case by default), not specifically for this one.
 *
 * @author Mario Herb
 */
public final class PostgresJdbcDialect implements JdbcDialect {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "PostgreSQL";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        return JdbcDialect.executeScalarLongQuery(connection, "SELECT nextval('" + sequenceName + "')");
    }
}

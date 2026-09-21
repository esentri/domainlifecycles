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

import io.domainlifecycles.jdbc.schema.TableMetadata;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link JdbcDialect} for Oracle Database.
 * <p>
 * Unlike the other supported dialects, Oracle requires a {@code FROM} clause even for a query that selects
 * no table data, hence {@code FROM DUAL}.
 *
 * @author Mario Herb
 */
public final class OracleJdbcDialect implements JdbcDialect {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "Oracle";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        return JdbcDialect.executeScalarLongQuery(connection, "SELECT " + sequenceName + ".NEXTVAL FROM DUAL");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Oracle has no {@code LIMIT}/{@code OFFSET} keywords either (its legacy pagination idiom is {@code
     * ROWNUM}); Oracle 12c and later support the same ANSI {@code OFFSET ? ROWS FETCH NEXT ? ROWS ONLY}
     * clause SQL Server does, bound offset-then-page-size.
     */
    @Override
    public PagedSelect pagedSelectSql(TableMetadata table, String orderByColumnName, int offset, int pageSize) {
        var sql = "SELECT * FROM " + quotedTableName(table)
            + " ORDER BY " + quoteIdentifier(orderByColumnName) + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return new PagedSelect(sql, new Object[]{offset, pageSize});
    }
}

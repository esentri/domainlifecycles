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

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.persistence.fetcher.RecordProvider;

import java.util.List;
import java.util.Objects;

/**
 * Base class for hand-written {@link RecordProvider} implementations that issue their own SQL against plain
 * JDBC (registered via {@code AggregateFetcher#withRecordProvider}, for a relationship the standard {@link
 * JdbcAggregateFetcher} resolution can't or shouldn't resolve on its own).
 * <p>
 * A subclass only needs to override {@link RecordProvider#provide} and/or {@link RecordProvider#provideCollection}
 * and call one of the {@code select*} methods below to obtain the child record(s) - the {@code
 * PreparedStatement} -&gt; {@code ResultSet} -&gt; {@link JdbcRecord} mapping is handled by {@link
 * JdbcRecordMapper} underneath, so it doesn't have to be re-implemented in every custom provider; {@link
 * #selectByColumn} and {@link #selectOneByColumn} are quoted exactly as every other statement the injected
 * {@link JdbcDialect} generates.
 *
 * @author Mario Herb
 */
public abstract class JdbcRecordProvider implements RecordProvider<JdbcRecord, JdbcRecord> {

    /**
     * Supplies the connection used to execute this provider's queries.
     */
    protected final JdbcConnectionProvider connectionProvider;

    /**
     * Builds the generated {@link #selectByColumn}/{@link #selectOneByColumn} statements, quoting identifiers
     * as it requires.
     */
    protected final JdbcDialect dialect;

    /**
     * Constructs a new instance of {@code JdbcRecordProvider}.
     *
     * @param domainPersistenceProvider supplies the connection and dialect used to execute this provider's
     *                                  queries, registered centrally on it
     */
    protected JdbcRecordProvider(JdbcDomainPersistenceProvider domainPersistenceProvider) {
        this.connectionProvider = Objects.requireNonNull(domainPersistenceProvider.connectionProvider);
        this.dialect = Objects.requireNonNull(domainPersistenceProvider.dialect);
    }

    /**
     * Executes the given SQL with the given positional parameters and maps every returned row onto a {@link
     * JdbcRecord}. See {@link JdbcRecordMapper#selectWithSql}.
     *
     * @param table  the table metadata describing the columns to read
     * @param sql    the SQL to execute, with {@code ?} placeholders for {@code params}
     * @param params the positional parameter values to bind
     * @return the mapped rows, in the order returned by the database
     */
    protected List<JdbcRecord> selectWithSql(TableMetadata table, String sql, Object... params) {
        return JdbcRecordMapper.selectWithSql(connectionProvider, table, sql, params);
    }

    /**
     * Convenience shorthand for {@link #selectWithSql} building the common {@code SELECT * FROM <table> WHERE
     * <columnName> = ?} query. See {@link JdbcRecordMapper#selectByColumn}.
     *
     * @param table      the table metadata describing the columns to read
     * @param columnName the physical column to filter on
     * @param value      the value to filter for
     * @return the mapped rows, in the order returned by the database
     */
    protected List<JdbcRecord> selectByColumn(TableMetadata table, String columnName, Object value) {
        return JdbcRecordMapper.selectByColumn(connectionProvider, dialect, table, columnName, value);
    }

    /**
     * Like {@link #selectWithSql}, but expects at most one matching row. See {@link JdbcRecordMapper#selectOne}.
     *
     * @param table  the table metadata describing the columns to read
     * @param sql    the SQL to execute, with {@code ?} placeholders for {@code params}
     * @param params the positional parameter values to bind
     * @return the single mapped row, or {@code null} if none matched
     */
    protected JdbcRecord selectOne(TableMetadata table, String sql, Object... params) {
        return JdbcRecordMapper.selectOne(connectionProvider, table, sql, params);
    }

    /**
     * Convenience shorthand for {@link #selectOne} building the common {@code SELECT * FROM <table> WHERE
     * <columnName> = ?} query. See {@link JdbcRecordMapper#selectOneByColumn}.
     *
     * @param table      the table metadata describing the columns to read
     * @param columnName the physical column to filter on
     * @param value      the value to filter for
     * @return the single mapped row, or {@code null} if none matched
     */
    protected JdbcRecord selectOneByColumn(TableMetadata table, String columnName, Object value) {
        return JdbcRecordMapper.selectOneByColumn(connectionProvider, dialect, table, columnName, value);
    }
}

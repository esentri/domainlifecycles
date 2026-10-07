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

package io.domainlifecycles.jdbc.util;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Maps plain JDBC {@link ResultSet} rows onto {@link JdbcRecord} instances, and runs simple parameterized
 * queries that do the same.
 * <p>
 * Intended for hand-written {@link io.domainlifecycles.persistence.fetcher.RecordProvider} implementations
 * (registered via {@code AggregateFetcher#withRecordProvider}) and custom repository finder methods that issue
 * their own SQL instead of going through the standard {@link io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher}
 * resolution - so they don't each have to hand-roll the same "{@code PreparedStatement} -&gt; {@code
 * ResultSet} -&gt; {@code JdbcRecord}" boilerplate. This is the same mapping {@code JdbcAggregateFetcher}
 * itself uses internally. {@link io.domainlifecycles.jdbc.imp.JdbcRecordProvider} wraps these as convenient
 * instance methods for a {@code RecordProvider} implementation to call directly.
 *
 * @author Mario Herb
 */
public final class JdbcRecordMapper {

    private JdbcRecordMapper() {
    }

    /**
     * Maps the current row of the given {@link ResultSet} onto a new {@link JdbcRecord} for the given table,
     * reading every column {@code table} declares. The query is expected to have selected them all (e.g. via
     * {@code SELECT *}).
     *
     * @param resultSet the result set, already positioned on the row to map (e.g. after a successful {@code
     *                  resultSet.next()})
     * @param table     the table metadata describing the columns to read
     * @return the mapped record
     * @throws SQLException if reading a column value fails
     */
    public static JdbcRecord mapRow(ResultSet resultSet, TableMetadata table) throws SQLException {
        var record = new JdbcRecord(table.name());
        for (var column : table.columns()) {
            record.set(column.name(), resultSet.getObject(column.name(), column.javaType()));
        }
        return record;
    }

    /**
     * Executes the given SQL with the given positional parameters and maps every returned row onto a {@link
     * JdbcRecord} via {@link #mapRow(ResultSet, TableMetadata)}.
     * <p>
     * Unlike {@link #selectByColumn}/{@link #selectOneByColumn}, this does <em>not</em> apply {@link
     * #normalizeForColumn}: an arbitrary {@code sql} string has no declared column for a given positional
     * parameter to look up a target physical type against (it may not even be a {@code WHERE column = ?}
     * comparison at all). A caller binding a {@link UUID} value against a column whose physical
     * representation is {@link String} or {@code byte[]} (see {@link #normalizeForColumn}) is responsible for
     * converting it to that representation itself before calling this method - the same way it is already
     * responsible for getting every other aspect of its own hand-written {@code sql} right.
     *
     * @param connectionProvider supplies the connection to run the query on
     * @param table              the table metadata describing the columns to read
     * @param sql                the SQL to execute, with {@code ?} placeholders for {@code params}
     * @param params             the positional parameter values to bind, already in the physical
     *                           representation each corresponding column expects
     * @return the mapped rows, in the order returned by the database
     * @throws DLCPersistenceException if the query fails
     */
    public static List<JdbcRecord> selectWithSql(
        JdbcConnectionProvider connectionProvider, TableMetadata table, String sql, Object... params
    ) {
        var connection = connectionProvider.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<JdbcRecord> rows = new ArrayList<>();
                while (resultSet.next()) {
                    rows.add(mapRow(resultSet, table));
                }
                return rows;
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Query on '%s' failed.", e, table.name());
        } finally {
            connectionProvider.releaseConnection(connection);
        }
    }

    /**
     * Convenience shorthand for {@link #selectWithSql} building the common {@code SELECT * FROM <table> WHERE
     * <columnName> = ?} query via {@link JdbcDialect#selectByColumnSql(TableMetadata, String)}, so table and
     * column names are quoted exactly as every other statement {@code dialect} generates.
     *
     * @param connectionProvider supplies the connection to run the query on
     * @param dialect            builds the {@code SELECT} statement, quoting identifiers as it requires
     * @param table              the table metadata describing the columns to read
     * @param columnName         the physical column to filter on
     * @param value              the value to filter for
     * @return the mapped rows, in the order returned by the database
     * @throws DLCPersistenceException if the query fails
     */
    public static List<JdbcRecord> selectByColumn(
        JdbcConnectionProvider connectionProvider, JdbcDialect dialect, TableMetadata table, String columnName,
        Object value
    ) {
        return selectWithSql(
            connectionProvider, table, dialect.selectByColumnSql(table, columnName),
            normalizeForColumn(table, columnName, value));
    }

    /**
     * Like {@link #selectWithSql}, but expects at most one matching row. Applies no UUID normalization
     * either, for exactly the same reason - see {@link #selectWithSql}'s Javadoc.
     *
     * @param connectionProvider supplies the connection to run the query on
     * @param table              the table metadata describing the columns to read
     * @param sql                the SQL to execute, with {@code ?} placeholders for {@code params}
     * @param params             the positional parameter values to bind, already in the physical
     *                           representation each corresponding column expects
     * @return the single mapped row, or {@code null} if none matched
     * @throws DLCPersistenceException if the query fails, or more than one row matched
     */
    public static JdbcRecord selectOne(
        JdbcConnectionProvider connectionProvider, TableMetadata table, String sql, Object... params
    ) {
        var rows = selectWithSql(connectionProvider, table, sql, params);
        if (rows.size() > 1) {
            throw DLCPersistenceException.fail(
                "More than one row found in table '%s' for a query that expected at most one.", table.name());
        }
        return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * Convenience shorthand for {@link #selectOne} building the common {@code SELECT * FROM <table> WHERE
     * <columnName> = ?} query via {@link JdbcDialect#selectByColumnSql(TableMetadata, String)}, so table and
     * column names are quoted exactly as every other statement {@code dialect} generates.
     *
     * @param connectionProvider supplies the connection to run the query on
     * @param dialect            builds the {@code SELECT} statement, quoting identifiers as it requires
     * @param table              the table metadata describing the columns to read
     * @param columnName         the physical column to filter on
     * @param value              the value to filter for
     * @return the single mapped row, or {@code null} if none matched
     * @throws DLCPersistenceException if the query fails, or more than one row matched
     */
    public static JdbcRecord selectOneByColumn(
        JdbcConnectionProvider connectionProvider, JdbcDialect dialect, TableMetadata table, String columnName,
        Object value
    ) {
        return selectOne(
            connectionProvider, table, dialect.selectByColumnSql(table, columnName),
            normalizeForColumn(table, columnName, value));
    }

    /**
     * Converts a {@link UUID} filter value to whatever physical representation ({@link UUID}, {@link String}
     * or {@code byte[]}) the target column actually holds, mirroring {@code JdbcValueObjectIdProvider}'s
     * identical conversion for insert values. Without this, a UUID stored in a {@code VARCHAR}/{@code CHAR}
     * column (the common case - see {@code db/migration-*}'s {@code VARCHAR2(36)} id columns) binds as a
     * {@code uuid}-typed parameter on strict dialects like Postgres, which then reject the generated {@code
     * WHERE column = ?} comparison with "operator does not exist: character varying = uuid" - Postgres does
     * allow the same UUID-to-text value on {@code INSERT} (an assignment cast), which is why this surfaces
     * only on the read side. Any value that is not a {@link UUID}, or whose target column already is one,
     * passes through unchanged.
     * <p>
     * Only {@link #selectByColumn}/{@link #selectOneByColumn} apply this - they alone know, from {@code
     * columnName}, which single column a filter value is compared against. {@link #selectWithSql}/{@link
     * #selectOne} do not and cannot apply it (see their Javadoc): a caller binding a raw {@link UUID} value
     * through those two is responsible for this same conversion itself.
     */
    private static Object normalizeForColumn(TableMetadata table, String columnName, Object value) {
        if (!(value instanceof UUID uuid)) {
            return value;
        }
        var physicalType = table.column(columnName).javaType();
        if (physicalType == String.class) {
            return uuid.toString();
        }
        if (physicalType == byte[].class) {
            var bytes = new byte[16];
            ByteBuffer.wrap(bytes).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
            return bytes;
        }
        return value;
    }
}

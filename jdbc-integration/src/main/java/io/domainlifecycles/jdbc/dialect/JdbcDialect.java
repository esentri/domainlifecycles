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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Encapsulates the SQL differences between database products that this module's otherwise database-agnostic
 * SQL generation needs to know about. jOOQ absorbs these differences internally; plain JDBC does not, so they
 * are made explicit here instead.
 * <p>
 * Most dialects implement "next sequence value" as a single scalar {@code SELECT} against a native
 * {@code SEQUENCE} object (see {@link #executeScalarLongQuery(Connection, String)} for a shared helper those
 * implementations can delegate to); {@link MySqlJdbcDialect} is the one exception, since standard MySQL has
 * no native sequence object at all and must emulate one across two statements instead - which is why this
 * method executes whatever the dialect needs directly against a {@link Connection}, rather than merely
 * returning a SQL string for a caller to run.
 * <p>
 * {@link #insertSql}, {@link #updateSql} and {@link #deleteSql} are the SQL statements {@code JdbcPersister}
 * executes for every insert/update/delete - each has a default implementation building plain, unquoted ANSI
 * SQL (this module's behaviour before these hooks existed), so an implementation only needs to override the
 * ones it actually needs to change. The most common reason to do so is identifier quoting (reserved words,
 * case-sensitive identifiers, ...): {@link #quoteIdentifier(String)} defaults to no quoting at all, and the
 * default {@code insertSql}/{@code updateSql}/{@code deleteSql} implementations route every table and column
 * name through it, so overriding just that one method already adjusts all three statements consistently.
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

    /**
     * Quotes a single physical identifier (a table, schema or column name) for inclusion in generated SQL,
     * e.g. to preserve case-sensitivity or to allow a reserved word as an identifier. The default
     * implementation performs no quoting at all, matching this module's behaviour before this hook existed.
     *
     * @param identifier the physical identifier to quote
     * @return the identifier, quoted as this dialect requires
     */
    default String quoteIdentifier(String identifier) {
        return identifier;
    }

    /**
     * Returns the (possibly schema-qualified) SQL reference for {@code table}, with the schema and table name
     * quoted separately via {@link #quoteIdentifier(String)} (so a quoted, schema-qualified name never quotes
     * across the separating {@code .}).
     *
     * @param table the table to reference
     * @return the quoted, possibly schema-qualified table reference
     */
    default String quotedTableName(TableMetadata table) {
        var name = quoteIdentifier(table.name());
        return (table.schema() == null || table.schema().isBlank())
            ? name
            : quoteIdentifier(table.schema()) + "." + name;
    }

    /**
     * Builds the {@code INSERT} statement for a full-column insert into {@code table}. The {@code ?}
     * placeholders are positioned in the same order as {@code columnNames}, so a caller must bind parameters
     * in that exact order.
     *
     * @param table       the table to insert into
     * @param columnNames the physical names of every column to insert, in the order to bind them
     * @return the {@code INSERT} statement, with one {@code ?} placeholder per column
     */
    default String insertSql(TableMetadata table, List<String> columnNames) {
        var columns = columnNames.stream().map(this::quoteIdentifier).collect(Collectors.joining(", "));
        var placeholders = columnNames.stream().map(c -> "?").collect(Collectors.joining(", "));
        return "INSERT INTO " + quotedTableName(table) + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    /**
     * Builds the {@code UPDATE} statement setting {@code setColumnNames} on the row identified by
     * {@code pkColumnName}, optionally also requiring {@code concurrencyColumnName} to still hold its previous
     * value (optimistic locking). The {@code ?} placeholders appear, in order: one per entry of {@code
     * setColumnNames}, then (if {@code versionChecked}) one for the new concurrency version, then one for the
     * primary key value, then (if {@code versionChecked}) one for the previous concurrency version - a caller
     * must bind parameters in that exact order.
     *
     * @param table                 the table to update
     * @param setColumnNames        the physical names of every column to set, in the order to bind them (must
     *                              not include {@code pkColumnName} or {@code concurrencyColumnName})
     * @param pkColumnName          the physical name of the primary key column identifying the row
     * @param concurrencyColumnName the physical name of the optimistic locking column, or {@code null} if
     *                              {@code versionChecked} is {@code false}
     * @param versionChecked        whether to add the optimistic locking {@code SET}/{@code WHERE} clauses
     * @return the {@code UPDATE} statement
     */
    default String updateSql(
        TableMetadata table, List<String> setColumnNames, String pkColumnName,
        String concurrencyColumnName, boolean versionChecked
    ) {
        var setClauses = new ArrayList<>(setColumnNames.stream()
            .map(c -> quoteIdentifier(c) + " = ?")
            .toList());
        if (versionChecked) {
            setClauses.add(quoteIdentifier(concurrencyColumnName) + " = ?");
        }
        var sql = new StringBuilder("UPDATE ").append(quotedTableName(table))
            .append(" SET ").append(String.join(", ", setClauses))
            .append(" WHERE ").append(quoteIdentifier(pkColumnName)).append(" = ?");
        if (versionChecked) {
            sql.append(" AND ").append(quoteIdentifier(concurrencyColumnName)).append(" = ?");
        }
        return sql.toString();
    }

    /**
     * Builds the {@code DELETE} statement for the row identified by {@code pkColumnName}, optionally also
     * requiring {@code concurrencyColumnName} to still hold its previous value (optimistic locking). The
     * {@code ?} placeholders appear, in order: one for the primary key value, then (if {@code versionChecked})
     * one for the previous concurrency version - a caller must bind parameters in that exact order.
     *
     * @param table                 the table to delete from
     * @param pkColumnName          the physical name of the primary key column identifying the row
     * @param concurrencyColumnName the physical name of the optimistic locking column, or {@code null} if
     *                              {@code versionChecked} is {@code false}
     * @param versionChecked        whether to add the optimistic locking {@code WHERE} clause
     * @return the {@code DELETE} statement
     */
    default String deleteSql(
        TableMetadata table, String pkColumnName, String concurrencyColumnName, boolean versionChecked
    ) {
        var sql = new StringBuilder("DELETE FROM ").append(quotedTableName(table))
            .append(" WHERE ").append(quoteIdentifier(pkColumnName)).append(" = ?");
        if (versionChecked) {
            sql.append(" AND ").append(quoteIdentifier(concurrencyColumnName)).append(" = ?");
        }
        return sql.toString();
    }

    /**
     * Builds the {@code SELECT} statement fetching every column of {@code table} for the rows matching
     * {@code columnName = ?} - the generated-SQL side of {@code JdbcRecordMapper.selectByColumn}/{@code
     * selectOneByColumn}, used by {@code JdbcAggregateFetcher} to resolve foreign keys and by {@code
     * JdbcRecordProvider}-based custom finders.
     *
     * @param table      the table to select from
     * @param columnName the physical column to filter on
     * @return the {@code SELECT} statement, with one {@code ?} placeholder for the filter value
     */
    default String selectByColumnSql(TableMetadata table, String columnName) {
        return "SELECT * FROM " + quotedTableName(table) + " WHERE " + quoteIdentifier(columnName) + " = ?";
    }

    /**
     * Builds a paged {@code SELECT} statement fetching every column of {@code table}, ordered by {@code
     * orderByColumnName}, restricted to one page of {@code pageSize} rows starting at {@code offset}.
     * <p>
     * The default implementation uses the {@code LIMIT ? OFFSET ?} clause H2/PostgreSQL/MySQL all understand
     * (in that bind order: page size, then offset). SQL Server and Oracle understand neither keyword and
     * need the ANSI {@code OFFSET ? ROWS FETCH NEXT ? ROWS ONLY} clause instead - bound in the opposite
     * order, offset then page size - which is why the SQL and its bind parameters are returned together as
     * one {@link PagedSelect}, rather than as a plain SQL string a caller might bind in the wrong order.
     *
     * @param table             the table to select from
     * @param orderByColumnName the physical column to order by
     * @param offset            the number of rows to skip
     * @param pageSize          the maximum number of rows to return
     * @return the paged {@code SELECT} statement, together with its bind parameters in the correct order
     */
    default PagedSelect pagedSelectSql(TableMetadata table, String orderByColumnName, int offset, int pageSize) {
        var sql = "SELECT * FROM " + quotedTableName(table)
            + " ORDER BY " + quoteIdentifier(orderByColumnName) + " LIMIT ? OFFSET ?";
        return new PagedSelect(sql, new Object[]{pageSize, offset});
    }

    /**
     * A paged {@code SELECT} statement built by {@link #pagedSelectSql}, together with its bind parameters in
     * the order its placeholders expect - dialects order/paginate differently enough (see {@link
     * #pagedSelectSql}) that the two must always travel together.
     *
     * @param sql    the paged {@code SELECT} statement
     * @param params the values to bind to {@code sql}'s placeholders, in order
     */
    record PagedSelect(String sql, Object[] params) {
    }
}

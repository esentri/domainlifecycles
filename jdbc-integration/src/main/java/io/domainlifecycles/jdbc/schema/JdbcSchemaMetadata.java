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

package io.domainlifecycles.jdbc.schema;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * An immutable, in-memory snapshot of a database schema, read once via {@link DatabaseMetaData}.
 * <p>
 * This is the plain JDBC replacement for the table/column/key metadata that jOOQ's code generator bakes into
 * generated {@code UpdatableRecord} classes at build time: instead of generated code, the schema is
 * introspected at runtime and cached in this snapshot. All other components of this module (record property
 * discovery, foreign key resolution for fetching, SQL generation for persisting) are built on top of it.
 * <p>
 * Only single-column primary keys and single-column foreign keys are supported, matching the restriction
 * already imposed by the jOOQ based persistence integration this module mirrors.
 *
 * @author Mario Herb
 */
public final class JdbcSchemaMetadata {

    private final Map<String, TableMetadata> tablesByName;

    private JdbcSchemaMetadata(Map<String, TableMetadata> tablesByName) {
        this.tablesByName = tablesByName;
    }

    /**
     * Reads the schema metadata for all tables visible through the given connection.
     *
     * @param connection the JDBC connection to read metadata from
     * @return the schema metadata snapshot
     */
    public static JdbcSchemaMetadata read(Connection connection) {
        return read(connection, null, null);
    }

    /**
     * Reads the schema metadata for all tables of the given schema, visible through the given connection.
     *
     * @param connection   the JDBC connection to read metadata from
     * @param schemaPattern the schema to read tables from, or {@code null} to read tables from all schemas
     *                      visible through the connection
     * @return the schema metadata snapshot
     */
    public static JdbcSchemaMetadata read(Connection connection, String schemaPattern) {
        return read(connection, null, schemaPattern);
    }

    /**
     * Reads the schema metadata for all tables matching the given catalog and schema, visible through the
     * given connection.
     *
     * @param connection    the JDBC connection to read metadata from
     * @param catalog       the catalog to read tables from, or {@code null} to not narrow the search by catalog
     * @param schemaPattern the schema to read tables from, or {@code null} to not narrow the search by schema
     * @return the schema metadata snapshot
     */
    public static JdbcSchemaMetadata read(Connection connection, String catalog, String schemaPattern) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            Map<String, TableMetadata> tables = new LinkedHashMap<>();
            record TableIdentifier(String schema, String name) {
            }
            List<TableIdentifier> tableIdentifiers = new ArrayList<>();
            try (ResultSet rs = databaseMetaData.getTables(catalog, schemaPattern, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tableIdentifiers.add(new TableIdentifier(rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME")));
                }
            }
            for (TableIdentifier tableIdentifier : tableIdentifiers) {
                tables.put(tableIdentifier.name(), readTable(
                    databaseMetaData, catalog, schemaPattern, tableIdentifier.schema(), tableIdentifier.name()));
            }
            return new JdbcSchemaMetadata(tables);
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Failed to read database schema metadata.", e);
        }
    }

    private static TableMetadata readTable(
        DatabaseMetaData databaseMetaData,
        String catalog,
        String schemaPattern,
        String tableSchema,
        String tableName
    ) throws SQLException {
        String primaryKeyName = readPrimaryKeyColumn(databaseMetaData, catalog, schemaPattern, tableName);
        List<ForeignKeyMetadata> foreignKeys = readForeignKeys(databaseMetaData, catalog, schemaPattern, tableName);
        List<ColumnMetadata> columns = readColumns(
            databaseMetaData, catalog, schemaPattern, tableName, primaryKeyName);
        return new TableMetadata(tableSchema, tableName, columns, primaryKeyName, foreignKeys);
    }

    private static String readPrimaryKeyColumn(
        DatabaseMetaData databaseMetaData,
        String catalog,
        String schemaPattern,
        String tableName
    ) throws SQLException {
        List<String> primaryKeyColumns = new ArrayList<>();
        try (ResultSet rs = databaseMetaData.getPrimaryKeys(catalog, schemaPattern, tableName)) {
            while (rs.next()) {
                primaryKeyColumns.add(rs.getString("COLUMN_NAME"));
            }
        }
        if (primaryKeyColumns.size() > 1) {
            throw DLCPersistenceException.fail(
                "Only single fielded primary keys are supported! Contradicting table='%s' with primary key " +
                    "columns %s.", tableName, primaryKeyColumns);
        }
        return primaryKeyColumns.isEmpty() ? null : primaryKeyColumns.get(0);
    }

    private static List<ForeignKeyMetadata> readForeignKeys(
        DatabaseMetaData databaseMetaData,
        String catalog,
        String schemaPattern,
        String tableName
    ) throws SQLException {
        List<ForeignKeyMetadata> foreignKeys = new ArrayList<>();
        Map<String, Integer> columnCountPerForeignKey = new LinkedHashMap<>();
        try (ResultSet rs = databaseMetaData.getImportedKeys(catalog, schemaPattern, tableName)) {
            while (rs.next()) {
                String foreignKeyName = rs.getString("FK_NAME");
                String foreignKeyColumn = rs.getString("FKCOLUMN_NAME");
                String referencedTable = rs.getString("PKTABLE_NAME");
                String referencedColumn = rs.getString("PKCOLUMN_NAME");
                String key = foreignKeyName != null ? foreignKeyName : foreignKeyColumn;
                columnCountPerForeignKey.merge(key, 1, Integer::sum);
                foreignKeys.add(new ForeignKeyMetadata(foreignKeyColumn, referencedTable, referencedColumn));
            }
        }
        boolean hasCompositeForeignKey = columnCountPerForeignKey.values().stream().anyMatch(count -> count > 1);
        if (hasCompositeForeignKey) {
            throw DLCPersistenceException.fail(
                "Only single fielded foreign keys are supported! Contradicting table='%s'.", tableName);
        }
        return foreignKeys;
    }

    private static List<ColumnMetadata> readColumns(
        DatabaseMetaData databaseMetaData,
        String catalog,
        String schemaPattern,
        String tableName,
        String primaryKeyName
    ) throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        String databaseProductName = databaseMetaData.getDatabaseProductName();
        try (ResultSet rs = databaseMetaData.getColumns(catalog, schemaPattern, tableName, "%")) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                int sqlType = rs.getInt("DATA_TYPE");
                String typeName = rs.getString("TYPE_NAME");
                int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                int precision = rs.getInt("COLUMN_SIZE");
                boolean nullable = rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                boolean primaryKey = columnName.equalsIgnoreCase(primaryKeyName);
                Class<?> javaType = JdbcSqlTypeMapping.javaType(
                    sqlType, typeName, decimalDigits, precision, databaseProductName);
                columns.add(new ColumnMetadata(columnName, sqlType, typeName, javaType, precision, nullable, primaryKey));
            }
        }
        return List.copyOf(columns);
    }

    /**
     * Returns the physical names of all tables contained in this schema snapshot.
     *
     * @return the table names
     */
    public Set<String> tableNames() {
        return Set.copyOf(tablesByName.keySet());
    }

    /**
     * Returns the metadata for the table with the given name.
     *
     * @param tableName the physical table name (matched case-insensitively)
     * @return the table metadata
     * @throws DLCPersistenceException if no table with the given name exists in this schema snapshot
     */
    public TableMetadata table(String tableName) {
        return findTable(tableName)
            .orElseThrow(() -> DLCPersistenceException.fail(
                "No table metadata found for '%s'. Available tables: %s", tableName, tablesByName.keySet()));
    }

    /**
     * Returns the metadata for the table with the given name, if it exists in this schema snapshot.
     *
     * @param tableName the physical table name (matched case-insensitively)
     * @return the table metadata, or empty if no such table exists
     */
    public Optional<TableMetadata> findTable(String tableName) {
        TableMetadata direct = tablesByName.get(tableName);
        if (direct != null) {
            return Optional.of(direct);
        }
        return tablesByName.entrySet()
            .stream()
            .filter(e -> e.getKey().equalsIgnoreCase(tableName))
            .map(Map.Entry::getValue)
            .findFirst();
    }
}

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

import java.util.List;
import java.util.Optional;

/**
 * Metadata of a single database table (or view), as discovered via {@link java.sql.DatabaseMetaData}.
 *
 * @param schema         the schema the table lives in, exactly as reported by the database, or {@code null}
 *                        if the database reported none (e.g. a table in H2's default, unqualified schema)
 * @param name           the physical table name, exactly as reported by the database
 * @param columns        the table's columns, in the order reported by the database
 * @param primaryKeyName the physical name of the single-column primary key of this table, or {@code null} if
 *                        the table has no primary key
 * @param foreignKeys    the table's single-column foreign key constraints
 * @author Mario Herb
 */
public record TableMetadata(
    String schema,
    String name,
    List<ColumnMetadata> columns,
    String primaryKeyName,
    List<ForeignKeyMetadata> foreignKeys
) {

    /**
     * Returns the name to use when referring to this table in generated SQL: schema-qualified if a schema
     * was reported for it, the bare table name otherwise.
     *
     * @return the (possibly schema-qualified) SQL reference for this table
     */
    public String qualifiedName() {
        return (schema == null || schema.isBlank()) ? name : schema + "." + name;
    }

    /**
     * Returns the column with the given physical name.
     *
     * @param columnName the physical column name
     * @return the matching column metadata
     * @throws DLCPersistenceException if no column with the given name exists on this table
     */
    public ColumnMetadata column(String columnName) {
        return findColumn(columnName)
            .orElseThrow(() -> DLCPersistenceException.fail(
                "Table '%s' has no column named '%s'.", name, columnName));
    }

    /**
     * Returns the column with the given physical name, if it exists on this table.
     *
     * @param columnName the physical column name (matched case-insensitively)
     * @return the matching column metadata, or empty if no such column exists
     */
    public Optional<ColumnMetadata> findColumn(String columnName) {
        return columns.stream()
            .filter(c -> c.name().equalsIgnoreCase(columnName))
            .findFirst();
    }

    /**
     * Returns the foreign key constraint declared on the given column, if any.
     *
     * @param columnName the physical column name
     * @return the matching foreign key metadata, or empty if the column is not a foreign key
     */
    public Optional<ForeignKeyMetadata> foreignKey(String columnName) {
        return foreignKeys.stream()
            .filter(fk -> fk.columnName().equalsIgnoreCase(columnName))
            .findFirst();
    }
}

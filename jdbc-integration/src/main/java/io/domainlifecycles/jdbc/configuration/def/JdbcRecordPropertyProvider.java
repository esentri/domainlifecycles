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

package io.domainlifecycles.jdbc.configuration.def;

import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.util.NamingUtil;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.records.RecordProperty;
import io.domainlifecycles.persistence.records.RecordPropertyProvider;

import java.util.List;
import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link RecordPropertyProvider}, backed by a {@link JdbcSchemaMetadata}
 * snapshot instead of jOOQ's generated, per-table {@code UpdatableRecord} classes.
 * <p>
 * Record property names follow the same "SNAKE_CASE column" -&gt; "camelCase property" convention as the
 * jOOQ based integration (see {@link NamingUtil}), so that {@link
 * io.domainlifecycles.persistence.mapping.RecordPropertyMatcher} implementations can be reused unchanged.
 *
 * @author Mario Herb
 */
public class JdbcRecordPropertyProvider implements RecordPropertyProvider {

    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcRecordPropertyProvider}.
     *
     * @param schemaMetadata the schema metadata snapshot to read table/column information from
     */
    public JdbcRecordPropertyProvider(JdbcSchemaMetadata schemaMetadata) {
        this.schemaMetadata = Objects.requireNonNull(schemaMetadata);
    }

    /**
     * {@inheritDoc}
     *
     * @param tableName the physical table name
     */
    @Override
    public List<RecordProperty> provideProperties(String tableName) {
        var table = schemaMetadata.table(tableName);
        if (table.primaryKeyName() == null) {
            throw DLCPersistenceException.fail(
                "Only tables with a single-column primary key are supported! Table '%s' has no primary key " +
                    "defined.", tableName);
        }
        String primaryKeyPropertyName = NamingUtil.snakeCaseToCamelCase(table.primaryKeyName().toLowerCase());

        return table.columns().stream()
            .map(column -> {
                String propertyName = NamingUtil.snakeCaseToCamelCase(column.name().toLowerCase());
                boolean nonNullForeignKey = table.foreignKey(column.name()).isPresent() && !column.nullable();
                return new RecordProperty(
                    propertyName,
                    tableName,
                    column.javaType(),
                    propertyName.equals(primaryKeyPropertyName),
                    nonNullForeignKey);
            })
            .toList();
    }
}

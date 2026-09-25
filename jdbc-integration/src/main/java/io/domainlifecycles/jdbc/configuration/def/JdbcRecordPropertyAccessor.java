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

import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.records.RecordProperty;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;

import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link RecordPropertyAccessor}. Where the jOOQ based implementation
 * reflectively invokes a generated getter/setter method on a concrete {@code UpdatableRecord} subclass, this
 * implementation resolves the property's physical column name via {@link JdbcSchemaMetadata} and reads or
 * writes it directly on the {@link JdbcRecord}'s column value map.
 *
 * @author Mario Herb
 */
public class JdbcRecordPropertyAccessor implements RecordPropertyAccessor<JdbcRecord> {

    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcRecordPropertyAccessor}.
     *
     * @param schemaMetadata the schema metadata snapshot used to resolve a property's physical column name
     */
    public JdbcRecordPropertyAccessor(JdbcSchemaMetadata schemaMetadata) {
        this.schemaMetadata = Objects.requireNonNull(schemaMetadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(RecordProperty property, JdbcRecord record, Object value) {
        record.set(columnName(property), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(RecordProperty property, JdbcRecord record) {
        return record.get(columnName(property));
    }

    /**
     * Resolves the physical column matching the given property by the same normalized (lower-cased,
     * underscore-stripped) comparison {@link io.domainlifecycles.jdbc.imp.matcher.JdbcRecordPropertyMatcher}
     * already uses for discovery, rather than by reconstructing an assumed exact snake_case column name -
     * this module's own migration schema is not itself consistent about whether an embedded digit gets its
     * own underscore (compare {@code test_entity_2_id} against {@code my_vo_value2}), so no single
     * camelCase-to-snake_case reconstruction could match both.
     */
    private String columnName(RecordProperty property) {
        var table = schemaMetadata.table(property.getRecordClassName());
        var normalizedPropertyName = normalize(property.getName());
        return table.columns().stream()
            .filter(c -> normalize(c.name()).equals(normalizedPropertyName))
            .findFirst()
            .map(io.domainlifecycles.jdbc.schema.ColumnMetadata::name)
            .orElseThrow(() -> DLCPersistenceException.fail(
                "Table '%s' has no column matching property '%s'.", table.name(), property.getName()));
    }

    private static String normalize(String name) {
        return name.toLowerCase().replace("_", "");
    }
}

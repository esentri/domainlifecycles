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

package io.domainlifecycles.jdbc.imp.matcher;

import io.domainlifecycles.jdbc.configuration.JdbcValueObjectColumnNameOverride;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.persistence.mapping.RecordPropertyMatcher;
import io.domainlifecycles.persistence.records.RecordProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link RecordPropertyMatcher}. The matching rules are identical to
 * the jOOQ based integration's: both fold record property names and field names to lower case and strip
 * underscores before comparing, so this behaves the same whether the record property name originates from a
 * generated jOOQ getter or, as here, from a column name translated to camelCase (see
 * {@link io.domainlifecycles.jdbc.util.NamingUtil}).
 *
 * @author Mario Herb
 */
public class JdbcRecordPropertyMatcher implements RecordPropertyMatcher {

    private final List<JdbcValueObjectColumnNameOverride> columnNameOverrides;

    /**
     * Constructs a new instance of {@code JdbcRecordPropertyMatcher} using the naming convention for every
     * embedded value object field, with no {@link JdbcValueObjectColumnNameOverride}s.
     */
    public JdbcRecordPropertyMatcher() {
        this(List.of());
    }

    /**
     * Constructs a new instance of {@code JdbcRecordPropertyMatcher}, consulting the given overrides in
     * {@link #matchValueObjectPath} in place of the naming convention for exactly the field paths they
     * register.
     *
     * @param columnNameOverrides the embedded value object field column name overrides to apply
     */
    public JdbcRecordPropertyMatcher(List<JdbcValueObjectColumnNameOverride> columnNameOverrides) {
        this.columnNameOverrides = Objects.requireNonNull(columnNameOverrides);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchProperty(RecordProperty recordProperty, FieldMirror fieldMirror) {
        return recordProperty.getName().toLowerCase().replaceAll("_", "")
            .equals(fieldMirror.getName().toLowerCase());
    }

    /**
     * {@inheritDoc}
     * <p>
     * A path matching a registered {@link JdbcValueObjectColumnNameOverride} - both its {@link
     * JdbcValueObjectColumnNameOverride#containingEntityType()} (compared against {@code
     * path.get(0).getDeclaredByTypeName()}, the fully-qualified name of the entity that declares the path's
     * first segment) and its {@link JdbcValueObjectColumnNameOverride#pathFromEntityToValueObjectField()}
     * (compared against every segment's name) must match - is compared against that override's {@link
     * JdbcValueObjectColumnNameOverride#columnName()} instead of the naming-convention derived name (still
     * folded to lower case with underscores stripped, exactly like every other comparison this class makes)
     * - see the class-level Javadoc there for why this escape hatch exists.
     */
    @Override
    public boolean matchValueObjectPath(RecordProperty recordProperty, List<FieldMirror> path) {
        var pathNames = path.stream().map(FieldMirror::getName).toList();
        var override = columnNameOverrides.stream()
            .filter(o -> !path.isEmpty()
                && o.containingEntityType().getName().equals(path.get(0).getDeclaredByTypeName())
                && Arrays.asList(o.pathFromEntityToValueObjectField()).equals(pathNames))
            .findFirst();
        if (override.isPresent()) {
            return recordProperty.getName().toLowerCase().replaceAll("_", "")
                .equals(override.get().columnName().toLowerCase().replaceAll("_", ""));
        }
        var pathName = new StringBuilder();
        path.forEach(e -> pathName.append(e.getName()));
        return recordProperty.getName()
            .toLowerCase()
            .replaceAll("_", "")
            .equals(pathName.toString().toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchForwardReference(RecordProperty recordProperty, EntityReferenceMirror entityReferenceMirror) {
        return recordProperty.getName().substring(0, recordProperty.getName().length() - 2).toLowerCase().replaceAll(
                "_", "")
            .equals(entityReferenceMirror.getName().toLowerCase());
    }
}

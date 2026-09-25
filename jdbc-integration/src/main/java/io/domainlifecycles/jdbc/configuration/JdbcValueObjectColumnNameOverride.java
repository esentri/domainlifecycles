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

package io.domainlifecycles.jdbc.configuration;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jdbc.imp.matcher.JdbcRecordPropertyMatcher;

/**
 * Overrides the physical column name this module's naming-convention based auto-mapping would otherwise
 * derive for a single, embedded (non-collection) value object field nested within an entity's own record -
 * the scalar-column analogue of {@link JdbcEntityValueObjectRecordTypeConfiguration}, which instead overrides
 * the physical *table* a {@code List<ValueObject>} field (or a single value object mapped to its own
 * dedicated table) is persisted in.
 * <p>
 * By default, an embedded value object's leaf field is expected in a column named by concatenating every
 * field name from the entity down to the leaf (e.g. {@code mandatoryComplexValueObject.
 * mandatorySimpleValueObject.value} &#8594; {@code MANDATORY_COMPLEX_VALUE_OBJECT_MANDATORY_SIMPLE_VALUE_OBJECT_VALUE},
 * see {@link JdbcRecordPropertyMatcher#matchValueObjectPath}). Unlike a VO-list's own table, an embedded
 * field has no separate physical name to configure - it is just one column among its owner's others - so a
 * deeply nested field path can easily exceed a dialect's identifier length limit (e.g. Postgres' 63-byte
 * {@code NAMEDATALEN}, MySQL's 64-character limit) with no way to shorten it. This configuration is the
 * escape hatch for exactly that case: register one instance per overridden field path, and pass the
 * resulting list to a {@link JdbcRecordPropertyMatcher}, which consults it in place of the naming convention
 * for exactly the registered path (falling through to the naming convention for every other field).
 *
 * @param containingEntityType            the entity type the value object field is (nested) within
 * @param columnName                      the physical column name to match instead of the naming-convention
 *                                         derived one
 * @param pathFromEntityToValueObjectField the field path from the entity down to the leaf scalar field, e.g.
 *                                         {@code "mandatoryComplexValueObject", "mandatorySimpleValueObject",
 *                                         "value"}
 * @author Mario Herb
 */
public record JdbcValueObjectColumnNameOverride(
    Class<? extends Entity<?>> containingEntityType,
    String columnName,
    String... pathFromEntityToValueObjectField) {
}

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
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jdbc.records.JdbcRecord;

/**
 * Configures an explicit table for a value object (or a {@code List<ValueObject>}/{@code List<Identity>}/
 * {@code List<Enum>} field's child records) that this module's naming-convention based auto-mapping cannot
 * resolve on its own - either because the table name doesn't follow the convention, or because the value
 * object is a single, non-collection field that should still be persisted in its own dedicated table rather
 * than embedded inline into its owner's record.
 * <p>
 * This is the plain JDBC analogue of the jOOQ based integration's {@code EntityValueObjectRecordTypeConfiguration}:
 * the difference is a physical table name ({@link String}) here where that one carries a generated record
 * {@code Class}, since every table in this module is represented by the same {@link JdbcRecord} class.
 *
 * @param containingEntityType        the containing entity type
 * @param containedValueObjectType    the contained value object type
 * @param tableName                   the physical name of the table the value object (or child records) are
 *                                    persisted in
 * @param pathFromEntityToValueObject the path from the entity to the value object
 * @author Mario Herb
 */
public record JdbcEntityValueObjectRecordTypeConfiguration(
    Class<? extends Entity<?>> containingEntityType,
    Class<? extends ValueObject> containedValueObjectType,
    String tableName,
    String... pathFromEntityToValueObject) {
}

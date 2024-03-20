/*
 *
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
 *  Copyright 2019-2024 the original author or authors.
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

package nitrox.dlc.persistence.records;


import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.ValueObject;

/**
 * Represents a entity value object record type configuration.
 *
 * @param <BASE_RECORD_TYPE>          the base record type
 * @param containingEntityType        the containing entity type
 * @param containedValueObjectType    the contained value object type
 * @param valueObjectRecordType       the value object record type
 * @param pathFromEntityToValueObject the path from the entity to the value object
 *
 * @author Mario Herb
 */
public record EntityValueObjectRecordTypeConfiguration<BASE_RECORD_TYPE>(
        Class<? extends Entity<?>> containingEntityType,
        Class<? extends ValueObject> containedValueObjectType,
        Class<? extends BASE_RECORD_TYPE> valueObjectRecordType,
        String... pathFromEntityToValueObject) {
}

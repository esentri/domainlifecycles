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

package nitrox.dlc.persistence.mapping;

import nitrox.dlc.domain.types.ValueObject;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.persistence.records.RecordProperty;

/**
 * Provides access to deeply nested {@link ValueObject} instances in the auto mapping process.
 *
 * @param <R>  the record type
 * @param <DO> the domain object type
 *
 * @author Mario Herb
 */
public interface MapperNestedValueObjectAccessor<R, DO extends DomainObject> {

    /**
     * Gets the mapped value object.
     *
     * @param record                        the record
     * @param valueObjectFieldName the name of the field that should hold the value object
     * @return the mapped value object
     */
    ValueObject getMappedValueObject(R record, String valueObjectFieldName);

    /**
     * Gets the mapped record property value.
     *
     * @param rp           the record property
     * @param domainObject the domain object
     * @return the mapped record property value
     */
    Object getMappedRecordPropertyValue(RecordProperty rp, DO domainObject);

}

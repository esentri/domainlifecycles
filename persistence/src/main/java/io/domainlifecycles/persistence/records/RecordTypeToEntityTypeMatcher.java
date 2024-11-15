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

package io.domainlifecycles.persistence.records;

import java.util.Optional;
import java.util.Set;

/**
 * Matches a record type to an entity type.
 *
 * @param <RECORD_TYPE> the record type
 * @author Mario Herb
 */
public interface RecordTypeToEntityTypeMatcher<RECORD_TYPE> {

    /**
     * Finds the matching record type for an entity.
     *
     * @param availableRecordTypes the available record types
     * @param entityTypeName       the full qualified entity type name
     * @return the matching record type
     */
    Optional<Class<? extends RECORD_TYPE>> findMatchingRecordType(Set<Class<? extends RECORD_TYPE>> availableRecordTypes, String entityTypeName);
}

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

package nitrox.dlc.persistence.mirror.api;

import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.persistence.mapping.RecordMapper;

import java.util.List;

/**
 * Record Mirror interface
 *
 * @author Mario Herb
 */
public interface RecordMirror<BASE_RECORD_TYPE> {

    /**
     * Returns the full qualified type name of the record type.
     */
    String recordTypeName();

    /**
     * Returns the full qualified type name of the domain object type {@link DomainObject}.
     */
    String domainObjectTypeName();

    /**
     * Returns the {@link DomainObject} type names for records that this record references directly
     * and where the reference is enforced (e.g. via Foreign Key Constraints of this record).
     */
    List<String> enforcedReferences();

    /**
     * Returns the record mapper instance to be able to map a record to ist corresponding {@link DomainObject}
     * or the {@link DomainObject} to a record
     */
    RecordMapper<BASE_RECORD_TYPE, ? extends DomainObject, ? extends AggregateRoot<?>> recordMapper();

}

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

package io.domainlifecycles.persistence.mirror.api;

import java.util.List;


/**
 * This interfaces enables to
 * - map value objects to record
 * - to provide information to be used in a fetcher implementation to fetch records form a database
 * - to provide information to be used in a repository implementation to apply insert, update and delete
 * operations for a given value object
 *
 * @param <BASE_RECORD_TYPE> the type of record
 * @author Mario Herb
 */
public interface ValueObjectRecordMirror<BASE_RECORD_TYPE> extends RecordMirror<BASE_RECORD_TYPE> {

    /**
     * Returns the full qualified type name of the entity which
     * contains this value object
     *
     * @return the containing entity type name
     */
    String containingEntityTypeName();

    /**
     * Returns the path of fields which must be accessed in the given order
     * to reach the value object represented by the given record.
     *
     * @return the path segments
     */
    List<String> pathSegments();

    /**
     * Returns the complete path of fields (separated by ".") which must be accessed in the
     * given order to reach the value object represented by the given record.
     *
     * @return the complete path of fields
     */
    String completePath();

    /**
     * Returns the full qualified name of the container class for this value object which is also record
     * mapped. It might be an entity or another record mapped value object
     * class
     *
     * @return the container class for this record
     */
    String recordMappedContainerTypeName();

    /**
     * Returns owner {@link EntityRecordMirror}
     *
     * @return owner
     */
    EntityRecordMirror<BASE_RECORD_TYPE> owner();

    /**
     * Sets the owner {@link EntityRecordMirror}.
     *
     * @param owner the owner to be set
     */
    void setOwner(EntityRecordMirror<BASE_RECORD_TYPE> owner);


}

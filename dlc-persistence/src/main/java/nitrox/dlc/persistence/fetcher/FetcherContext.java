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

package nitrox.dlc.persistence.fetcher;

import nitrox.dlc.domain.types.internal.DomainObject;

import java.util.Optional;

/**
 * FetcherContext is the base interface for all fetcher contexts.
 *
 * @param <BASE_RECORD_TYPE> the base record type
 *
 * @author Mario Herb
 */
public interface FetcherContext<BASE_RECORD_TYPE> {

    /**
     * Whether the given record has already been fetched.
     *
     * @param record the record to check
     * @return true if the record has already been fetched, false otherwise
     */
    boolean isFetched(BASE_RECORD_TYPE record);

    /**
     * Returns the domain object for the given record.
     *
     * @param record the record to get the domain object for
     * @return the domain object for the given record
     */
    Optional<DomainObject> getDomainObjectFor(BASE_RECORD_TYPE record);

    /**
     * Returns the record for the given domain object.
     *
     * @param p the domain object to get the record for
     * @return the record for the given domain object
     */
    Optional<BASE_RECORD_TYPE> getRecordFor(DomainObject p);
}

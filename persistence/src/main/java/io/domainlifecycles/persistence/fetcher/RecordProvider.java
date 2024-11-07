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

package io.domainlifecycles.persistence.fetcher;

import java.util.Collection;

/**
 * A RecordProvider allows us to provide records for properties in an
 * AggregateFetcher by using custom user defined queries.
 * The query can be issued outside the fetcher,
 * then the resulting records are provided to the fetcher by a record provider.
 * <p>
 * A "child" record or a collection of "child" records is to be returned. To be
 * able to define a query the parent record is given. The parent record's values
 * could be used as query parameters.
 *
 * @param <CHILD_RECORD_TYPE>
 * @param <PARENT_RECORD_TYPE>
 * @author Mario Herb
 */
public interface RecordProvider<CHILD_RECORD_TYPE, PARENT_RECORD_TYPE> {

    /**
     * Implement this method to provide a single child record for.
     * <p>
     * Instead of issuing a query to fetch record, a fetcher using this record provider
     * will use this record.
     *
     * @param parentRecord The parent record
     * @return The "child" record for the parent record
     */
    default CHILD_RECORD_TYPE provide(PARENT_RECORD_TYPE parentRecord) {
        throw new UnsupportedOperationException(
            "This EntityRecordProvider should deliver a single record for its assigned property. It's currently not " +
                "implemented.");
    }

    /**
     * Implement this method to provide a collection of child records
     * <p>
     * Instead of issuing a query to fetch a collection of records, a fetcher using this record provider
     * will use this collection of records.
     *
     * @param parentRecord The parent record
     * @return The collection of "child" records for the parent record
     */
    default Collection<CHILD_RECORD_TYPE> provideCollection(PARENT_RECORD_TYPE parentRecord) {
        throw new UnsupportedOperationException(
            "This EntityRecordProvider should deliver a collection of records for its assigned property. It's " +
                "currently not implemented.");
    }

}

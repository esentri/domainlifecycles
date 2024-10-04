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

package io.domainlifecycles.persistence.fetcher.simple;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.fetcher.FetcherContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * SimpleFetcherContext is a simple implementation of a fetcher context.
 *
 * @param <BASE_RECORD_TYPE> the base record type
 * @author Mario Herb
 */
public class SimpleFetcherContext<BASE_RECORD_TYPE> implements FetcherContext<BASE_RECORD_TYPE> {

    private final Set<FetchedRecord<BASE_RECORD_TYPE>> fetchedRecordSet = new HashSet<>();

    private final Map<DomainObject, BASE_RECORD_TYPE> fetchedRecordMap = new IdentityHashMap<>();

    private final Map<FetchedRecord<BASE_RECORD_TYPE>, DomainObject> recordToDomainObjectMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BASE_RECORD_TYPE> getRecordFor(DomainObject p) {
        if (p == null) {
            return Optional.empty();
        }
        return Optional.of(fetchedRecordMap.get(p));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFetched(BASE_RECORD_TYPE record) {
        return fetchedRecordSet.contains(FetchedRecord.of(record));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DomainObject> getDomainObjectFor(BASE_RECORD_TYPE record) {
        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(recordToDomainObjectMap.get(FetchedRecord.of(record)));
    }

    /**
     * Assigns the given record to the given domain object.
     *
     * @param p      the domain object
     * @param record the record
     */
    public void assignRecordToDomainObject(DomainObject p, BASE_RECORD_TYPE record) {
        if (p == null) return;
        fetchedRecordMap.put(p, record);
        recordToDomainObjectMap.put(FetchedRecord.of(record), p);
    }

    /**
     * Marks the given record as fetched.
     *
     * @param record the record to record as fetched
     */
    public void recordFetched(BASE_RECORD_TYPE record) {
        fetchedRecordSet.add(FetchedRecord.of(record));
    }


}

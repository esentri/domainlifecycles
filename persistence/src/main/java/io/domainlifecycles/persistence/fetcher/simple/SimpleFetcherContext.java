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

package io.domainlifecycles.persistence.fetcher.simple;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.fetcher.FetcherContext;
import io.domainlifecycles.persistence.mapping.ScalarListElement;

import java.util.ArrayDeque;
import java.util.Deque;
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

    /**
     * A {@link ScalarListElement} wraps a single {@code List<Identity>}/{@code List<Enum>} element and is
     * re-created (a new instance, equal by value) every time it is re-derived from an already-fetched
     * entity's plain field (e.g. to build the "database state" access model for diffing) - it can therefore
     * never be found again in {@link #fetchedRecordMap}, which relies on reference identity. Records for
     * such elements are tracked here instead, keyed by value equality plus a caller-provided "scope" (in
     * practice the owning {@code ValueObjectRecordMirror}), since the wrapped value alone (e.g. the enum
     * constant {@code TWO}) does not distinguish "this aggregate root's own enum list" from "this
     * aggregate's child entity's enum list" - both may legitimately contain an equal element at the same
     * time. A {@link Deque} per key additionally keeps multiple records for duplicate-valued elements within
     * the very same list (e.g. the same enum constant twice) distinct, so each lookup consumes exactly one
     * matching record.
     */
    private final Map<ScalarListKey, Deque<BASE_RECORD_TYPE>> scalarListElementRecords = new HashMap<>();

    private final Map<FetchedRecord<BASE_RECORD_TYPE>, DomainObject> recordToDomainObjectMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BASE_RECORD_TYPE> getRecordFor(DomainObject p) {
        return getRecordFor(p, null);
    }

    /**
     * Like {@link #getRecordFor(DomainObject)}, but for a {@link ScalarListElement} additionally scoped by
     * the given {@code scope} (in practice the owning {@code ValueObjectRecordMirror}), to distinguish
     * equal-valued elements belonging to different lists. The {@code scope} is ignored for any other kind
     * of domain object.
     *
     * @param p     the domain object to get the record for
     * @param scope disambiguates equal-valued {@link ScalarListElement}s belonging to different lists
     * @return the record for the given domain object
     */
    public Optional<BASE_RECORD_TYPE> getRecordFor(DomainObject p, Object scope) {
        if (p == null) {
            return Optional.empty();
        }
        if (p instanceof ScalarListElement<?> scalarListElement) {
            var records = scalarListElementRecords.get(new ScalarListKey(scope, scalarListElement));
            return Optional.ofNullable(records == null ? null : records.poll());
        }
        return Optional.ofNullable(fetchedRecordMap.get(p));
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
        assignRecordToDomainObject(p, record, null);
    }

    /**
     * Like {@link #assignRecordToDomainObject(DomainObject, Object)}, but for a {@link ScalarListElement}
     * additionally scoped by the given {@code scope} (in practice the owning {@code
     * ValueObjectRecordMirror}) - see {@link #getRecordFor(DomainObject, Object)}. The {@code scope} is
     * ignored for any other kind of domain object.
     *
     * @param p      the domain object
     * @param record the record
     * @param scope  disambiguates equal-valued {@link ScalarListElement}s belonging to different lists
     */
    public void assignRecordToDomainObject(DomainObject p, BASE_RECORD_TYPE record, Object scope) {
        if (p == null) return;
        if (p instanceof ScalarListElement<?> scalarListElement) {
            scalarListElementRecords.computeIfAbsent(new ScalarListKey(scope, scalarListElement),
                k -> new ArrayDeque<>()).add(record);
        } else {
            fetchedRecordMap.put(p, record);
        }
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

    private record ScalarListKey(Object scope, ScalarListElement<?> element) {
    }

}

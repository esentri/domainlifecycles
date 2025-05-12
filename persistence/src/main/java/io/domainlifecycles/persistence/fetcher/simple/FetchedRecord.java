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

import java.util.Objects;

/**
 * We need this class to manage records in the fetcher context, because some
 * record implementations (e.g. jooq) implement equals and hashcode only on
 * structure not on type level that can lead to unwanted collisions in sets and
 * hashmaps which are hold in the fetcher context
 *
 * @param <RECORD> the record type
 * @author Mario Herb
 */
public class FetchedRecord<RECORD> {

    /**
     * The `record` field represents the record instance associated with this object.
     * It serves as the primary data entity tied to the context or lifecycle operations
     * within the fetcher or persistence mechanism.
     */
    public final RECORD record;
    /**
     * Represents the name of the record type associated with a fetched record.
     * <p>
     * This field is used to distinguish between different types of records within
     * contexts that may handle multiple types of records. It helps avoid unwanted
     * collisions in collections such as sets or maps by identifying records at the
     * type level.
     */
    public final String recordTypeName;

    private FetchedRecord(RECORD record, String recordTypeName) {
        this.record = record;
        this.recordTypeName = recordTypeName;
    }

    /**
     * Creates a new FetchedRecord.
     *
     * @param record   the record
     * @param <RECORD> the record type
     * @return the fetched record
     */
    public static <RECORD> FetchedRecord<RECORD> of(RECORD record) {
        return new FetchedRecord<>(record, record.getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedRecord)) return false;
        FetchedRecord<RECORD> that = (FetchedRecord<RECORD>) o;
        return record.equals(that.record) && recordTypeName.equals(that.recordTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(record, recordTypeName);
    }
}

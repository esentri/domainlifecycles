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

package io.domainlifecycles.persistence.mapping;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.persistence.fetcher.AggregateFetcher;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.repository.persister.Persister;

/**
 * Abstract base class for all record mappers.
 *
 * @param <R>  record type
 * @param <DO> domain object type
 * @param <A>  aggregate root type
 * @author Mario Herb
 */
public abstract class AbstractRecordMapper<R, DO extends DomainObject, A extends AggregateRoot<?>> implements RecordMapper<R, DO, A> {

    /**
     * Mapping a record to a domain object. Here to build "complete" correct domain objects it is necessary to
     * map the record to the builder first, so the mapping ist applied before the domain object is built.
     *
     * @param record mapping source
     * @return domainObject new {@link DomainObject} instance as mapping result
     */
    public DO to(R record) {
        if (record == null) {
            return null;
        }
        var builder = recordToDomainObjectBuilder(record);
        return (DO) builder.build();
    }

    /**
     * We need this method in {@link Persister} or {@link AggregateFetcher}. We return a {@link DomainObjectBuilder}
     * instance,
     * to be able to build only complete instances, when all values are set,
     * to prevent NotNull assertions and other invariant checks form failing too early in the mapping process
     *
     * @param record mapping source
     * @return domain object builder new {@link DomainObjectBuilder} instance
     */
    public abstract DomainObjectBuilder<DO> recordToDomainObjectBuilder(R record);

    /**
     * Part of our general mapping interface to map a domain object instance into a record.
     * Each record mapper must implement this, depending on the domain object's structure.
     *
     * @param domainObject {@link DomainObject} mapping source
     * @param root         instance of {@link AggregateRoot} of the domain object
     * @return record mapping result
     */
    public abstract R from(DO domainObject, A root);

}

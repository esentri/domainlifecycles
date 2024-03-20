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

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.persistence.repository.persister.Persister;

/**
 * Base interface for all record mappers.
 *
 * @param <R>
 * @param <DO>
 * @param <A>
 */
public interface RecordMapper<R, DO extends DomainObject, A extends AggregateRoot<?>> {

    /**
     * This method is called from the {@link Persister} or {@link nitrox.dlc.persistence.fetcher.AggregateFetcher}. We return a {@link DomainObjectBuilder} instance,
     * to be able to build only complete instances, when all values are set,
     * to prevent NotNull assertions and other invariant checks from failing too early in the mapping process.
     *
     * @param record mapping source
     * @return domain object builder new {@link DomainObjectBuilder} instance
     */
    DomainObjectBuilder<DO> recordToDomainObjectBuilder(R record);

    /**
     * Part of the general mapping interface to map a domain object instance into a record.
     * Each record mapper must implement this, depending on the domain object's structure.
     *
     * @param domainObject    {@link DomainObject} mapping source
     * @param root instance of {@link AggregateRoot} of the domain object
     * @return record mapping result
     */
    R from(DO domainObject, A root);

    /**
     * Needed to assign custom mappers in the mapping process;
     */
    Class<DO> domainObjectType();
    /**
     * Needed to assign custom mappers in the mapping process;
     */
    Class<R> recordType();
}

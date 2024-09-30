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

package io.domainlifecycles.persistence.fetcher;


import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.internal.DomainObject;

import java.util.List;

/**
 * An aggregate fetcher fetches an aggregate consisting of several entities from
 * the database only by the ID of the aggregate root. In some cases this is
 * needed, because a fetcher implementation cannot always guarantee to be able
 * to derive a correct query to the underlying database. Or sometimes we want to
 * implement a more efficient query to be able to do some performance tuning.
 *
 * @param <A>      EntityClass of the aggregate's root entity
 * @param <I>      Identity Class of the root entity
 * @param <RECORD> the record type of the aggregate's root entity
 * @author Mario Herb
 */
public interface AggregateFetcher<A extends AggregateRoot<I>, I extends Identity<?>, RECORD> {

    /**
     * Define a custom {@link RecordProvider}, which provides a record or a
     * collection of records for one of the entity´s properties, which
     * contain a sub entity of the aggregate or a collection of sub entities or
     * a record mapped value object or a collection of record mapped value objects.
     * With this mechanism the results of user defined queries could be
     * defined when fetching the aggregate.
     *
     * @param recordProvider        - the {@link RecordProvider} instance to be registered within this
     * {@link AggregateFetcher}
     * @param containingEntityClass - the {@link Entity} containing the properties whose records are provided by the
     *                              registered {@link RecordProvider}
     * @param propertyClass         - the {@link DomainObject} class of the contained properties
     * @param propertyPath          - the path of property names containing the domain object instances, for which
     *                              the {@link RecordProvider}
     *                              should supply the records
     * @return this - to offer a fluent API
     */
    AggregateFetcher<A, I, RECORD> withRecordProvider(RecordProvider<? extends RECORD, ? extends RECORD> recordProvider,
                                                      Class<? extends Entity<?>> containingEntityClass,
                                                      Class<? extends DomainObject> propertyClass,
                                                      List<String> propertyPath);

    /**
     * Do a deep fetch and return a "complete" aggregate instance.
     *
     * @param id - the id of aggregate's root entity
     * @return optional of the aggregate instance fetched
     */
    FetcherResult<A, RECORD> fetchDeep(I id);

    /**
     * Sometimes we want to fetch the root "records" of an aggregate by a
     * custom query, e.g. to implement paging with a custom sort With this
     * method we can "complete" the result of a custom query to be able to
     * delivers full consistent aggregate instances.
     *
     * @param aggregateRecord - the root entity's record representation
     * @return optional of the aggregate instance fetched
     */
    FetcherResult<A, RECORD> fetchDeep(RECORD aggregateRecord);

}

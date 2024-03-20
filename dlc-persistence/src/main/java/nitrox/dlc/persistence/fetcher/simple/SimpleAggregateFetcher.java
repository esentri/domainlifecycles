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

package nitrox.dlc.persistence.fetcher.simple;

import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.persistence.fetcher.AggregateFetcher;
import nitrox.dlc.persistence.fetcher.FetcherResult;

/**
 * SimpleAggregateFetcher is a base class for all simple aggregate fetchers.
 *
 * @param <ID>     the identity type
 * @param <A>      the aggregate root type
 * @param <I>      the identity type
 * @param <RECORD> the record type
 *
 * @author Mario Herb
 */
public abstract class SimpleAggregateFetcher<ID, A extends AggregateRoot<I>, I extends Identity<ID>, RECORD> implements AggregateFetcher<A, I, RECORD> {


    /**
     * Fetches the basic aggregate by id value.
     *
     * @param id             the id value
     * @param fetcherContext the fetcher context
     * @return the aggregate
     */
    public abstract A fetchBasicByIdValue(ID id, SimpleFetcherContext<RECORD> fetcherContext);

    /**
     * Fetches the basic aggregate by record.
     *
     * @param aggregateRecord the aggregate record
     * @param fetcherContext  the fetcher context
     * @return the aggregate
     */
    public abstract A fetchBasicByRecord(RECORD aggregateRecord, SimpleFetcherContext<RECORD> fetcherContext);


    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherResult<A, RECORD> fetchDeep(I id) {
        SimpleFetcherContext<RECORD> fc = new SimpleFetcherContext<>();
        A root = fetchBasicByIdValue(id.value(), fc);
        return new FetcherResult<>(root, fc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherResult<A, RECORD> fetchDeep(RECORD aggregateRecord) {
        SimpleFetcherContext<RECORD> fc = new SimpleFetcherContext<>();
        A root = fetchBasicByRecord(aggregateRecord, fc);
        return new FetcherResult<>(root, fc);
    }
}

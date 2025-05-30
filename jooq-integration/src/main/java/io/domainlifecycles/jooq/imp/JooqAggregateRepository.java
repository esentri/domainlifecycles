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

package io.domainlifecycles.jooq.imp;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

/**
 * jOOQ specific implementation of a {@link PersistenceActionPublishingRepository}.
 *
 * @param <A> type of AggregateRoot
 * @param <I> type of Identity
 * @author Mario Herb
 */
public class JooqAggregateRepository<A extends AggregateRoot<I>, I extends Identity<?>> extends PersistenceActionPublishingRepository<I, A, UpdatableRecord<?>> {

    protected final DSLContext dslContext;
    private final JooqAggregateFetcher<A, I> fetcher;

    public JooqAggregateRepository(Class<A> aggregateRootClass,
                                   DSLContext dslContext,
                                   JooqDomainPersistenceProvider domainPersistenceProvider,
                                   PersistenceEventPublisher persistenceEventPublisher) {
        super(new JooqPersister(dslContext, domainPersistenceProvider), domainPersistenceProvider,
            persistenceEventPublisher);
        this.fetcher = new JooqAggregateFetcher<>(aggregateRootClass, dslContext, domainPersistenceProvider);
        this.dslContext = dslContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherResult<A, UpdatableRecord<?>> findResultById(I rootId) {
        return fetcher.fetchDeep(rootId);
    }

    public JooqAggregateFetcher<A, I> getFetcher() {
        return fetcher;
    }

}

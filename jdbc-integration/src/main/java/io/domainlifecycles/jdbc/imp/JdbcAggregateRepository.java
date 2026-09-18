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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;

/**
 * Plain JDBC based implementation of a {@link PersistenceActionPublishingRepository}.
 * <p>
 * All aggregate-level INSERT/UPDATE/DELETE cascade orchestration, optimistic locking retry-free version
 * bumping, and persistence event publishing is inherited unchanged from {@link
 * PersistenceActionPublishingRepository}/{@code DomainStructureAwareRepository} in the {@code persistence}
 * module - this class only wires a {@link JdbcPersister} and a {@link JdbcAggregateFetcher} together, exactly
 * as {@code JooqAggregateRepository} wires a {@code JooqPersister} and a {@code JooqAggregateFetcher}.
 *
 * @param <A> type of AggregateRoot
 * @param <I> type of Identity
 * @author Mario Herb
 */
public class JdbcAggregateRepository<A extends AggregateRoot<I>, I extends Identity<?>>
    extends PersistenceActionPublishingRepository<I, A, JdbcRecord> {

    private final JdbcAggregateFetcher<A, I> fetcher;

    /**
     * Constructs an instance of {@code JdbcAggregateRepository}.
     *
     * @param aggregateRootClass        the class of the aggregate root managed by this repository
     * @param domainPersistenceProvider the persistence provider used to resolve entity record mirrors, and
     *                                  supplying the connection, dialect and schema metadata registered
     *                                  centrally on it
     * @param persistenceEventPublisher the publisher for persistence events
     */
    public JdbcAggregateRepository(
        Class<A> aggregateRootClass,
        JdbcDomainPersistenceProvider domainPersistenceProvider,
        PersistenceEventPublisher persistenceEventPublisher
    ) {
        super(new JdbcPersister(domainPersistenceProvider), domainPersistenceProvider, persistenceEventPublisher);
        this.fetcher = new JdbcAggregateFetcher<>(aggregateRootClass, domainPersistenceProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherResult<A, JdbcRecord> findResultById(I rootId) {
        return fetcher.fetchDeep(rootId);
    }

    /**
     * Returns the {@link JdbcAggregateFetcher} instance associated with this repository.
     *
     * @return the fetcher responsible for retrieving aggregate roots from the database
     */
    public JdbcAggregateFetcher<A, I> getFetcher() {
        return fetcher;
    }
}

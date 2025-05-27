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

package io.domainlifecycles.persistence.repository;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.persister.Persister;

/**
 * This is the abstract superclass of all managed repositories that encapsulate
 * queries and access to aggregates according to the Domain Driven Design approach.
 * <p>
 * A repository mediates between the domain and data mapping layers and acts like
 * an in-memory collection of domain objects. Services construct declarative query
 * specifications and submit them to a repository for satisfaction. Aggregates
 * can be added to and removed from the repository, like a simple data collection,
 * and the mapping code encapsulated by the repository performs the appropriate
 * operations in the background. Conceptually, a repository encapsulates the
 * collection of aggregates persisted in a data store and the operations performed
 * on them, providing a more object-oriented view of the persistence layer.
 * PersistenceActionPublishingRepository also supports the goal of achieving clean separation and one-way
 * dependency between the domain and data mapping layers.
 * <p>
 * For more information, read about
 * <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">
 * Repositories</a>.
 *
 * @param <ID>     type of aggregate identity
 * @param <ROOT>   type of stored aggregates
 * @param <RECORD> type of record representation
 * @author Mario Herb
 */
public abstract class PersistenceActionPublishingRepository<ID extends Identity<?>, ROOT extends AggregateRoot<ID>,
    RECORD>
    extends DomainStructureAwareRepository<ID, ROOT, RECORD> {

    private final PersistenceEventPublisher persistenceEventPublisher;

    /**
     * Constructs a new instance of PersistenceActionPublishingRepository.
     *
     * @param persister                  the persister responsible for managing persistence actions
     *                                   such as insert, update, and delete for the record type.
     * @param domainPersistenceProvider  the domain persistence provider responsible for mapping
     *                                   the domain objects to their database representation.
     * @param persistenceEventPublisher  the persistence event publisher used to publish persistence-related actions.
     */
    protected PersistenceActionPublishingRepository(Persister<RECORD> persister,
                                                    DomainPersistenceProvider<RECORD> domainPersistenceProvider,
                                                    PersistenceEventPublisher persistenceEventPublisher) {
        super(persister, domainPersistenceProvider);
        this.persistenceEventPublisher = persistenceEventPublisher;
    }

    /**
     * This method delivers the current database state of the root by its root
     * id
     *
     * @param id of the aggregate root to be fetched.
     * @return Optional of the root entity instance, with the given id.
     */
    public abstract FetcherResult<ROOT, RECORD> findResultById(ID id);

    /**
     * This method delivers the current database state of the root by its root
     *
     * @param action to be published.
     */
    public void publish(PersistenceAction<?> action) {
        this.persistenceEventPublisher.publish(action);
    }
}

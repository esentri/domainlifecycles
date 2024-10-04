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

package io.domainlifecycles.persistence.repository.persister;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

/**
 * A SimplePersister is responsible on appliying simple INSERT, UPDATE or DELETE operations
 * on a given database for the passed PersistenceAction. Normally it maps
 * the {@link DomainObject} to its database representation (record)
 * and performes the corresponding action. It also must make sure that if there were changes performed by
 * the database (e.g. triggers) are
 * reflected back into the passed {@link DomainObject}.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link DomainObject}
 * @author Mario Herb
 */
public interface Persister<BASE_RECORD_TYPE> {

    /**
     * Inserts a new record for the given {@link DomainObject} into the database.
     *
     * @param insertAction the insert action
     * @param pc           the persistence context
     * @return the inserted record
     */
    BASE_RECORD_TYPE insertOne(PersistenceAction<BASE_RECORD_TYPE> insertAction,
                               PersistenceContext<BASE_RECORD_TYPE> pc);

    /**
     * Updates the record for the given {@link DomainObject} in the database.
     *
     * @param updateAction the update action
     * @param pc           the persistence context
     * @return the updated record
     */
    BASE_RECORD_TYPE updateOne(PersistenceAction<BASE_RECORD_TYPE> updateAction,
                               PersistenceContext<BASE_RECORD_TYPE> pc);

    /**
     * Deletes the record for the given {@link DomainObject} from the database.
     *
     * @param deleteAction the delete action
     * @param pc           the persistence context
     * @return the deleted record
     */
    BASE_RECORD_TYPE deleteOne(PersistenceAction<BASE_RECORD_TYPE> deleteAction,
                               PersistenceContext<BASE_RECORD_TYPE> pc);

    /**
     * Increases the version of the given {@link DomainObject} in the database.
     *
     * @param entity the entity
     * @param pc     the persistence context
     * @return the updated record
     */
    BASE_RECORD_TYPE increaseVersion(Entity<?> entity, PersistenceContext<BASE_RECORD_TYPE> pc);

}

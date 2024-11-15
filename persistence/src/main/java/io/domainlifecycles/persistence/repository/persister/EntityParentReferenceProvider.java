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

package io.domainlifecycles.persistence.repository.persister;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;

/**
 * An EntityParentReferenceProvider provides foreign key ids and adds them into a given entity record.
 * The provider must check all "parent" records of this record and copy the id values into the target entity record.
 * Therefor it must walk along the object tree along the object references up to the root and
 * map the corresponding entity objects to their record representation to get the key values to copy them.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link DomainObject}
 * @author Mario Herb
 */
public interface EntityParentReferenceProvider<BASE_RECORD_TYPE> {

    /**
     * Provides parent foreign key ids for a given entity record.
     *
     * @param entityRecord                    the entity record
     * @param domainObjectInstanceAccessModel the domain object instance access model
     */
    void provideParentForeignKeyIdsForEntityRecord(BASE_RECORD_TYPE entityRecord,
                                                   DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> domainObjectInstanceAccessModel);
}

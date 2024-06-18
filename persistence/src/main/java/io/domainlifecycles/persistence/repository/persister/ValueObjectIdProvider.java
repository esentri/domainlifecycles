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

import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

/**
 * A ValueObjectIdProvider provides technical ids for new {@link ValueObject}s.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link ValueObject}
 *
 * @author Mario Herb
 */
public interface ValueObjectIdProvider<BASE_RECORD_TYPE> {

    /**
     * Provides technical ids for new {@link ValueObject}s.
     *
     * @param newVoRecord the new {@link ValueObject} record
     * @param domainObjectInstanceAccessModel the instance access model
     * @param pc the persistence context
     */
    void provideTechnicalIdsForNewVoRecord(BASE_RECORD_TYPE newVoRecord, DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> domainObjectInstanceAccessModel,
                                           PersistenceContext<BASE_RECORD_TYPE> pc);
}

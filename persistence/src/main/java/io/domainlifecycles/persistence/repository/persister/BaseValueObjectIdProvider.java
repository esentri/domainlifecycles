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

package io.domainlifecycles.persistence.repository.persister;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

import java.io.Serializable;

/**
 * A BaseValueObjectIdProvider provides basic functionality for providing technical ids for new {@link ValueObject}s.
 *
 * <p>The technical id of the container may be of different types (e.g. a
 * long-compatible type or a UUID). The concrete type is resolved by the
 * implementation at runtime based on the underlying record's field type, so no
 * static type parameter for the container tech id is exposed here.</p>
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link ValueObject}
 * @author Mario Herb
 */
public abstract class BaseValueObjectIdProvider<BASE_RECORD_TYPE> implements ValueObjectIdProvider<BASE_RECORD_TYPE> {

    private final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider;

    public BaseValueObjectIdProvider(DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider) {
        this.domainPersistenceProvider = domainPersistenceProvider;
    }

    public void provideTechnicalIdsForNewVoRecord(BASE_RECORD_TYPE newVoRecord,
                                                  DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModel,
                                                  PersistenceContext<BASE_RECORD_TYPE> pc) {
        var container = instanceAccessModel
            .structuralPosition
            .accessPathFromRoot
            .descendingIterator()
            .next()
            .domainObject;
        Serializable containerTechId;
        if (container instanceof Entity) {
            containerTechId = (Serializable) domainPersistenceProvider.getId((Entity<?>) container).value();
        } else {
            BASE_RECORD_TYPE voContainerRecord = pc.getNewValueObjectRecord((ValueObject) container);
            containerTechId = selectExistingTechIdOfValueObject(voContainerRecord);
        }
        setContainerIdInNewVoRecord(newVoRecord, containerTechId);
        provideNewTechIdForValueObjectRecord(newVoRecord);
    }

    protected abstract void setContainerIdInNewVoRecord(BASE_RECORD_TYPE newVoRecord,
                                                        Serializable containerTechId);

    protected abstract Serializable selectExistingTechIdOfValueObject(BASE_RECORD_TYPE voContainerRecord);

    protected abstract void provideNewTechIdForValueObjectRecord(BASE_RECORD_TYPE newVoRecord);
}

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

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

/**
 * A BaseValueObjectIdProvider provides basic functionality for providing technical ids for new {@link ValueObject}s.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link ValueObject}
 * @param <CONTAINER_TECH_ID_TYPE> the type of the technical id of the container of the {@link ValueObject}
 *
 * @author Mario Herb
 */
public abstract class BaseValueObjectIdProvider<BASE_RECORD_TYPE, CONTAINER_TECH_ID_TYPE> implements ValueObjectIdProvider<BASE_RECORD_TYPE> {

    private final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider;

    /**
     * Constructor.
     *
     * @param domainPersistenceProvider the domain persistence provider
     */
    public BaseValueObjectIdProvider(DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider) {
        this.domainPersistenceProvider = domainPersistenceProvider;
    }

    /**
     * Provides the technical id for the given {@link ValueObject} record.
     *
     * @param newVoRecord the new {@link ValueObject} record
     * @param instanceAccessModel the instance access model
     * @param pc the persistence context
     */
    public void provideTechnicalIdsForNewVoRecord(BASE_RECORD_TYPE newVoRecord,
                                                  DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModel,
                                                  PersistenceContext<BASE_RECORD_TYPE> pc) {
        var container = instanceAccessModel
            .structuralPosition
            .accessPathFromRoot
            .descendingIterator()
            .next()
            .domainObject;
        CONTAINER_TECH_ID_TYPE containerTechId;
        if (container instanceof Entity) {
            containerTechId = (CONTAINER_TECH_ID_TYPE) (domainPersistenceProvider.getId((Entity<?>) container)).value();
        } else {
            BASE_RECORD_TYPE voContainerRecord = pc.getNewValueObjectRecord((ValueObject) container);
            containerTechId = selectExistingTechIdOfValueObject(voContainerRecord);
        }
        setContainerIdInNewVoRecord(newVoRecord, containerTechId);
        provideNewTechIdForValueObjectRecord(newVoRecord);
    }


    protected abstract void setContainerIdInNewVoRecord(BASE_RECORD_TYPE newVoRecord, CONTAINER_TECH_ID_TYPE containerTechId);

    //The implementor must know that the given record instance does not contain the id
    //It must be selected by using equals on all values --> it a value object
    // the technical Id should be completely hidden from the domain
    protected abstract CONTAINER_TECH_ID_TYPE selectExistingTechIdOfValueObject(BASE_RECORD_TYPE voContainerRecord);

    protected abstract void provideNewTechIdForValueObjectRecord(BASE_RECORD_TYPE newVoRecord);
}

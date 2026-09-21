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
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.StructuralPosition;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

import java.io.Serializable;
import java.util.Iterator;

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
        Serializable containerTechId = resolveContainerTechId(
            instanceAccessModel.structuralPosition.accessPathFromRoot.descendingIterator(), pc);
        setContainerIdInNewVoRecord(newVoRecord, containerTechId);
        provideNewTechIdForValueObjectRecord(newVoRecord);
    }

    /**
     * Resolves the technical id of the nearest actually persisted container (an {@link Entity}, or a
     * {@link ValueObject} that already has its own inserted record) for a new value object / scalar list
     * element record, walking up the access path from the immediate parent towards the root.
     * <p>
     * This walk-up is needed because an intermediate ancestor may be an inline value object (a plain,
     * non-collection value object field that is mapped as columns on its own owner's record rather than as
     * a separate row) - such an ancestor never has an entry in {@link PersistenceContext#getNewValueObjectRecord}
     * and must be skipped in favor of the next persisted ancestor.
     * <p>
     * A missing entry alone does not tell "genuinely inline" apart from "record-mapped, but not inserted
     * yet due to an ordering bug" - both look identical to {@link PersistenceContext#getNewValueObjectRecord}.
     * {@link #hasPendingInsertAction} resolves that ambiguity by checking whether an {@code INSERT} action
     * was ever detected for the ancestor in the first place: only a truly inline ancestor (no such action)
     * is skipped; one with a not-yet-run {@code INSERT} action fails loudly instead of being silently
     * misattributed to a more distant ancestor.
     *
     * @param ancestorsNearestFirst the access path ancestors, nearest (immediate parent) first
     * @param pc                    the persistence context
     * @return the technical id of the nearest persisted container
     */
    private Serializable resolveContainerTechId(
        Iterator<StructuralPosition.AccessPathElement> ancestorsNearestFirst,
        PersistenceContext<BASE_RECORD_TYPE> pc) {
        while (ancestorsNearestFirst.hasNext()) {
            var ancestor = ancestorsNearestFirst.next().domainObject;
            if (ancestor instanceof Entity) {
                return (Serializable) domainPersistenceProvider.getId((Entity<?>) ancestor).value();
            }
            BASE_RECORD_TYPE voContainerRecord = pc.getNewValueObjectRecord((ValueObject) ancestor);
            if (voContainerRecord != null) {
                return selectExistingTechIdOfValueObject(voContainerRecord);
            }
            if (hasPendingInsertAction((ValueObject) ancestor, pc)) {
                // this ancestor IS record-mapped - an INSERT action for it was detected during structure
                // processing - but that action has not actually run yet, so its record isn't registered
                // in the persistence context. Unlike a genuinely inline ancestor (handled below), this is
                // an insertion-ordering bug: walking further up would silently attribute the new value
                // object to the wrong (grand-)container instead.
                throw DLCPersistenceException.fail(
                    "Insertion ordering error: the record-mapped value object ancestor '%s' has not been "
                        + "inserted yet, so its technical id is not yet known.",
                    ancestor.getClass().getName());
            }
            //this ancestor has no INSERT action of its own at all - it is genuinely an inline (non
            //record-mapped) value object without a record of its own - keep walking up to find the
            //nearest persisted container
        }
        throw DLCPersistenceException.fail(
            "Could not determine the persisted container of a new value object record: neither an Entity nor a " +
                "previously inserted ValueObject was found in the access path!");
    }

    /**
     * Checks whether an {@code INSERT} action was detected for {@code ancestor} during structure processing
     * (making it record-mapped, i.e. not inline), regardless of whether that action has already run.
     */
    private boolean hasPendingInsertAction(ValueObject ancestor, PersistenceContext<BASE_RECORD_TYPE> pc) {
        return pc.getActionsPartitioned(ancestor.getClass().getName(), PersistenceAction.ActionType.INSERT)
            .stream()
            .anyMatch(action -> action.instanceAccessModel.domainObject() == ancestor);
    }

    protected abstract void setContainerIdInNewVoRecord(BASE_RECORD_TYPE newVoRecord,
                                                        Serializable containerTechId);

    protected abstract Serializable selectExistingTechIdOfValueObject(BASE_RECORD_TYPE voContainerRecord);

    protected abstract void provideNewTechIdForValueObjectRecord(BASE_RECORD_TYPE newVoRecord);
}

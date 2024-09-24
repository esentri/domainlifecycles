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

package io.domainlifecycles.persistence.repository.actions;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;

import java.util.Objects;


/**
 * We need to work with several kinds of PersistenceActions, which depend on the
 * changes that were detected:
 * <br>
 * INSERT - inserting a new record into a database. UPDATE - updating an
 * existing record in a database. DELETE - deleting an existing record from a
 * database. DELETE_UPDATE - sometimes before being able to delete a record. A
 * reference to record being deleted must be set to "null". This is necessary to
 * comply with foreign key constraints and to keep all the changes consistent.
 * <br>
 * A PersistenceAction keeps a reference to {@link DomainObjectInstanceAccessModel} on which a change was detected and the type of action,
 * that has to be applied to the database.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is being persisted
 *
 * @author Mario Herb
 */
public class PersistenceAction<BASE_RECORD_TYPE> {

    /**
     * The type of the action that has to be applied to the database.
     */
    public enum ActionType {INSERT, UPDATE, DELETE, DELETE_UPDATE}

    /**
     * The {@link DomainObjectInstanceAccessModel} on which a change was detected.
     */
    public final DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModel;

    /**
     * The {@link DomainObjectInstanceAccessModel} before the update.
     */
    public final DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModelBeforeUpdate;

    /**
     * The type of the action that has to be applied to the database.
     */
    public final ActionType actionType;
    private BASE_RECORD_TYPE actionRecord;

    /**
     * Creates a new PersistenceAction.
     *
     * @param instanceAccessModel the {@link DomainObjectInstanceAccessModel} on which a change was detected
     * @param actionType         the type of the action that has to be applied to the database
     * @param instanceAccessModelBeforeUpdate the {@link DomainObjectInstanceAccessModel} before the update
     */
    public PersistenceAction(DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModel,
                             ActionType actionType,
                             DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> instanceAccessModelBeforeUpdate) {
        if (actionType == null) {
            throw DLCPersistenceException.fail("Null action type!");
        }
        if (ActionType.UPDATE.equals(actionType) && instanceAccessModelBeforeUpdate == null) {
            throw DLCPersistenceException.fail("Update actions need a 'before' instance access model!");
        }
        if (instanceAccessModel == null) {
            throw DLCPersistenceException.fail("Every PersistenceAction must contain a non-null DomainObjectInstanceAccessModel!");
        }
        this.instanceAccessModel = instanceAccessModel;
        this.instanceAccessModelBeforeUpdate = instanceAccessModelBeforeUpdate;
        this.actionType = actionType;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistenceAction<?> that)) return false;
        return instanceAccessModel.equals(that.instanceAccessModel) && Objects.equals(instanceAccessModelBeforeUpdate, that.instanceAccessModelBeforeUpdate) && actionType == that.actionType;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(instanceAccessModel, instanceAccessModelBeforeUpdate, actionType);
    }

    /**
     * Returns the record type on which a change was detected.
     *
     * @return the record type on which a change was detected
     */
    public BASE_RECORD_TYPE getActionRecord() {
        return actionRecord;
    }

    /**
     * Sets the record type on which a change was detected.
     *
     * @param actionRecord the record type on which a change was detected
     */
    public void setActionRecord(BASE_RECORD_TYPE actionRecord) {
        this.actionRecord = actionRecord;
    }
}

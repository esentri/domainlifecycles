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

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.persister.BaseValueObjectIdProvider;
import io.domainlifecycles.persistence.repository.persister.ValueObjectIdProvider;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Sequence;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;

import java.util.Objects;

/**
 * jOOQ specific implementation of a {@link ValueObjectIdProvider}.
 *
 * @author Mario Herb
 */
public class JooqValueObjectIdProvider extends BaseValueObjectIdProvider<UpdatableRecord<?>, Long> implements ValueObjectIdProvider<UpdatableRecord<?>> {
    private final DSLContext dslContext;

    public JooqValueObjectIdProvider(
        DomainPersistenceProvider<UpdatableRecord<?>> domainPersistenceProvider,
        DSLContext dslContext) {
        super(domainPersistenceProvider);
        this.dslContext = Objects.requireNonNull(dslContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContainerIdInNewVoRecord(UpdatableRecord<?> newVoRecord, Long containerTechId) {
        var f = (Field<Long>) newVoRecord.getTable().field("CONTAINER_ID");
        if (f == null) {
            f = (Field<Long>) newVoRecord.getTable().field("container_id");
        }
        if (f == null) {
            throw DLCPersistenceException.fail("Record '%s' does not contain a field CONTAINER_ID", newVoRecord);
        }
        newVoRecord.setValue(f, containerTechId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long selectExistingTechIdOfValueObject(UpdatableRecord<?> voContainerRecord) {
        var f = voContainerRecord.getTable().field("ID");
        if (f == null) {
            f = voContainerRecord.getTable().field("id");
        }
        if (f == null) {
            throw DLCPersistenceException.fail("Record '%s' does not contain a field ID", voContainerRecord);
        }
        return (Long) f.getValue(voContainerRecord);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void provideNewTechIdForValueObjectRecord(UpdatableRecord<?> newVoRecord) {
        try {
            Sequence<?> s = newVoRecord.getTable().getSchema().getSequence(newVoRecord.getTable().getName() + "_SEQ");
            if (s == null) {
                s = newVoRecord.getTable().getSchema().getSequence(newVoRecord.getTable().getName() + "_seq");
            }
            if (s == null) {
                throw DLCPersistenceException.fail(
                    "Sequence '%s_SEQ' not found. Please create the sequence in your database!",
                    newVoRecord.getTable().getName());
            }
            var newTechId = dslContext.nextval(s).longValue();
            newVoRecord.setValue((Field<Long>) newVoRecord.getTable().getPrimaryKey().getFields().get(0), newTechId);
        } catch (DataAccessException ex) {
            throw DLCPersistenceException.fail("Couldn't access sequence '%s_SEQ'", ex,
                newVoRecord.getTable().getName());
        }
    }
}

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

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;

import java.util.Objects;

/**
 * A BasePersister provides basic functionality for persisting {@link DomainObject}s.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is used to persist the {@link DomainObject}
 * @author Mario Herb
 */
public abstract class BasePersister<BASE_RECORD_TYPE> implements Persister<BASE_RECORD_TYPE> {

    private final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider;
    private final ValueObjectIdProvider<BASE_RECORD_TYPE> valueObjectIdProvider;
    private final EntityParentReferenceProvider<BASE_RECORD_TYPE> entityParentReferenceProvider;

    public BasePersister(DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider,
                         ValueObjectIdProvider<BASE_RECORD_TYPE> valueObjectIdProvider,
                         EntityParentReferenceProvider<BASE_RECORD_TYPE> entityParentReferenceProvider) {
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.valueObjectIdProvider = valueObjectIdProvider;
        this.entityParentReferenceProvider = entityParentReferenceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BASE_RECORD_TYPE insertOne(PersistenceAction<BASE_RECORD_TYPE> insertAction,
                                      PersistenceContext<BASE_RECORD_TYPE> pc) {
        final BASE_RECORD_TYPE record = getRecordFromDomainObject(insertAction, pc.getProcessedRoot());
        if (insertAction.instanceAccessModel.isValueObject()) {
            valueObjectIdProvider.provideTechnicalIdsForNewVoRecord(record, insertAction.instanceAccessModel, pc);
            doInsert(record);
            pc.addNewValueObjectRecord((ValueObject) insertAction.instanceAccessModel.domainObject(), record);
        } else {
            doInsert(record);
            //changes to properties by a trigger or something are only allowed for entities, value objects should be
            // immutable
            adaptChangesFromRecordToEntity(insertAction.instanceAccessModel, record);
        }
        insertAction.setActionRecord(record);
        return record;
    }

    protected abstract void doInsert(BASE_RECORD_TYPE record);

    /**
     * {@inheritDoc}
     */
    @Override
    public BASE_RECORD_TYPE updateOne(PersistenceAction<BASE_RECORD_TYPE> updateAction,
                                      PersistenceContext<BASE_RECORD_TYPE> pc) {
        final BASE_RECORD_TYPE record = getRecordFromDomainObject(updateAction, pc.getProcessedRoot());
        doUpdate(record);
        adaptChangesFromRecordToEntity(updateAction.instanceAccessModel, record);
        updateAction.setActionRecord(record);
        return record;
    }

    protected abstract void doUpdate(BASE_RECORD_TYPE record);

    /**
     * {@inheritDoc}
     */
    @Override
    public BASE_RECORD_TYPE deleteOne(PersistenceAction<BASE_RECORD_TYPE> deleteAction,
                                      PersistenceContext<BASE_RECORD_TYPE> pc) {
        if (deleteAction.instanceAccessModel.isEntity()) {
            final BASE_RECORD_TYPE record = getRecordFromDomainObject(deleteAction, pc.getProcessedRoot());
            doDelete(record);
            deleteAction.setActionRecord(record);
            return record;
        } else {
            final BASE_RECORD_TYPE record = deleteRecordMappedValueObject(
                deleteAction.instanceAccessModel.domainObject(), pc);
            deleteAction.setActionRecord(record);
            return record;
        }
    }

    protected abstract void doDelete(BASE_RECORD_TYPE record);

    /**
     * {@inheritDoc}
     */

    @Override
    public BASE_RECORD_TYPE increaseVersion(Entity<?> entity, PersistenceContext<BASE_RECORD_TYPE> pc) {
        var em = Domain.entityMirrorFor(entity);
        var concurrencyField = em.getConcurrencyVersionField();
        if (concurrencyField.isPresent()) {
            RecordMapper<BASE_RECORD_TYPE, Entity<?>, AggregateRoot<?>> recordMapper =
                (RecordMapper<BASE_RECORD_TYPE, Entity<?>, AggregateRoot<?>>)
                    domainPersistenceProvider
                        .persistenceMirror.getEntityRecordMapper(entity.getClass().getName());
            final BASE_RECORD_TYPE record = recordMapper.from(entity, pc.getProcessedRoot());
            doIncreaseVersion(record);
            DlcAccess.accessorFor(entity)
                .poke(
                    concurrencyField.get().getName(),
                    entity.concurrencyVersion() + 1
                );
            return record;
        }
        return null;
    }

    protected abstract void doIncreaseVersion(BASE_RECORD_TYPE record);


    protected BASE_RECORD_TYPE getRecordFromDomainObject(final PersistenceAction<BASE_RECORD_TYPE> persistenceAction,
                                                         final AggregateRoot<?> root) {
        Objects.requireNonNull(persistenceAction);
        Objects.requireNonNull(root);
        BASE_RECORD_TYPE record;
        var recordMapper = (RecordMapper<BASE_RECORD_TYPE, DomainObject, AggregateRoot<?>>) persistenceAction
            .instanceAccessModel
            .recordMirror
            .recordMapper();
        record = recordMapper.from(persistenceAction.instanceAccessModel.domainObject(), root);
        if (persistenceAction.instanceAccessModel.domainObject() instanceof Entity && !(persistenceAction.instanceAccessModel.domainObject() instanceof AggregateRoot)) {
            entityParentReferenceProvider.provideParentForeignKeyIdsForEntityRecord(record,
                persistenceAction.instanceAccessModel);
        }
        return record;
    }

    protected BASE_RECORD_TYPE deleteRecordMappedValueObject(final DomainObject domainObject,
                                                             final PersistenceContext<BASE_RECORD_TYPE> pc) {
        Objects.requireNonNull(domainObject);
        Objects.requireNonNull(pc);
        BASE_RECORD_TYPE record = pc.getDatabaseStateRootFetched().fetchedContext().getRecordFor(
            domainObject).orElseThrow();
        doDelete(record);
        return record;
    }

    /**
     * E.g. Adapt increased version values or result values set or updated by database triggers into the entity, so
     * that its reference
     * represents the current database state.
     *
     * @param domainObjectInstanceAccessModel instance in which changes are taken over
     * @param record                          instance of which changes are adapted
     */
    protected void adaptChangesFromRecordToEntity(final DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> domainObjectInstanceAccessModel, final BASE_RECORD_TYPE record) {
        Objects.requireNonNull(record);
        var em = Domain.entityMirrorFor(domainObjectInstanceAccessModel.instanceType().getName());

        RecordMapper<BASE_RECORD_TYPE, ?, ?> recordMapper =
            (RecordMapper<BASE_RECORD_TYPE, ?, ?>) domainPersistenceProvider
            .persistenceMirror
            .getEntityRecordMapper(domainObjectInstanceAccessModel.instanceType().getName());
        var builder = recordMapper.recordToDomainObjectBuilder(record);

        domainObjectInstanceAccessModel
            .children.stream().filter(DomainObjectInstanceAccessModel::isRecordMapped).forEach(child -> {
                var accessorField =
                    child.structuralPosition.accessPathFromRoot.descendingIterator().next().accessorToNextElement;
                var builderTypeName = builder.instanceType().getName();
                var dtm = Domain.typeMirror(builderTypeName)
                    .orElseThrow(
                        () -> DLCPersistenceException.fail("DomainTypeMirror not found for '%s'", builderTypeName));
                var fm = dtm.fieldByName(accessorField);
                if (fm.getType().hasCollectionContainer()) {
                    builder.addValueToCollection(child.domainObject(), accessorField);
                } else {
                    builder.setFieldValue(child.domainObject(), accessorField);
                }
            });
        var mappedResult = builder.build();

        var instanceAccessor = DlcAccess.accessorFor(domainObjectInstanceAccessModel.domainObject());
        var resultAccessor = DlcAccess.accessorFor(mappedResult);
        em.getAllFields()
            .stream()
            .filter(f ->
                f.getType().getDomainType().equals(DomainType.VALUE_OBJECT) ||
                    f.getType().getDomainType().equals(DomainType.IDENTITY) ||
                    f.getType().getDomainType().equals(DomainType.ENUM) ||
                    f.getType().getDomainType().equals(DomainType.NON_DOMAIN)
            )
            .forEach(f -> {

                var propertyValueOriginal = instanceAccessor.peek(f.getName());
                var propertyValueResult = resultAccessor.peek(f.getName());

                if ((propertyValueOriginal == null && propertyValueResult != null) ||
                    (propertyValueOriginal != null && !propertyValueOriginal.equals(propertyValueResult))) {
                    instanceAccessor.poke(f.getName(), propertyValueResult);
                }
            });
    }

}

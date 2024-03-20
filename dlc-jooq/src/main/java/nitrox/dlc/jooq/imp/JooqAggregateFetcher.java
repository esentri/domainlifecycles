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

package nitrox.dlc.jooq.imp;

import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.jooq.configuration.JooqDomainPersistenceConfiguration;
import nitrox.dlc.jooq.imp.provider.JooqDomainPersistenceProvider;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.fetcher.InternalAggregateFetcher;
import nitrox.dlc.persistence.mirror.api.ValueObjectRecordMirror;
import nitrox.dlc.persistence.records.NewRecordInstanceProvider;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.NoDataFoundException;

import java.util.Collection;

/**
 * jOOQ specific implementation of a {@link InternalAggregateFetcher}.
 *
 * @author Mario Herb
 */
public class JooqAggregateFetcher<A extends AggregateRoot<I>, I extends Identity<?>> extends InternalAggregateFetcher<A, I, UpdatableRecord<?>> {

    private final JooqDomainPersistenceProvider domainPersistenceProvider;

    private final DSLContext dslContext;

    private final NewRecordInstanceProvider newRecordInstanceProvider;

    public JooqAggregateFetcher(Class<A> aggregateRootClass,
                                DSLContext dslContext,
                                JooqDomainPersistenceProvider domainPersistenceProvider) {
        super(
            aggregateRootClass,
            domainPersistenceProvider
        );
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.dslContext = dslContext;
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration = (JooqDomainPersistenceConfiguration) domainPersistenceProvider.domainPersistenceConfiguration;
        this.newRecordInstanceProvider = jooqPersistenceConfiguration.newRecordInstanceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected UpdatableRecord<?> getEntityRecordById(I id) {
       var entityClassName = Domain.entityMirrorForIdentityTypeName(id.getClass().getName()).getTypeName();
        String recordClassName = domainPersistenceProvider.persistenceMirror.getEntityRecordMirror(entityClassName).recordTypeName();
        var recordInstance = (UpdatableRecord<?>) newRecordInstanceProvider.provideNewRecord(recordClassName);

        var pk = recordInstance.getTable().getPrimaryKey();
        if (pk.getFields().size() > 1) {
            throw DLCPersistenceException.fail("Find by ID. Currently only single valued primary key are supported. The primary key '%s' contains multiple fields!", pk.getName());
        }
        recordInstance.set((Field) pk.getFields().get(0), id.value());
        recordInstance.attach(dslContext.configuration());
        try {
            recordInstance.refresh();
        } catch (NoDataFoundException ex) {
            return null;
        }
        return recordInstance;

    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected UpdatableRecord<?> getEntityReferenceRecordByParentRecord(UpdatableRecord<?> parentRecord, String referencedEntityClassName) {
        var parentRecordInstance = (UpdatableRecord<?>) parentRecord;
        String referencedRecordClassName = domainPersistenceProvider.entityRecordType(referencedEntityClassName);
        UpdatableRecord<?> dummyChildRecord = newRecordInstanceProvider.provideNewRecord(referencedRecordClassName);

        ForeignKey<UpdatableRecord<?>, UpdatableRecord<?>> fkCandidate = null;
        var fks = parentRecordInstance.getTable().getReferences();
        boolean childFk = false;
        for (var fk : fks) {
            if (fk.getKey().getTable().equals(dummyChildRecord.getTable())) {
                if (fkCandidate != null) {

                    throw DLCPersistenceException.fail("Only clear foreign key references between tables are supported! "
                        + "There are multiple foreign key relations between '%s' and '%s'.", parentRecordInstance.getTable().getName(), dummyChildRecord.getTable().getName());
                }
                if (fk.getKey().getFields().size() > 1) {
                    throw DLCPersistenceException.fail("Only single valued foreign key references between tables are supported!" +
                        " The foreign key '%s' contains multiple fields!", fk.getName());
                }
                if (!fk.getTable().equals(fk.getKey().getTable())) {
                    //no self reference support
                    fkCandidate = (ForeignKey<UpdatableRecord<?>, UpdatableRecord<?>>) fk;
                    dummyChildRecord.set((Field) fk.getKey().getFields().get(0), parentRecordInstance.get((Field) fk.getFields().get(0)));
                    dummyChildRecord.attach(dslContext.configuration());
                    childFk = true;
                }

            }
        }
        var otherFks = dummyChildRecord.getTable().getReferencesTo(parentRecordInstance.getTable());
        for (var fk : otherFks) {
            if (!fk.getTable().equals(fk.getKey().getTable())) {
                if (fkCandidate != null && !fkCandidate.equals(fk)) {
                    throw DLCPersistenceException.fail("Only clear foreign key references between tables are supported! "
                        + "There are multiple foreign key relations between '%s' and '%s'.", parentRecordInstance.getTable().getName(), dummyChildRecord.getTable().getName());
                }
                fkCandidate = (ForeignKey<UpdatableRecord<?>, UpdatableRecord<?>>) fk;
            }
        }
        if (fkCandidate == null) {
            throw DLCPersistenceException.fail("No foreign key references between tables '%s' and '%s' were found! Self references (hierarchy) are not supported!", parentRecordInstance.getTable().getName(), dummyChildRecord.getTable().getName());
        }
        Result<?> r;
        if (childFk) {
            try {
                dummyChildRecord.refresh();
            } catch (NoDataFoundException ex) {
                return null;
            }
            return dummyChildRecord;
        } else {
            r = fkCandidate.fetchChildren(parentRecordInstance);
        }
        if (r.size() > 1) {
            throw DLCPersistenceException.fail("More than 1 row was fetched via fk reference '%s'! But on entity side this was modelled as 1-1 relation!", fkCandidate.getName());
        }
        if (r.size() > 0) {
            return (UpdatableRecord<?>) r.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<UpdatableRecord<?>> getChildValueObjectRecordCollectionByParentRecord(UpdatableRecord<?> parentRecord, ValueObjectRecordMirror<UpdatableRecord<?>> vorm) {
        var o = newRecordInstanceProvider.provideNewRecord(vorm.recordTypeName());
        var dummyChildRecord = (TableRecord<?>) o;

        var fks = dummyChildRecord.getTable().getReferencesTo(parentRecord.getTable());


        if (fks.size() == 0) {
            throw DLCPersistenceException.fail("No foreign key references between tables '%s' and '%s' were found!", parentRecord.getTable().getName(), dummyChildRecord.getTable().getName());
        }
        if (fks.size() > 1) {
            throw DLCPersistenceException.fail("ValueObject references: Exact foreign key reference between table '%s' and '%s' could not be identified! '%d' FK definitions could be found!", dummyChildRecord.getTable(), parentRecord.getTable(), fks.size());
        }
        ForeignKey<UpdatableRecord<?>,UpdatableRecord<?>> fk = (ForeignKey<UpdatableRecord<?>, UpdatableRecord<?>>) fks.get(0);
        return fk.fetchChildren(parentRecord);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    protected Collection<UpdatableRecord<?>> getEntityReferenceRecordCollectionByParentRecord(UpdatableRecord<?> parentRecord, String referencedEntityClassName) {
        String referencedEntityRecordClassName = domainPersistenceProvider.entityRecordType(referencedEntityClassName);

        return fetchChildren(parentRecord, referencedEntityRecordClassName);
    }

    @SuppressWarnings("unchecked")
    private Collection<UpdatableRecord<?>> fetchChildren(Object parentRecord, String referencedRecordClassName) {
        var parentRecordInstance = (TableRecord<?>) parentRecord;
        var o = newRecordInstanceProvider.provideNewRecord(referencedRecordClassName);
        var dummyChildRecord = (TableRecord<?>) o;

        ForeignKey<UpdatableRecord<?>,UpdatableRecord<?>> fkCandidate = null;
        var fks = dummyChildRecord.getTable().getReferencesTo(parentRecordInstance.getTable());
        for (var fk : fks) {
            if (fkCandidate != null) {
                throw DLCPersistenceException.fail("Only clear foreign key references between tables are supported! "
                    + "There are multiple foreign key relations between '%s' and '%s'.", dummyChildRecord.getTable().getName(), parentRecordInstance.getTable().getName());
            }
            fkCandidate = (ForeignKey<UpdatableRecord<?>,UpdatableRecord<?>>)fk;
        }
        if (fkCandidate == null) {
            throw DLCPersistenceException.fail("No foreign key references between tables '%s' and '%s' were found!", parentRecordInstance.getTable().getName(), dummyChildRecord.getTable().getName());
        }
        return fkCandidate.fetchChildren((UpdatableRecord<?>) parentRecordInstance);
    }
}

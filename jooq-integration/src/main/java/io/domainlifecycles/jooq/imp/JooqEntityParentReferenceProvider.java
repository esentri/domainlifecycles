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

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.StructuralPosition;
import io.domainlifecycles.persistence.repository.persister.EntityParentReferenceProvider;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.UpdatableRecord;

import java.util.Iterator;

/**
 * jOOQ specific implementation of a {@link EntityParentReferenceProvider}.
 *
 * @author Mario Herb
 */
public class JooqEntityParentReferenceProvider implements EntityParentReferenceProvider<UpdatableRecord<?>> {

    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    public JooqEntityParentReferenceProvider(JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void provideParentForeignKeyIdsForEntityRecord(UpdatableRecord<?> entityRecord,
                                                          DomainObjectInstanceAccessModel<UpdatableRecord<?>> domainObjectInstanceAccessModel) {
        var foreignKeysToBeProvided = entityRecord
            .getTable()
            .getReferences()
            .stream()
            .filter(r -> ((ForeignKey<?, ?>) r)
                .getFields()
                .stream()
                .filter(tf -> !tf.getDataType().nullable())
                .anyMatch(tf -> tf.getValue(entityRecord) == null)
            ).toList();
        if (foreignKeysToBeProvided.size() > 0) {
            foreignKeysToBeProvided.forEach(
                fk -> {
                    Iterator<StructuralPosition.AccessPathElement> it = domainObjectInstanceAccessModel
                        .structuralPosition
                        .accessPathFromRoot
                        .descendingIterator();
                    while (it.hasNext()) {
                        StructuralPosition.AccessPathElement ancestor = it.next();
                        EntityRecordMirror<?> erm = jooqDomainPersistenceProvider
                            .persistenceMirror.getEntityRecordMirror(ancestor.domainObject.getClass().getName());
                        if (erm.recordTypeName().equals(fk.getKey().getTable().getRecordType().getName())) {
                            AggregateRoot<?> root = (AggregateRoot<?>) domainObjectInstanceAccessModel
                                .structuralPosition
                                .accessPathFromRoot
                                .getFirst()
                                .domainObject;
                            UpdatableRecord<?> ancestorRecord = ((RecordMapper<UpdatableRecord<?>, Entity<?>,
                                AggregateRoot<?>>) erm.recordMapper())
                                .from((Entity<?>) ancestor.domainObject, root);
                            entityRecord.set((Field) fk.getFields().get(0), ancestorRecord.key().getValue(0));
                        }
                    }
                }
            );

        }
    }
}

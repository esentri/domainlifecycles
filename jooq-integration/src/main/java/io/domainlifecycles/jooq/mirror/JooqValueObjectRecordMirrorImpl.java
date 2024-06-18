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

package io.domainlifecycles.jooq.mirror;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import org.jooq.UpdatableRecord;

import java.util.List;
import java.util.Objects;

/**
 * jOOQ specific implementation of a {@link ValueObjectRecordMirror}.
 *
 * @author Mario Herb
 */
public class JooqValueObjectRecordMirrorImpl implements ValueObjectRecordMirror<UpdatableRecord<?>> {

    private final String containingEntityTypeName;
    private final String containedValueObjectTypeName;
    private final Class<? extends UpdatableRecord<?>> valueObjectRecordType;
    private final List<String> pathFromEntityToValueObject;

    private String recordMappedContainerTypeName;

    private final RecordMapper<UpdatableRecord<?>,?,?> valueObjectRecordMapperInstance;
    private final List<String> references;

    private EntityRecordMirror<UpdatableRecord<?>> owner;

    public JooqValueObjectRecordMirrorImpl(String containingEntityTypeName,
                                           String containedValueObjectTypeName,
                                           Class<? extends UpdatableRecord<?>> valueObjectRecordType,
                                           List<String> pathFromEntityToValueObject,
                                           RecordMapper<UpdatableRecord<?>,?,?> valueObjectRecordMapperInstance,
                                           List<String> references) {
        this.containingEntityTypeName = Objects.requireNonNull(containingEntityTypeName);
        this.containedValueObjectTypeName = Objects.requireNonNull(containedValueObjectTypeName);
        this.valueObjectRecordType = Objects.requireNonNull(valueObjectRecordType);
        this.pathFromEntityToValueObject = Objects.requireNonNull(pathFromEntityToValueObject);

        this.valueObjectRecordMapperInstance = valueObjectRecordMapperInstance;
        this.references = references;
        if (this.references == null || this.references.size() == 0) {
            throw DLCPersistenceException.fail("A value object when being persisted in its own record needs at least a reference (foreign key) " +
                    "to its container entity in the database representation. \n" +
                    "There is no database reference detected for '%s'"
                    + " within its container '%s'!",
                containingEntityTypeName, containedValueObjectTypeName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String recordTypeName() {
        return this.valueObjectRecordType.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String domainObjectTypeName() {
        return this.containedValueObjectTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String containingEntityTypeName() {
        return this.containingEntityTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> pathSegments() {
        return pathFromEntityToValueObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String completePath() {
        var b = new StringBuilder();
        pathFromEntityToValueObject.forEach(s -> b.append(".").append(s));
        return b.substring(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> enforcedReferences() {
        return references;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordMapper<UpdatableRecord<?>,?,?> recordMapper() {
        return this.valueObjectRecordMapperInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String recordMappedContainerTypeName() {
        //we need to initialize this variable lazy, because at instantiation time, we do not have the complete mirror structure
        if (this.recordMappedContainerTypeName == null) {
            JooqEntityRecordMirrorImpl entityRecordMirror = (JooqEntityRecordMirrorImpl) this.owner;
            var predecessorVorm = entityRecordMirror
                .valueObjectRecords()
                .stream()
                .filter(vorm -> this.completePath().startsWith(vorm.completePath())
                    && (this.pathSegments().size() == (vorm.pathSegments().size() + 1))).min((o1, o2) -> Integer.compare(o1.pathSegments().size(), o2.pathSegments().size()) * -1);
            if (predecessorVorm.isPresent()) {
                var vorm = predecessorVorm.get();
                this.recordMappedContainerTypeName = vorm.domainObjectTypeName();
            } else {
                this.recordMappedContainerTypeName = entityRecordMirror.domainObjectTypeName();
            }

        }
        return this.recordMappedContainerTypeName;
    }

    @Override
    public EntityRecordMirror<UpdatableRecord<?>> owner() {
        return owner;
    }

    @Override
    public void setOwner(EntityRecordMirror<UpdatableRecord<?>> owner){
        this.owner = owner;
    }


}

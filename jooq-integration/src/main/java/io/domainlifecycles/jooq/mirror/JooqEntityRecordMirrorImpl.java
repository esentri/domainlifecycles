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

import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import org.jooq.UpdatableRecord;

import java.util.List;

/**
 * jOOQ specific implementation of a {@link EntityRecordMirror}.
 *
 * @author Mario Herb
 */
public class JooqEntityRecordMirrorImpl implements EntityRecordMirror<UpdatableRecord<?>> {

    private final String recordTypeName;
    private final String entityTypeName;


    private final RecordMapper<UpdatableRecord<?>, ?, ?> entityRecordMapperInstance;
    private final List<ValueObjectRecordMirror<UpdatableRecord<?>>> valueObjectRecordMirrors;

    private final List<String> enforcedReferences;

    public JooqEntityRecordMirrorImpl(String recordTypeName,
                                      String entityTypeName,
                                      RecordMapper<UpdatableRecord<?>,?,?> entityRecordMapper,
                                      List<String> enforcedReferences,
                                      List<ValueObjectRecordMirror<UpdatableRecord<?>>> valueObjectRecordMirrors) {
        this.recordTypeName = recordTypeName;
        this.entityTypeName = entityTypeName;
        entityRecordMapperInstance = entityRecordMapper;
        this.valueObjectRecordMirrors = valueObjectRecordMirrors;
        this.enforcedReferences = enforcedReferences;
        for(var vrm : valueObjectRecordMirrors){
            vrm.setOwner(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String recordTypeName() {
        return recordTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String domainObjectTypeName() {
        return entityTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordMapper<UpdatableRecord<?>, ?, ?> recordMapper() {
        return entityRecordMapperInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueObjectRecordMirror<UpdatableRecord<?>>> valueObjectRecords() {
        return this.valueObjectRecordMirrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> enforcedReferences() {
        return this.enforcedReferences;
    }

    /// ----------------------------------------------------------

    /// COMPARISON & HASHING.

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityRecordMirror erm)) return false;
        return recordTypeName.equals(erm.recordTypeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * entityTypeName.hashCode() + recordTypeName.hashCode();
    }

    /// ----------------------------------------------------------

    /// STRING REPRESENTATION.

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "JooqEntityRecordMirrorImpl{" +
            "recordTypeName=" + recordTypeName +
            ", entityTypeName=" + entityTypeName +
            ", entityRecordMapperInstance=" + entityRecordMapperInstance +
            ", valueObjectRecordMirrors=" + valueObjectRecordMirrors +
            ", enforcedReferences=" + enforcedReferences +
            '}';
    }

}

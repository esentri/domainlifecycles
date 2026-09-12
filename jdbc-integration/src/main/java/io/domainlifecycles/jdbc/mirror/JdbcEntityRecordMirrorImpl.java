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

package io.domainlifecycles.jdbc.mirror;

import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;

import java.util.List;

/**
 * Plain JDBC based implementation of a {@link EntityRecordMirror}. The record type name is the physical
 * table name.
 *
 * @author Mario Herb
 */
public class JdbcEntityRecordMirrorImpl implements EntityRecordMirror<JdbcRecord> {

    private final String recordTypeName;
    private final String entityTypeName;

    private final RecordMapper<JdbcRecord, ?, ?> entityRecordMapperInstance;
    private final List<ValueObjectRecordMirror<JdbcRecord>> valueObjectRecordMirrors;

    private final List<String> enforcedReferences;

    /**
     * Constructs an instance of {@code JdbcEntityRecordMirrorImpl}.
     *
     * @param recordTypeName           the physical table name.
     * @param entityTypeName           the name of the entity type.
     * @param entityRecordMapper       the {@link RecordMapper} instance responsible for mapping records to entities.
     * @param enforcedReferences       a list of enforced reference names that are part of the entity record structure.
     * @param valueObjectRecordMirrors a list of {@link ValueObjectRecordMirror} instances that represent
     *                                 the value objects associated with this entity record.
     */
    public JdbcEntityRecordMirrorImpl(String recordTypeName,
                                       String entityTypeName,
                                       RecordMapper<JdbcRecord, ?, ?> entityRecordMapper,
                                       List<String> enforcedReferences,
                                       List<ValueObjectRecordMirror<JdbcRecord>> valueObjectRecordMirrors) {
        this.recordTypeName = recordTypeName;
        this.entityTypeName = entityTypeName;
        this.entityRecordMapperInstance = entityRecordMapper;
        this.valueObjectRecordMirrors = valueObjectRecordMirrors;
        this.enforcedReferences = enforcedReferences;
        for (var vrm : valueObjectRecordMirrors) {
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
    public RecordMapper<JdbcRecord, ?, ?> recordMapper() {
        return entityRecordMapperInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueObjectRecordMirror<JdbcRecord>> valueObjectRecords() {
        return this.valueObjectRecordMirrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> enforcedReferences() {
        return this.enforcedReferences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityRecordMirror<?> erm)) return false;
        return recordTypeName.equals(erm.recordTypeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * entityTypeName.hashCode() + recordTypeName.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "JdbcEntityRecordMirrorImpl{" +
            "recordTypeName=" + recordTypeName +
            ", entityTypeName=" + entityTypeName +
            ", entityRecordMapperInstance=" + entityRecordMapperInstance +
            ", valueObjectRecordMirrors=" + valueObjectRecordMirrors +
            ", enforcedReferences=" + enforcedReferences +
            '}';
    }
}

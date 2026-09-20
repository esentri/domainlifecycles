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
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;

import java.util.List;
import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link ValueObjectRecordMirror}.
 * <p>
 * Unlike the jOOQ based {@code JooqValueObjectRecordMirrorImpl}, which derives {@link #recordTypeName()} from
 * the generated record {@code Class}, this implementation takes the physical table name directly as a
 * constructor argument: every table in this module is represented by the same {@link JdbcRecord} class, so
 * the class itself carries no per-table identity to derive a name from.
 *
 * @author Mario Herb
 */
public class JdbcValueObjectRecordMirrorImpl implements ValueObjectRecordMirror<JdbcRecord> {

    private final String containingEntityTypeName;
    private final String containedValueObjectTypeName;
    private final String recordTypeName;
    private final List<String> pathFromEntityToValueObject;

    private String recordMappedContainerTypeName;

    private final RecordMapper<JdbcRecord, ?, ?> valueObjectRecordMapperInstance;
    private final List<String> references;

    private EntityRecordMirror<JdbcRecord> owner;

    /**
     * Constructs a new instance of {@code JdbcValueObjectRecordMirrorImpl}.
     *
     * @param containingEntityTypeName        the name of the entity type that contains the value object, must not be null
     * @param containedValueObjectTypeName    the name of the value object type contained within the entity, must not be null
     * @param recordTypeName                  the physical table name of the record representing the value object, must not be null
     * @param pathFromEntityToValueObject      the path segments representing the navigation path from the entity to the value object, must not be null
     * @param valueObjectRecordMapperInstance  the record mapper instance to map the value object record to its domain object and vice versa
     * @param references                      the list of database references (e.g., foreign keys) that link the value object's record to its containing entity's record, must not be null or empty
     * @throws DLCPersistenceException if the references list is null or empty, as a value object requires at least one database reference to its containing entity
     */
    public JdbcValueObjectRecordMirrorImpl(String containingEntityTypeName,
                                            String containedValueObjectTypeName,
                                            String recordTypeName,
                                            List<String> pathFromEntityToValueObject,
                                            RecordMapper<JdbcRecord, ?, ?> valueObjectRecordMapperInstance,
                                            List<String> references) {
        this.containingEntityTypeName = Objects.requireNonNull(containingEntityTypeName);
        this.containedValueObjectTypeName = Objects.requireNonNull(containedValueObjectTypeName);
        this.recordTypeName = Objects.requireNonNull(recordTypeName);
        this.pathFromEntityToValueObject = Objects.requireNonNull(pathFromEntityToValueObject);

        this.valueObjectRecordMapperInstance = valueObjectRecordMapperInstance;
        this.references = references;
        if (this.references == null || this.references.isEmpty()) {
            throw DLCPersistenceException.fail(
                "A value object when being persisted in its own record needs at least a reference (foreign key) " +
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
        return this.recordTypeName;
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
    public RecordMapper<JdbcRecord, ?, ?> recordMapper() {
        return this.valueObjectRecordMapperInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String recordMappedContainerTypeName() {
        //we need to initialize this variable lazy, because at instantiation time, we do not have the complete mirror
        // structure
        if (this.recordMappedContainerTypeName == null) {
            var entityRecordMirror = this.owner;
            var predecessorVorm = entityRecordMirror
                .valueObjectRecords()
                .stream()
                // a strict prefix *at a path-segment boundary* - plain String.startsWith(vorm.completePath())
                // would also match an unrelated sibling field whose name happens to extend the candidate's
                // last segment (e.g. "valueObjectsOneToMany2..." is a startsWith match for
                // "valueObjectsOneToMany", even though they are sibling fields, not ancestor/descendant)
                .filter(vorm -> this.completePath().startsWith(vorm.completePath() + ".")
                    && (this.pathSegments().size() == (vorm.pathSegments().size() + 1))).min(
                    (o1, o2) -> Integer.compare(o1.pathSegments().size(), o2.pathSegments().size()) * -1);
            if (predecessorVorm.isPresent()) {
                var vorm = predecessorVorm.get();
                this.recordMappedContainerTypeName = vorm.domainObjectTypeName();
            } else {
                this.recordMappedContainerTypeName = entityRecordMirror.domainObjectTypeName();
            }
        }
        return this.recordMappedContainerTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecordMirror<JdbcRecord> owner() {
        return owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwner(EntityRecordMirror<JdbcRecord> owner) {
        this.owner = owner;
    }
}

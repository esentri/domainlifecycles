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

package io.domainlifecycles.jdbc.imp.provider;

import io.domainlifecycles.jdbc.mirror.JdbcEntityRecordMirrorImpl;
import io.domainlifecycles.jdbc.mirror.JdbcValueObjectRecordMirrorImpl;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides {@link EntityRecordMirror} and {@link ValueObjectRecordMirror} instances for plain JDBC based
 * persistence.
 * <p>
 * This deliberately does not implement the shared {@code RecordMirrorInstanceProvider<BASE_RECORD_TYPE>}
 * interface (used by the jOOQ based integration): that interface's {@code provideValueObjectRecordMirror}
 * method identifies a value object's record type by a {@code Class<? extends BASE_RECORD_TYPE>} parameter,
 * which presupposes one distinct Java class per table. In this module every table is represented by the
 * same {@link JdbcRecord} class, so the table name is passed as a plain String instead, requiring this
 * module-local method signature.
 * <p>
 * Where the jOOQ based {@code JooqRecordMirrorInstanceProvider} discovers a record's foreign key
 * references by instantiating a record and inspecting its generated jOOQ {@code Table} metamodel, this
 * implementation reads the same information from a {@link JdbcSchemaMetadata} snapshot.
 *
 * @author Mario Herb
 */
public class JdbcRecordMirrorInstanceProvider {

    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcRecordMirrorInstanceProvider}.
     *
     * @param schemaMetadata the schema metadata snapshot to read foreign key information from
     */
    public JdbcRecordMirrorInstanceProvider(JdbcSchemaMetadata schemaMetadata) {
        this.schemaMetadata = Objects.requireNonNull(schemaMetadata);
    }

    /**
     * Provides the entity record mirror for a table.
     *
     * @param recordTypeName                           the physical table name
     * @param entityTypeName                           name of entity type
     * @param mapper                                   the record mapper
     * @param valueObjectRecordMirrors                 mirrors of value object records
     * @param recordCanonicalNameToDomainObjectTypeMap table name to domain object type(s)
     * @return the entity record mirror
     */
    public EntityRecordMirror<JdbcRecord> provideEntityRecordMirror(
        String recordTypeName,
        String entityTypeName,
        RecordMapper<JdbcRecord, ?, ?> mapper,
        List<ValueObjectRecordMirror<JdbcRecord>> valueObjectRecordMirrors,
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap
    ) {
        return new JdbcEntityRecordMirrorImpl(
            recordTypeName,
            entityTypeName,
            mapper,
            enforcedReferences(recordTypeName, recordCanonicalNameToDomainObjectTypeMap),
            valueObjectRecordMirrors
        );
    }

    /**
     * Provides the value object record mirror for a table.
     *
     * @param containingEntityTypeName                 name of containing entity type
     * @param containedValueObjectTypeName              name of contained value object type
     * @param recordTypeName                           the physical table name
     * @param pathFromEntityToValueObject               path from entity to value object
     * @param mapper                                   the record mapper
     * @param recordCanonicalNameToDomainObjectTypeMap table name to domain object type(s)
     * @return the value object record mirror
     */
    public ValueObjectRecordMirror<JdbcRecord> provideValueObjectRecordMirror(
        String containingEntityTypeName,
        String containedValueObjectTypeName,
        String recordTypeName,
        List<String> pathFromEntityToValueObject,
        RecordMapper<JdbcRecord, ?, ?> mapper,
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap
    ) {
        return new JdbcValueObjectRecordMirrorImpl(
            containingEntityTypeName,
            containedValueObjectTypeName,
            recordTypeName,
            pathFromEntityToValueObject,
            mapper,
            enforcedReferences(recordTypeName, recordCanonicalNameToDomainObjectTypeMap)
        );
    }

    private List<String> enforcedReferences(
        String recordTypeName,
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap
    ) {
        return schemaMetadata.table(recordTypeName).foreignKeys().stream()
            .flatMap(fk -> recordCanonicalNameToDomainObjectTypeMap
                .getOrDefault(fk.referencedTableName(), List.of())
                .stream())
            .distinct()
            .collect(Collectors.toList());
    }
}

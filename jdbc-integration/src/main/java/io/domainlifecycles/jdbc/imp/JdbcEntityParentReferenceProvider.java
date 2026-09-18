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

package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.repository.persister.EntityParentReferenceProvider;

import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link EntityParentReferenceProvider}.
 * <p>
 * Where the jOOQ based implementation re-maps the ancestor entity into a record just to read its primary key
 * value, this implementation reads the ancestor's id directly via {@link
 * io.domainlifecycles.persistence.provider.DomainPersistenceProvider#getId(Entity)} - the same shortcut
 * already used by {@link io.domainlifecycles.persistence.repository.persister.BaseValueObjectIdProvider}
 * (the generic base class {@link JdbcValueObjectIdProvider} builds on) for the equivalent problem, so this is
 * an established pattern rather than a JDBC-specific shortcut.
 *
 * @author Mario Herb
 */
public class JdbcEntityParentReferenceProvider implements EntityParentReferenceProvider<JdbcRecord> {

    private final JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;
    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcEntityParentReferenceProvider}.
     *
     * @param jdbcDomainPersistenceProvider the persistence provider used to look up entity record mirrors, and
     *                                      supplying the schema metadata registered centrally on it
     */
    public JdbcEntityParentReferenceProvider(JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider) {
        this.jdbcDomainPersistenceProvider = Objects.requireNonNull(jdbcDomainPersistenceProvider);
        this.schemaMetadata = Objects.requireNonNull(jdbcDomainPersistenceProvider.schemaMetadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void provideParentForeignKeyIdsForEntityRecord(
        JdbcRecord entityRecord,
        DomainObjectInstanceAccessModel<JdbcRecord> domainObjectInstanceAccessModel
    ) {
        var table = schemaMetadata.table(entityRecord.tableName());
        var foreignKeysToBeProvided = table.foreignKeys().stream()
            .filter(fk -> !table.column(fk.columnName()).nullable())
            .filter(fk -> entityRecord.get(table.column(fk.columnName()).name()) == null)
            .toList();
        if (foreignKeysToBeProvided.isEmpty()) {
            return;
        }
        foreignKeysToBeProvided.forEach(fk -> {
            var ancestors = domainObjectInstanceAccessModel.structuralPosition.accessPathFromRoot
                .descendingIterator();
            while (ancestors.hasNext()) {
                var ancestor = ancestors.next();
                var erm = jdbcDomainPersistenceProvider.persistenceMirror
                    .getEntityRecordMirror(ancestor.domainObject.getClass().getName());
                if (erm.recordTypeName().equalsIgnoreCase(fk.referencedTableName())) {
                    var ancestorId = jdbcDomainPersistenceProvider.getId((Entity<?>) ancestor.domainObject);
                    entityRecord.set(table.column(fk.columnName()).name(), ancestorId.value());
                }
            }
        });
    }
}

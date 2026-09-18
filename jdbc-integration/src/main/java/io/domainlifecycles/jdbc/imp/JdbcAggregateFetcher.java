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

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.InternalAggregateFetcher;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link InternalAggregateFetcher}.
 * <p>
 * Where the jOOQ based implementation resolves foreign key relationships between two tables at fetch time via
 * jOOQ's live, generated table metamodel ({@code Table.getReferences()}/{@code getReferencesTo(...)}), this
 * implementation resolves the same relationships from the precomputed {@link JdbcSchemaMetadata} snapshot,
 * then builds and executes a plain {@code SELECT} statement.
 *
 * @param <A> type of AggregateRoot
 * @param <I> type of Identity
 * @author Mario Herb
 */
public class JdbcAggregateFetcher<A extends AggregateRoot<I>, I extends Identity<?>>
    extends InternalAggregateFetcher<A, I, JdbcRecord> {

    private final JdbcDomainPersistenceProvider domainPersistenceProvider;
    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs an instance of {@code JdbcAggregateFetcher}.
     *
     * @param aggregateRootClass        the class of the aggregate root being managed
     * @param domainPersistenceProvider the persistence provider used to resolve entity record mirrors, and
     *                                  supplying the connection and schema metadata registered centrally on it
     */
    public JdbcAggregateFetcher(
        Class<A> aggregateRootClass,
        JdbcDomainPersistenceProvider domainPersistenceProvider
    ) {
        super(aggregateRootClass, domainPersistenceProvider);
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.connectionProvider = Objects.requireNonNull(domainPersistenceProvider.connectionProvider);
        this.schemaMetadata = Objects.requireNonNull(domainPersistenceProvider.schemaMetadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JdbcRecord getEntityRecordById(I id) {
        var entityTypeName = Domain.entityMirrorForIdentityTypeName(id.getClass().getName()).getTypeName();
        var tableName = domainPersistenceProvider.persistenceMirror.getEntityRecordMirror(entityTypeName)
            .recordTypeName();
        var table = schemaMetadata.table(tableName);
        return selectByPrimaryKey(table, id.value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JdbcRecord getEntityReferenceRecordByParentRecord(JdbcRecord parentRecord,
                                                                 String referencedEntityClassName) {
        var childTableName = domainPersistenceProvider.entityRecordType(referencedEntityClassName);
        var parentTable = schemaMetadata.table(parentRecord.tableName());
        var childTable = schemaMetadata.table(childTableName);

        if (parentTable.name().equalsIgnoreCase(childTable.name())) {
            throw DLCPersistenceException.fail(
                "Self references (hierarchy) are not supported! Table: '%s'", parentTable.name());
        }

        var parentHoldsForeignKey = parentTable.foreignKeys().stream()
            .filter(fk -> fk.referencedTableName().equalsIgnoreCase(childTable.name()))
            .toList();
        var childHoldsForeignKey = childTable.foreignKeys().stream()
            .filter(fk -> fk.referencedTableName().equalsIgnoreCase(parentTable.name()))
            .toList();

        if (parentHoldsForeignKey.size() + childHoldsForeignKey.size() > 1) {
            throw DLCPersistenceException.fail(
                "Only clear foreign key references between tables are supported! There are multiple foreign " +
                    "key relations between '%s' and '%s'.", parentTable.name(), childTable.name());
        }

        if (!parentHoldsForeignKey.isEmpty()) {
            var foreignKeyColumn = parentTable.column(parentHoldsForeignKey.get(0).columnName());
            var foreignKeyValue = parentRecord.get(foreignKeyColumn.name());
            if (foreignKeyValue == null) {
                return null;
            }
            return selectByPrimaryKey(childTable, foreignKeyValue);
        }

        if (!childHoldsForeignKey.isEmpty()) {
            var rows = selectChildrenByForeignKey(parentTable, parentRecord, childTable,
                childHoldsForeignKey.get(0).columnName());
            if (rows.size() > 1) {
                throw DLCPersistenceException.fail(
                    "More than 1 row was fetched via fk reference '%s'! But on entity side this was modelled " +
                        "as 1-1 relation!", childHoldsForeignKey.get(0).columnName());
            }
            return rows.isEmpty() ? null : rows.get(0);
        }

        throw DLCPersistenceException.fail(
            "No foreign key references between tables '%s' and '%s' were found! Self references (hierarchy) " +
                "are not supported!", parentTable.name(), childTable.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<JdbcRecord> getChildValueObjectRecordCollectionByParentRecord(
        JdbcRecord parentRecord, ValueObjectRecordMirror<JdbcRecord> vorm) {
        return fetchChildren(parentRecord, vorm.recordTypeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<JdbcRecord> getEntityReferenceRecordCollectionByParentRecord(
        JdbcRecord parentRecord, String referencedEntityClassName) {
        var childTableName = domainPersistenceProvider.entityRecordType(referencedEntityClassName);
        return fetchChildren(parentRecord, childTableName);
    }

    private Collection<JdbcRecord> fetchChildren(JdbcRecord parentRecord, String childTableName) {
        var parentTable = schemaMetadata.table(parentRecord.tableName());
        var childTable = schemaMetadata.table(childTableName);
        var matchingForeignKeys = childTable.foreignKeys().stream()
            .filter(fk -> fk.referencedTableName().equalsIgnoreCase(parentTable.name()))
            .toList();
        if (matchingForeignKeys.isEmpty()) {
            throw DLCPersistenceException.fail(
                "No foreign key references between tables '%s' and '%s' were found!",
                childTable.name(), parentTable.name());
        }
        if (matchingForeignKeys.size() > 1) {
            throw DLCPersistenceException.fail(
                "Exact foreign key reference between table '%s' and '%s' could not be identified! '%d' FK " +
                    "definitions could be found!", childTable.name(), parentTable.name(),
                matchingForeignKeys.size());
        }
        return selectChildrenByForeignKey(parentTable, parentRecord, childTable,
            matchingForeignKeys.get(0).columnName());
    }

    private List<JdbcRecord> selectChildrenByForeignKey(
        TableMetadata parentTable, JdbcRecord parentRecord, TableMetadata childTable, String foreignKeyColumnName) {
        var parentPkColumn = parentTable.column(requirePrimaryKeyName(parentTable));
        var parentPkValue = parentRecord.get(parentPkColumn.name());
        var foreignKeyColumn = childTable.column(foreignKeyColumnName);
        return JdbcRecordMapper.selectByColumn(connectionProvider, childTable, foreignKeyColumn.name(), parentPkValue);
    }

    private JdbcRecord selectByPrimaryKey(TableMetadata table, Object primaryKeyValue) {
        var pkColumn = table.column(requirePrimaryKeyName(table));
        var rows = JdbcRecordMapper.selectByColumn(connectionProvider, table, pkColumn.name(), primaryKeyValue);
        if (rows.size() > 1) {
            throw DLCPersistenceException.fail(
                "Find by ID: more than one row found for primary key value '%s' in table '%s'.",
                primaryKeyValue, table.name());
        }
        return rows.isEmpty() ? null : rows.get(0);
    }

    private String requirePrimaryKeyName(TableMetadata table) {
        var pkName = table.primaryKeyName();
        if (pkName == null) {
            throw DLCPersistenceException.fail("Table '%s' has no primary key defined.", table.name());
        }
        return pkName;
    }
}

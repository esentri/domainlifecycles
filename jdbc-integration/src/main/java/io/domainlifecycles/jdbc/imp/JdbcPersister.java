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

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.ColumnMetadata;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.repository.persister.BasePersister;
import io.domainlifecycles.persistence.repository.persister.Persister;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Plain JDBC based implementation of a {@link Persister}.
 * <p>
 * Every {@code JdbcRecord} known to this persister carries the full set of mapped column values (there is no
 * per-field "changed" tracking as jOOQ's generated records have), so every {@code UPDATE} this persister
 * issues is already a full-column update - unlike the jOOQ based implementation, no separate code path is
 * needed for {@link #doIncreaseVersion(JdbcRecord)}: it is identical to {@link #doUpdate(JdbcRecord)}.
 * <p>
 * Optimistic locking is implemented explicitly (jOOQ does this internally via its {@code recordVersionFields}
 * codegen option, keyed to the same physical column name used here): for any table that has a column named
 * {@code CONCURRENCY_VERSION}, {@code UPDATE}/{@code DELETE} statements add {@code AND concurrency_version =
 * <the value the record was mapped with>} to their {@code WHERE} clause, and a successful update also bumps
 * that column to {@code +1} in the {@code SET} clause (and in the in-memory record afterward, so that {@link
 * BasePersister#adaptChangesFromRecordToEntity} picks up the new value). Zero affected rows is reported as an
 * optimistic locking conflict via {@link DLCPersistenceException}, mirroring what jOOQ's own {@code
 * DataChangedException} signals at the same point.
 *
 * @author Mario Herb
 */
public class JdbcPersister extends BasePersister<JdbcRecord> implements Persister<JdbcRecord> {

    private static final String CONCURRENCY_VERSION_COLUMN_NAME = "CONCURRENCY_VERSION";

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcDialect dialect;
    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcPersister}.
     *
     * @param domainPersistenceProvider the persistence provider used to resolve entity record mirrors, and
     *                                  supplying the connection, dialect and schema metadata registered
     *                                  centrally on it
     */
    public JdbcPersister(JdbcDomainPersistenceProvider domainPersistenceProvider) {
        super(domainPersistenceProvider,
            new JdbcValueObjectIdProvider(domainPersistenceProvider),
            new JdbcEntityParentReferenceProvider(domainPersistenceProvider)
        );
        this.connectionProvider = Objects.requireNonNull(domainPersistenceProvider.connectionProvider);
        this.dialect = Objects.requireNonNull(domainPersistenceProvider.dialect);
        this.schemaMetadata = Objects.requireNonNull(domainPersistenceProvider.schemaMetadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doInsert(JdbcRecord record) {
        var table = schemaMetadata.table(record.tableName());
        var concurrencyColumn = table.findColumn(CONCURRENCY_VERSION_COLUMN_NAME).orElse(null);
        if (concurrencyColumn != null && record.has(concurrencyColumn.name())) {
            // mirrors jOOQ's recordVersionFields codegen option: on INSERT, the initial version value is
            // always 1, regardless of whatever value the in-memory entity carried beforehand - adapted back
            // onto the entity afterward via BasePersister#adaptChangesFromRecordToEntity
            record.set(concurrencyColumn.name(), 1L);
        }
        var values = record.values();
        var columnNames = new ArrayList<>(values.keySet());
        var sql = dialect.insertSql(table, columnNames);
        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            int index = 1;
            for (var columnName : columnNames) {
                bindValue(statement, index++, table.column(columnName), values.get(columnName));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Insert into '%s' failed.", e, table.name());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doUpdate(JdbcRecord record) {
        executeVersionCheckedUpdate(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDelete(JdbcRecord record) {
        var table = schemaMetadata.table(record.tableName());
        var pkColumn = primaryKeyColumn(table);
        var values = record.values();
        var concurrencyColumn = table.findColumn(CONCURRENCY_VERSION_COLUMN_NAME).orElse(null);
        var versionChecked = concurrencyColumn != null && values.containsKey(concurrencyColumn.name());

        var sql = dialect.deleteSql(
            table, pkColumn.name(), versionChecked ? concurrencyColumn.name() : null, versionChecked);

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setObject(1, values.get(pkColumn.name()));
            if (versionChecked) {
                statement.setObject(2, values.get(concurrencyColumn.name()));
            }
            int affected = statement.executeUpdate();
            if (affected == 0) {
                throw optimisticLockConflict(table.name(), values.get(pkColumn.name()));
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Delete from '%s' failed.", e, table.name());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * See the class documentation: since every {@link JdbcRecord} update is already a full-column update,
     * this needs no different behaviour than {@link #doUpdate(JdbcRecord)}.
     */
    @Override
    protected void doIncreaseVersion(JdbcRecord record) {
        executeVersionCheckedUpdate(record);
    }

    private void executeVersionCheckedUpdate(JdbcRecord record) {
        var table = schemaMetadata.table(record.tableName());
        var pkColumn = primaryKeyColumn(table);
        var values = record.values();
        var concurrencyColumn = table.findColumn(CONCURRENCY_VERSION_COLUMN_NAME).orElse(null);
        var versionChecked = concurrencyColumn != null && values.containsKey(concurrencyColumn.name());

        long oldVersion = 0;
        long newVersion = 0;
        if (versionChecked) {
            oldVersion = ((Number) values.get(concurrencyColumn.name())).longValue();
            newVersion = oldVersion + 1;
        }

        List<String> setColumns = values.keySet().stream()
            .filter(c -> !c.equals(pkColumn.name()))
            .filter(c -> !versionChecked || !c.equals(concurrencyColumn.name()))
            .toList();

        var sql = dialect.updateSql(
            table, setColumns, pkColumn.name(), versionChecked ? concurrencyColumn.name() : null, versionChecked);

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            int index = 1;
            for (var columnName : setColumns) {
                bindValue(statement, index++, table.column(columnName), values.get(columnName));
            }
            if (versionChecked) {
                statement.setObject(index++, newVersion);
            }
            statement.setObject(index++, values.get(pkColumn.name()));
            if (versionChecked) {
                statement.setObject(index, oldVersion);
            }
            int affected = statement.executeUpdate();
            if (affected == 0) {
                throw optimisticLockConflict(table.name(), values.get(pkColumn.name()));
            }
            if (versionChecked) {
                record.set(concurrencyColumn.name(), newVersion);
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Update of '%s' failed.", e, table.name());
        }
    }

    /**
     * Binds a single parameter, routing a {@code null} value through {@link PreparedStatement#setNull} with
     * the target column's own JDBC SQL type rather than {@link PreparedStatement#setObject}: an untyped
     * {@code null} leaves the driver to guess a default type for the parameter, and SQL Server's driver picks
     * one that it then refuses to implicitly convert into some column types (observed for {@code VARBINARY}
     * columns, raising {@code "Implicit conversion from data type nvarchar to varbinary is not allowed"})
     * even though the value being inserted is {@code null}.
     */
    private void bindValue(PreparedStatement statement, int index, ColumnMetadata column, Object value)
        throws SQLException {
        if (value == null) {
            statement.setNull(index, column.sqlType());
        } else {
            statement.setObject(index, value);
        }
    }

    private ColumnMetadata primaryKeyColumn(TableMetadata table) {
        var pkName = table.primaryKeyName();
        if (pkName == null) {
            throw DLCPersistenceException.fail("Table '%s' has no primary key defined.", table.name());
        }
        return table.column(pkName);
    }

    private DLCPersistenceException optimisticLockConflict(String tableName, Object primaryKeyValue) {
        return DLCPersistenceException.fail(
            "Optimistic locking conflict: row with primary key '%s' in table '%s' was not found or its "
                + "concurrency version no longer matched (it was concurrently modified or deleted).",
            primaryKeyValue, tableName);
    }
}

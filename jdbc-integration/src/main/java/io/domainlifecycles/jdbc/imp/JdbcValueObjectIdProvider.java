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
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.repository.persister.BaseValueObjectIdProvider;
import io.domainlifecycles.persistence.repository.persister.ValueObjectIdProvider;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

/**
 * Plain JDBC based implementation of a {@link ValueObjectIdProvider}.
 * <p>
 * Supports long-compatible technical ids (generated via a database sequence named {@code <TABLE_NAME>_SEQ})
 * as well as UUID based technical ids, regardless of their physical column representation: a native
 * {@code UUID} type, a {@code VARCHAR}/{@code CHAR} column (mapped to {@link String}), or a
 * {@code BINARY(16)} column (mapped to {@code byte[]}).
 * <p>
 * Mirrors {@code JooqValueObjectIdProvider}'s conventions exactly, substituting {@link JdbcSchemaMetadata}
 * for jOOQ's generated table metamodel.
 *
 * @author Mario Herb
 */
public class JdbcValueObjectIdProvider extends BaseValueObjectIdProvider<JdbcRecord> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcDialect dialect;
    private final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs a new instance of {@code JdbcValueObjectIdProvider}.
     *
     * @param domainPersistenceProvider the domain persistence provider used to resolve the technical id of an
     *                                  already persisted ancestor entity, and supplying the connection, dialect
     *                                  and schema metadata registered centrally on it
     */
    public JdbcValueObjectIdProvider(JdbcDomainPersistenceProvider domainPersistenceProvider) {
        super(domainPersistenceProvider);
        this.connectionProvider = Objects.requireNonNull(domainPersistenceProvider.connectionProvider);
        this.dialect = Objects.requireNonNull(domainPersistenceProvider.dialect);
        this.schemaMetadata = Objects.requireNonNull(domainPersistenceProvider.schemaMetadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContainerIdInNewVoRecord(JdbcRecord newVoRecord, Serializable containerTechId) {
        var column = table(newVoRecord).column("CONTAINER_ID");
        newVoRecord.set(column.name(), toPhysicalValue(containerTechId, column.javaType()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Serializable selectExistingTechIdOfValueObject(JdbcRecord voContainerRecord) {
        var column = table(voContainerRecord).column("ID");
        Object value = voContainerRecord.get(column.name());
        return normaliseToLogicalId(value, column.javaType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void provideNewTechIdForValueObjectRecord(JdbcRecord newVoRecord) {
        var table = table(newVoRecord);
        var pkColumnName = table.primaryKeyName();
        if (pkColumnName == null) {
            throw DLCPersistenceException.fail("Table '%s' has no primary key defined.", table.name());
        }
        var pkColumn = table.column(pkColumnName);

        if (isUuidCompatible(pkColumn.javaType())) {
            newVoRecord.set(pkColumn.name(), toPhysicalValue(UUID.randomUUID(), pkColumn.javaType()));
        } else if (isLongCompatible(pkColumn.javaType())) {
            var sequenceName = table.name() + "_SEQ";
            var newTechId = JdbcSequenceIdGenerator.nextValue(connectionProvider.getConnection(), dialect, sequenceName);
            newVoRecord.set(pkColumn.name(), newTechId);
        } else {
            throw DLCPersistenceException.fail(
                "Unsupported primary key type '%s' for table '%s'. "
                    + "Only long-compatible or UUID (uuid/VARCHAR/BINARY(16)) types are supported.",
                pkColumn.javaType().getName(), table.name());
        }
    }

    private TableMetadata table(JdbcRecord record) {
        return schemaMetadata.table(record.tableName());
    }

    // --- Value binding, aware of physical UUID representation ---------------------

    /**
     * Converts a logical id value into the physical representation expected by the column
     * ({@link UUID}, {@link String} or {@code byte[]}); any other value is passed through unchanged.
     */
    private Object toPhysicalValue(Serializable value, Class<?> physicalType) {
        if (value instanceof UUID uuid) {
            if (physicalType == UUID.class) {
                return uuid;
            } else if (physicalType == String.class) {
                return uuid.toString();
            } else if (physicalType == byte[].class) {
                return uuidToBytes(uuid);
            }
            throw DLCPersistenceException.fail(
                "Cannot bind UUID value to a column of type '%s'", physicalType.getName());
        }
        return value;
    }

    /**
     * Converts a physical id value into its logical Java representation. For UUID columns this yields a
     * {@link UUID}; for long-compatible columns the value is returned unchanged.
     */
    private Serializable normaliseToLogicalId(Object value, Class<?> physicalType) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (physicalType == String.class && value instanceof String s && looksLikeUuid(s)) {
            return UUID.fromString(s);
        }
        if (physicalType == byte[].class && value instanceof byte[] b && b.length == 16) {
            return bytesToUuid(b);
        }
        return (Serializable) value;
    }

    // --- Type helpers -------------------------------------------------------------

    private static boolean isLongCompatible(Class<?> type) {
        return type == Long.class
            || type == Integer.class
            || type == Short.class
            || type == Byte.class
            || type == BigInteger.class
            || type == long.class
            || type == int.class;
    }

    private static boolean isUuidCompatible(Class<?> type) {
        return type == UUID.class || type == String.class || type == byte[].class;
    }

    private static boolean looksLikeUuid(String s) {
        return s.length() == 36 && s.charAt(8) == '-' && s.charAt(13) == '-'
            && s.charAt(18) == '-' && s.charAt(23) == '-';
    }

    private static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private static UUID bytesToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long most = bb.getLong();
        long least = bb.getLong();
        return new UUID(most, least);
    }
}

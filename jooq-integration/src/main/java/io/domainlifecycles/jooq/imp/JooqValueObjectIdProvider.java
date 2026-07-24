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

package io.domainlifecycles.jooq.imp;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.persister.BaseValueObjectIdProvider;
import io.domainlifecycles.persistence.repository.persister.ValueObjectIdProvider;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Sequence;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

/**
 * jOOQ specific implementation of a {@link ValueObjectIdProvider}.
 *
 * <p>Supports long-compatible technical ids (generated via a database sequence)
 * as well as UUID based technical ids. UUIDs are supported regardless of their
 * physical column representation: a native {@code uuid} type (mapped to
 * {@link UUID}), a {@code VARCHAR}/{@code CHAR} column (mapped to
 * {@link String}) or a {@code BINARY(16)} column (mapped to {@code byte[]}).</p>
 *
 * @author Mario Herb
 */
public class JooqValueObjectIdProvider extends BaseValueObjectIdProvider<UpdatableRecord<?>> implements ValueObjectIdProvider<UpdatableRecord<?>> {

    private final DSLContext dslContext;

    /**
     * Constructs a new instance of JooqValueObjectIdProvider.
     *
     * @param domainPersistenceProvider the domain persistence provider responsible for managing
     *                                   persistence and retrieval of value object records.
     * @param dslContext                the DSL context used for constructing and executing SQL
     *                                   queries. This context must not be null.
     */
    public JooqValueObjectIdProvider(
        DomainPersistenceProvider<UpdatableRecord<?>> domainPersistenceProvider,
        DSLContext dslContext) {
        super(domainPersistenceProvider);
        this.dslContext = Objects.requireNonNull(dslContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContainerIdInNewVoRecord(UpdatableRecord<?> newVoRecord, Serializable containerTechId) {
        Field<?> f = containerIdField(newVoRecord);
        setValueForFieldType(newVoRecord, f, containerTechId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Serializable selectExistingTechIdOfValueObject(UpdatableRecord<?> voContainerRecord) {
        Field<?> f = idField(voContainerRecord);
        Object value = f.getValue(voContainerRecord);
        // Normalise any physical UUID representation to a UUID so that callers
        // (and the container-id side) work with a stable type.
        return normaliseToLogicalId(value, f.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void provideNewTechIdForValueObjectRecord(UpdatableRecord<?> newVoRecord) {
        Field<?> pk = newVoRecord.getTable().getPrimaryKey().getFields().get(0);
        Class<?> pkType = pk.getType();

        if (isUuidCompatible(pkType)) {
            UUID newId = UUID.randomUUID();
            setValueForFieldType(newVoRecord, pk, newId);
        } else if (isLongCompatible(pkType)) {
            provideNewSequenceId(newVoRecord, pk);
        } else {
            throw DLCPersistenceException.fail(
                "Unsupported primary key type '%s' for table '%s'. "
                    + "Only long-compatible or UUID (uuid/VARCHAR/BINARY(16)) types are supported.",
                pkType.getName(), newVoRecord.getTable().getName());
        }
    }

    // --- ID generation strategies -------------------------------------------------

    private void provideNewSequenceId(UpdatableRecord<?> newVoRecord, Field<?> pk) {
        try {
            Sequence<?> s = resolveSequence(newVoRecord);
            Long newTechId = dslContext.nextval(s).longValue();
            @SuppressWarnings("unchecked")
            Field<Object> objField = (Field<Object>) pk;
            newVoRecord.setValue(objField, newTechId);
        } catch (DataAccessException ex) {
            throw DLCPersistenceException.fail("Couldn't access sequence '%s_SEQ'", ex,
                newVoRecord.getTable().getName());
        }
    }

    private Sequence<?> resolveSequence(UpdatableRecord<?> newVoRecord) {
        Sequence<?> s = newVoRecord.getTable().getSchema()
            .getSequence(newVoRecord.getTable().getName() + "_SEQ");
        if (s == null) {
            s = newVoRecord.getTable().getSchema()
                .getSequence(newVoRecord.getTable().getName() + "_seq");
        }
        if (s == null) {
            throw DLCPersistenceException.fail(
                "Sequence '%s_SEQ' not found. Please create the sequence in your database!",
                newVoRecord.getTable().getName());
        }
        return s;
    }

    // --- Value binding, aware of physical UUID representation ---------------------

    /**
     * Sets {@code value} into {@code field}, converting a logical {@link UUID}
     * into the physical representation expected by the column
     * ({@link UUID}, {@link String} or {@code byte[]}).
     */
    @SuppressWarnings("unchecked")
    private void setValueForFieldType(UpdatableRecord<?> record, Field<?> field, Serializable value) {
        Class<?> fieldType = field.getType();
        Object physical;

        if (value instanceof UUID uuid) {
            if (fieldType == UUID.class) {
                physical = uuid;
            } else if (fieldType == String.class) {
                physical = uuid.toString();
            } else if (fieldType == byte[].class) {
                physical = uuidToBytes(uuid);
            } else {
                throw DLCPersistenceException.fail(
                    "Cannot bind UUID value to field '%s' of type '%s'",
                    field.getName(), fieldType.getName());
            }
        } else {
            // Long / numeric / already-matching type: hand over as-is and let
            // jOOQ coerce if needed.
            physical = value;
        }

        record.setValue((Field<Object>) field, physical);
    }

    /**
     * Converts a physical id value into its logical Java representation.
     * For UUID columns this yields a {@link UUID}; for long-compatible columns
     * the value is returned unchanged.
     */
    private Serializable normaliseToLogicalId(Object value, Class<?> fieldType) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID) {
            return (UUID) value;
        }
        if (fieldType == String.class && value instanceof String s && looksLikeUuid(s)) {
            return UUID.fromString(s);
        }
        if (fieldType == byte[].class && value instanceof byte[] b && b.length == 16) {
            return bytesToUuid(b);
        }
        return (Serializable) value;
    }

    // --- Field lookup helpers -----------------------------------------------------

    private Field<?> containerIdField(UpdatableRecord<?> record) {
        Field<?> f = record.getTable().field("CONTAINER_ID");
        if (f == null) {
            f = record.getTable().field("container_id");
        }
        if (f == null) {
            throw DLCPersistenceException.fail(
                "Record '%s' does not contain a field CONTAINER_ID", record);
        }
        return f;
    }

    private Field<?> idField(UpdatableRecord<?> record) {
        Field<?> f = record.getTable().field("ID");
        if (f == null) {
            f = record.getTable().field("id");
        }
        if (f == null) {
            throw DLCPersistenceException.fail(
                "Record '%s' does not contain a field ID", record);
        }
        return f;
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
        // Native uuid -> UUID, VARCHAR -> String, BINARY(16) -> byte[]
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

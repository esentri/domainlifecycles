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

package io.domainlifecycles.jdbc.records;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A generic record representation for plain JDBC persistence. In contrast to jOOQ, which generates one
 * dedicated {@code UpdatableRecord} subclass per database table, this single class represents the row of
 * any table: the table name identifies which table a given instance belongs to, and the column values are
 * held in an ordered name/value map.
 * <p>
 * The column names used as keys are the physical (database) column names, exactly as reported by
 * {@link java.sql.DatabaseMetaData}, not the camel-cased property names used on the domain side. Translating
 * between the two is the responsibility of the record property layer built on top of this class.
 *
 * @author Mario Herb
 */
public final class JdbcRecord {

    private final String tableName;

    private final Map<String, Object> values = new LinkedHashMap<>();

    /**
     * Creates a new, empty record for the given table.
     *
     * @param tableName the physical name of the table this record represents
     */
    public JdbcRecord(String tableName) {
        this.tableName = Objects.requireNonNull(tableName);
    }

    /**
     * Returns the physical name of the table this record represents.
     *
     * @return the table name
     */
    public String tableName() {
        return tableName;
    }

    /**
     * Returns the value stored for the given column.
     *
     * @param columnName the physical column name
     * @return the value, or {@code null} if not set
     */
    public Object get(String columnName) {
        return values.get(columnName);
    }

    /**
     * Sets the value for the given column.
     *
     * @param columnName the physical column name
     * @param value      the value to set
     */
    public void set(String columnName, Object value) {
        values.put(columnName, value);
    }

    /**
     * Returns whether a value has been explicitly set for the given column.
     *
     * @param columnName the physical column name
     * @return true, if a value was set for the column
     */
    public boolean has(String columnName) {
        return values.containsKey(columnName);
    }

    /**
     * Returns an immutable view of the column values currently held by this record, in insertion order.
     * <p>
     * Unlike {@link Map#copyOf(Map)}, this tolerates {@code null} values - entirely legitimate here for a
     * nullable database column - by snapshotting into a plain {@link LinkedHashMap} wrapped as unmodifiable,
     * rather than into a {@code Map.of(...)}-style immutable map, which throws {@link NullPointerException}
     * on any {@code null} value.
     *
     * @return the column name to value map
     */
    public Map<String, Object> values() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Records are compared structurally, by table name and column values, not by instance identity. The
     * fetcher's "already fetched this row" deduplication (see {@code SimpleFetcherContext}) relies on this to
     * recognize two separately mapped instances of the same physical row as equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JdbcRecord that)) {
            return false;
        }
        return tableName.equals(that.tableName) && values.equals(that.values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(tableName, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "JdbcRecord{" +
            "tableName='" + tableName + '\'' +
            ", values=" + values +
            '}';
    }
}

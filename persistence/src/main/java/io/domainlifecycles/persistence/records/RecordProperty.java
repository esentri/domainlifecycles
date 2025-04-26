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

package io.domainlifecycles.persistence.records;

import java.util.Objects;

/**
 * Represents a record property.
 *
 * @author Mario Herb
 */
public class RecordProperty {

    private final String name;
    private final String recordClassName;
    private final Class<?> propertyType;
    private final boolean primaryKey;
    private final boolean nonNullForeignKey;

    /**
     * Creates a new record property.
     *
     * @param name              the name
     * @param recordClassName   the record class
     * @param propertyType      the property type
     * @param primaryKey        the primary key
     * @param nonNullForeignKey the non-null foreign key
     */
    public RecordProperty(String name, String recordClassName,
                          Class<?> propertyType,
                          boolean primaryKey,
                          boolean nonNullForeignKey
    ) {
        this.name = name;
        this.recordClassName = Objects.requireNonNull(recordClassName);
        this.propertyType = Objects.requireNonNull(propertyType);
        this.primaryKey = primaryKey;
        this.nonNullForeignKey = nonNullForeignKey;
    }

    /**
     * Getter for the property name.
     *
     * @return the property name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the recordClassName.
     *
     * @return the record class name (full qualified name)
     */
    public String getRecordClassName() {
        return recordClassName;
    }

    /**
     * Getter for the property type.
     *
     * @return the property type
     */
    public Class<?> getPropertyType() {
        return propertyType;
    }

    /**
     * Whether the property is the primary key.
     *
     * @return true if the property is the primary key, false otherwise
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Whether the property is a non-null foreign key.
     *
     * @return true if the property is a non-null foreign key, false otherwise
     */
    public boolean isNonNullForeignKey() {
        return nonNullForeignKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordProperty that)) return false;
        return primaryKey == that.primaryKey &&
            name.equals(that.name) &&
            recordClassName.equals(that.recordClassName) &&
            propertyType.equals(that.propertyType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, recordClassName, propertyType, primaryKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RecordProperty{" +
            "name='" + name + '\'' +
            ", recordClassName=" + recordClassName +
            ", propertyType=" + propertyType +
            ", primaryKey=" + primaryKey +
            '}';
    }
}

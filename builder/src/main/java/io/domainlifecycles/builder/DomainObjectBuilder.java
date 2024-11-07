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

package io.domainlifecycles.builder;

import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.internal.DomainObject;

import java.util.Collection;

/**
 * Common interface for a builder which will be called by the DLC framework on
 * several occasions, e.g. Deserializing JSON input or upon loading aggregates
 * from the database.
 *
 * @param <T> the domain type to be built
 * @author Mario Herb
 * @author Dominik Galler
 */
public interface DomainObjectBuilder<T extends DomainObject> {

    /**
     * Final call to build the desired domain object, when all properties within
     * the builder are set.
     *
     * @return the new instance of the domain object (=entity or value object)
     */
    T build();

    /**
     * This method adds an object into a collection of the given field name.
     *
     * @param object    added
     * @param fieldName of field containing collection
     * @throws DLCBuilderException if the field does not hold a Collection
     */
    void addValueToCollection(Object object, String fieldName);

    /**
     * In case of a to-many reference this method provides a new container (java
     * collection instance) for the field with the given field name. If no
     * container could be provided an exception is thrown.
     *
     * @param fieldName of field containing collection
     * @return the new Collection instance for the given field
     * @throws DLCBuilderException if the field does not hold a Collection or if providing the value fails
     */
    Collection<DomainObject> newCollectionInstanceForField(String fieldName);

    /**
     * Sets a fields value for a given field name. If the field type
     * doesn't fit the type of given field value an exception is thrown.
     *
     * @param value     concrete object to be set in the builder for the
     *                  given field name
     * @param fieldName name of the field to be set
     */
    void setFieldValue(Object value, String fieldName);

    /**
     * Query if the builder is able to set a value for the given field.
     *
     * @param fieldName of field which should be set by the builder
     * @return true, if the builder can set a value for the given field, else
     * false
     */
    boolean canInstantiateField(String fieldName);

    /**
     * Queries the current field value set in the builder for the given
     * field name.
     *
     * @param fieldName the field name of the field being queried
     * @return the object instance that is currently set in the builder instance
     */
    Object getFieldValue(String fieldName);


    /**
     * Convenience method to obtain the primary identity value currently set
     * within the builder
     *
     * @param <ID> the type of the identity to be returned
     * @return current primary identity value
     */
    <ID extends Identity<?>> ID getPrimaryIdentity();

    /**
     * Convenience method to return the primary identity field name for the
     * instanceType assigned to the builder instance.
     *
     * @return the field name of the primary identity field of the
     * instanceType
     */
    String getPrimaryIdentityFieldName();

    /**
     * The instance type for this builder.
     *
     * @return instance type (domain object type)
     */
    Class<T> instanceType();

    /**
     * Returns the instance of the builder handled
     *
     * @return the instance of the builder handled
     */
    Object getBuilderInstance();
}

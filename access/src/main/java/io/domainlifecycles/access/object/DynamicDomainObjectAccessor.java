/*
 *
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

package io.domainlifecycles.access.object;

import io.domainlifecycles.domain.types.internal.DomainObject;

/**
 * General interface to access the values of a {@link DomainObject}.
 *
 * @author Mario Herb
 */
public interface DynamicDomainObjectAccessor {

    /**
     * @param fieldName of field value to be returned
     * @param <T>       type of returned value
     * @return value of field with the given name of the DomainObject assigned to the accessor.
     */
    <T> T peek(String fieldName);

    /**
     * Sets the argument in the field with the passed fieldName on the assigned
     * DomainObject.
     *
     * @param fieldName of field to be set
     * @param argument  value to be set
     */
    void poke(String fieldName, Object argument);

    /**
     * Assigns the DomainObject to be peeked or poked.
     *
     * @param domainObject (re-)assigned
     */
    void assign(DomainObject domainObject);

    /**
     * @return the currently assigned DomainObject to be peeked or poked.
     */
    DomainObject getAssigned();
}

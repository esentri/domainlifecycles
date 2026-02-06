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
 *  Copyright 2019-2025 the original author or authors.
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

import io.domainlifecycles.domain.types.ValueObject;

/**
 * Factory for creating single-valued value objects.
 *
 * @author Mario Herb
 */
public interface SingleValuedValueObjectFactory {

    /**
     * Creates a new instance of a value object of the specified type.
     *
     * @param value              the value to be encapsulated by the value object
     * @param valueObjectTypeName the fully qualified class name of the value object type
     * @param <V>                the type of the value
     * @param <VO>               the type of the value object, which must extend {@link ValueObject}
     * @return a new instance of the specified value object type encapsulating the provided value
     */
    <V, VO extends ValueObject> VO newInstance(V value, String valueObjectTypeName);
}

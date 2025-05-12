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

package io.domainlifecycles.mirror.api;

/**
 * The WildcardBound enum represents the type of bound for a wildcard in generics.
 * It is used to distinguish between the types of bounds that can be applied
 * to a wildcard generic in type parameter definitions.
 *
 * @author Mario Herb
 */
public enum WildcardBound {
    /**
     * Represents a lower-bound wildcard in generics, denoted by `? super T`.
     * The LOWER constant is used to indicate that a wildcard is restricted
     * by a lower bound, specifying a type or its superclasses.
     */
    LOWER,

    /**
     * Represents an upper-bound wildcard in generics, denoted by `? extends T`.
     * The UPPER constant is used to indicate that a wildcard is restricted
     * by an upper bound, specifying a type or its subclasses.
     */
    UPPER
}

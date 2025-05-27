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

package io.domainlifecycles.mirror.model;

/**
 * Not all kinds of Assertions are applicable everywhere.
 * The AssertionTypeGroup defines groups of Assertion applicability.
 *
 * @author Mario Herb
 */
public enum AssertionTypeGroup {
    /**
     * Represents the group of assertions applicable to String types.
     */
    STRING,
    /**
     * Represents the group of assertions applicable to temporal types such as dates and times.
     */
    TEMPORAL,
    /**
     * Represents the group of assertions applicable to numeric types.
     */
    NUMERIC,
    /**
     * Represents the group of assertions specifically applicable to fractional numeric types.
     * This includes assertions relevant for numbers with decimal places or fractional representations.
     */
    NUMERIC_FRACTIONAL,
    /**
     * Represents the group of assertions applicable to boolean types.
     * This type group is for assertions specifically intended
     * for evaluating boolean values.
     */
    BOOLEAN,
    /**
     * Represents the group of assertions applicable to general object types.
     * This type group is used for assertions specifically intended
     * to evaluate object-related properties and behaviors.
     */
    OBJECT,
    /**
     * Represents the group of assertions applicable to iterable types.
     * This type group is used for assertions specifically intended
     * to evaluate the properties and behaviors of iterable collections.
     */
    ITERABLE
}

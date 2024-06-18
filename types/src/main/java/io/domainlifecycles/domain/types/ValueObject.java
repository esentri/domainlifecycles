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

//    ____                _
//   |    \ ___ _____ ___|_|___
//   |  |  | . |     | .'| |   |
//  _|____/|___|_|_|_|__,|_|_|_|_
// |  |  |_|  _|___ ___ _ _ ___| |___ ___
// |  |__| |  _| -_|  _| | |  _| | -_|_ -|
// |_____|_|_| |___|___|_  |___|_|___|___|
//                     |___|
// Copyright (C) esentri.NitroX - All Rights Reserved.
//
// Unauthorized copying of this file, via any medium
// is strictly prohibited. Proprietary and confidential.

package io.domainlifecycles.domain.types;

import io.domainlifecycles.domain.types.internal.DomainObject;

/**
 * This is the common supertype (marker interface) to represent value objects.
 * <p>
 * Value objects represent typed values, that have no conceptual identity, in
 * the application domain. They can help you write better code that is less
 * error-prone, more performant and more expressive. Value objects have three
 * main characteristics:
 * <p><ul>
 * <li> Value Equality: Value objects are defined by their attributes. They are
 * equal if their attributes are equal. A value object differs from an entity in
 * that it does not have a concept of identity. For example, if we consider a
 * Duration as a value object, then a duration of 60 seconds would be the same
 * as a duration of one minute since the underlying value is the same. In Java,
 * we need to redefine the equals and hashCode methods, or consider implementing
 * Value Objects with Java Record classes
 * <li> Immutability: Once created, a value object should always be equal. The
 * only way to change its value is by full replacement. What this means, in code,
 * is to create a new instance with the new value.
 * <li> Self-Validation: A value object must verify the validity of its attributes
 * when being created. If any of its attributes are invalid, then the object should
 * not be created and an error or exception should be raised. For instance, if
 * we have a concept of Age it wouldn’t make sense to create an instance of age
 * with a negative value.
 * </ul><p>
 * <p>
 * Technically a value object is a shallowly immutable, transparent carrier for
 * a fixed set of values, called the value object components. In terms of
 * implementation, instances of a value object class
 * <p><ul>
 * <li> are final and immutable (though may contain references to mutable objects);
 * <li> have implementations of equals, hashCode, and toString which are computed
 * solely from the instance's state and not from its identity or the state of any
 * other object or variable;
 * <li> make no use of identity-sensitive operations such as reference equality
 * (==) between instances, identity hash code of instances, or synchronization
 * on an instances's intrinsic lock;
 * <li> are considered equal solely based on equals(), not based on reference
 * equality (==);
 * <li> are freely substitutable when equal, meaning that interchanging any two
 * instances x and y that are equal according to equals() in any computation or
 * method invocation should produce no visible change in behavior.
 * </ul><p>
 * A program may produce unpredictable results if it attempts to distinguish
 * two references to equal values of a value object class, whether directly via
 * reference equality or indirectly via an appeal to synchronization, identity
 * hashing,serialization, or any other identity-sensitive mechanism. Use of
 * such identity-sensitive operations on instances of value object classes
 * may have unpredictable effects and should be avoided.
 * <p>
 * For more information, read about
 * <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">
 * Value Objects</a>.
 * <p>
 * Consider implementing Value Objects with Java Record classes, as all necessary
 * implementation requirements are immediately addressed.
 *
 * @see java.lang.Record
 *
 * @author Mario Herb
 */
public interface ValueObject extends DomainObject, Validatable {
}

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

/**
 * This is the common supertype to represent user-defined identity types that
 * declare an external "identity", i.e. an instance agnostic identity decoupled
 * from Java's intrinsic object state.
 * <p>
 * Consider implementing user-defined identity types with Java Record classes,
 * as all necessary implementation requirements are immediately addressed.
 *
 * @param <V> type of identity value.
 * @see java.lang.Record
 *
 * @author Tobias Herb
 */
public interface Identity<V> extends Validatable{

    /**
     * Returns the value that identifies a thread of continuity.
     */
    V value();

    /// IDENTITY COMPANION

    /**
     * Returns the 'default' string representation of an identity object, which
     * is a string consisting of the name of the identity type, the {@literal @}
     * character, and the unsigned hexadecimal representation of the identity
     * hash code of the Java object and the identity value within square brackets.
     * In other words, this method returns a string equal to the value of:
     * <blockquote><pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode()) + '[' + value() + ']'
     * </pre></blockquote>
     *
     * @param id identity object whose string representation is returned
     * @return string representation of the given identity object
     */
    static String toString(final Identity<?> id) {
        return String.format("%s@%s(value=%s)", id.getClass().getName(), System.identityHashCode(id), id.value());
    }


    static boolean equals(final Identity<?> a, final Identity<?> b) {
        if (a == b) return true;
        if (a == null || b == null || a.getClass() != b.getClass()) {
            return false;
        }
        return a.value().equals(b.value());
    }
}

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

import io.domainlifecycles.reflect.JavaReflect;

import java.lang.reflect.Member;

/**
 * AccessLevel classifies the access level of fields or methods according to Java specification.
 *
 * @author Mario Herb
 */
public enum AccessLevel {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    PACKAGE;

    /**
     * Retrieve AccessLevel for a {@link Member}.
     *
     * @param member input
     * @return according access level
     */
    public static AccessLevel of(Member member) {
        if (JavaReflect.isPublic(member)) {
            return PUBLIC;
        } else if (JavaReflect.isProtected(member)) {
            return PROTECTED;
        } else if (JavaReflect.isPrivate(member)) {
            return PRIVATE;
        } else {
            return PACKAGE;
        }
    }
}

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

package io.domainlifecycles.reflect;

/**
 * This enum specifies the level of members selected by reflective operations
 * using {@link JavaReflect}.
 *
 * @author Tobias Herb
 */
public enum MemberSelect {

    /**
     * Select all declared members. This includes public, protected, default
     * (package) access, and private members, but excludes inherited members.
     * The order in which declared members are returned is unspecified.
     */
    DECLARED,

    /**
     * Select all public accessible members. This includes all public declared
     * members and all public members of superclasses and superinterfaces.
     */
    ACCESSIBLE,

    /**
     * Select all members that are visible relative to selection declaring.
     * This includes all public members of the selection declaring, including
     * all members of superclasses and superinterfaces, all protected members
     * of superclasses, and all default (package) access members which are
     * located in the same package as the selection declaring using same the
     * classloader.
     */
    VISIBLE,

    /**
     * Select all members declared in the selection declaring type hierarchy.
     * This includes all declared members of the selection declaring, including
     * all declared members of superclasses and superinterfaces.
     */
    HIERARCHY
}

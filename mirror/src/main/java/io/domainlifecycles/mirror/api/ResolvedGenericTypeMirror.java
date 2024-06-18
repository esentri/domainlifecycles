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

package io.domainlifecycles.mirror.api;

import java.util.List;
import java.util.Optional;

/**
 * ResolvedGenericTypeMirror represents a resolved generic type model.
 * It provides information about the Java type and any generic types it may have.
 * It also indicates if the type is a wildcard type with an upper or lower bound.
 *
 * @author Mario Herb
 */
public interface ResolvedGenericTypeMirror {

    /**
     * Retrieves the resolved type description of the generic type.
     *
     * @param shortTypeNames a boolean indicating whether to use short type names or not
     * @return a String representing the resolved type description
     */
    String resolvedTypeDescription(boolean shortTypeNames);

    /**
     * Retrieves the resolved type description only up to the first level generic type.
     *
     * @param shortTypeNames a boolean indicating whether to use short type names or not
     * @return a String representing the resolved type description
     */
    String resolvedTypeDescriptionFirstLevel(boolean shortTypeNames);

    /**
     * Retrieves the full qualified type name of the resolved generic type.
     *
     * @return the full qualified type name representing the type of the resolved generic type.
     */
    String typeName();

    /**
     * Retrieves a list of resolved generic types for this object.
     *
     * @return a List of ResolvedGenericTypeMirror objects representing the resolved generic types.
     */
    List<ResolvedGenericTypeMirror> genericTypes();

    /**
     * Retrieves an Optional that represents the wildcard bound of a resolved generic type.
     *
     * @return an Optional of type WildcardBound, which indicates if the type has an upper or lower bound.
     */
    Optional<WildcardBound> wildcardBound();

    /**
     * Method to check if the object is an array.
     *
     * @return true if the object is an array; otherwise, false.
     */
    boolean isArray();
}

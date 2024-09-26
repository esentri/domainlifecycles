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
 * The AssertedContainableTypeMirror reflects a Java type specification.
 *
 * This mirror provides information on specified Assertions (bean validation annotations).
 * as well as container types. java.langOptional, java.util.stream.Stream and java.util.Collection
 * are supported container types.
 *
 * @author Mario Herb
 */
public interface AssertedContainableTypeMirror {

    /**
     * @return the full qualified type name of the mirrored type.
     * Might be the full qualified name of the contained type, if the type is a container type.
     */
    String getTypeName();

    /**
     * @return the list of associated {@link AssertionMirror} instances, mirroring the corresponding assertions.
     */
    List<AssertionMirror> getAssertions();

    /**
     * @return the {@link DomainType} of the mirrored type.
     */
    DomainType getDomainType();

    /**
     * Query if the mirrored type is a container type.
     *
     * @return true, if the mirrored type is a container type.
     */
    default boolean hasContainer(){
        return hasOptionalContainer() || hasCollectionContainer() || hasStreamContainer();
    }

    /**
     * Query if the mirrored type is an optional type {@see java.util.Optional}.
     *
     * @return true, if the mirrored type is an optional type.
     */
    boolean hasOptionalContainer();

    /**
     * Query if the mirrored type is a collection type {@see java.util.Collection}.
     *
     * @return true, if the mirrored type is a collection type.
     */
    boolean hasCollectionContainer();

    /**
     * Query if the mirrored type is a set type {@see java.util.Set}.
     *
     * @return true, if the mirrored type is a set type.
     */
    boolean hasSetContainer();

    /**
     * Query if the mirrored type is a list type {@see java.util.List}.
     *
     * @return true, if the mirrored type is a list type.
     */
    boolean hasListContainer();

    /**
     * Query if the mirrored type is a stream type {@see java.util.Stream}.
     *
     * @return true, if the mirrored type is a java Stream.
     */
    boolean hasStreamContainer();

    /**
     * Query if the mirrored type is an array type {@see java.util.Stream}.
     *
     * @return true, if the mirrored type is a java array.
     */
    boolean isArray();

    /**
     * @return the full qualified container type name, if the mirrored type is a container type.
     * Otherwise returns empty.
     */
    Optional<String> getContainerTypeName();

    /**
     * @return the list of {@link AssertionMirror} instances associated with the container.
     */
    List<AssertionMirror> getContainerAssertions();

    /**
     * Retrieves the resolved generic type of the mirrored type.
     *
     * The resolved type provides information about the Java type and any generic types it may have.
     * It also indicates if the type is a wildcard type with an upper or lower bound.
     *
     * @return The resolved type of the mirrored type.
     */
    ResolvedGenericTypeMirror getResolvedGenericType();

}

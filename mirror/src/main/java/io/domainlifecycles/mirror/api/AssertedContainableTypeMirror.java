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

import java.util.List;
import java.util.Optional;

/**
 * The AssertedContainableTypeMirror reflects a Java type specification.
 * <p>
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
    default boolean hasContainer() {
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
     * Returns the JVM binary name (as returned by {@link Class#getName()}) of the type reflected by
     * {@link #getTypeName()}.
     * <p>
     * {@link #getTypeName()} always reports the component type of an array (e.g. {@code byte} for a
     * {@code byte[]} field), the array characteristic being exposed separately via {@link #isArray()}.
     * Callers that need a name which can be compared against {@link Class#getName()} or passed to
     * {@link Class#forName(String)} must therefore reassemble the array name. This method does that:
     * for a {@code byte[]} it returns {@code [B}, for a {@code java.lang.Byte[]} it returns
     * {@code [Ljava.lang.Byte;}.
     * <p>
     * For non array types the result is identical to {@link #getTypeName()}.
     *
     * @return the JVM binary name of the mirrored type
     */
    default String getBinaryTypeName() {
        if (!isArray()) {
            return getTypeName();
        }
        return "[" + jvmDescriptor(getTypeName());
    }

    /**
     * Maps a type name to its JVM type descriptor.
     *
     * @param typeName the type name to map, either a primitive name, a full qualified class name or an
     *                 array type name in binary ({@code [B}) or source ({@code byte[]}) notation
     * @return the JVM type descriptor
     */
    private static String jvmDescriptor(String typeName) {
        switch (typeName) {
            case "byte":
                return "B";
            case "short":
                return "S";
            case "int":
                return "I";
            case "long":
                return "J";
            case "char":
                return "C";
            case "float":
                return "F";
            case "double":
                return "D";
            case "boolean":
                return "Z";
            default:
                break;
        }
        if (typeName.startsWith("[")) {
            // already a binary array name, the component type of a multidimensional array
            return typeName;
        }
        if (typeName.endsWith("[]")) {
            // source notation of a multidimensional array component type
            return "[" + jvmDescriptor(typeName.substring(0, typeName.length() - 2));
        }
        return "L" + typeName + ";";
    }

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
     * <p>
     * The resolved type provides information about the Java type and any generic types it may have.
     * It also indicates if the type is a wildcard type with an upper or lower bound.
     *
     * @return The resolved type of the mirrored type.
     */
    ResolvedGenericTypeMirror getResolvedGenericType();

}

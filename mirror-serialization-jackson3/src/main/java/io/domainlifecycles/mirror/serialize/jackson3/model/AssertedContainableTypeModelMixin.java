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

package io.domainlifecycles.mirror.serialize.jackson3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;

import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertedContainableTypeModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertedContainableTypeModelMixin {

    /**
     * Indicates whether the associated model contains an optional container.
     * This flag is used during the serialization and deserialization process
     * to represent the presence of an {@code Optional}-like container in the model's structure.
     */
    @JsonProperty
    public boolean hasOptionalContainer;

    /**
     * Indicates whether the associated model contains a collection-based container.
     * This boolean flag is utilized during the serialization and deserialization
     * process to represent the presence of a collection-like container, such as a
     * {@code List} or {@code Set}, in the structure of the model.
     */
    @JsonProperty
    public boolean hasCollectionContainer;

    /**
     * Indicates whether the associated model contains a set-based container.
     * This boolean flag is utilized during the serialization and deserialization
     * process to represent the presence of a {@code Set}-like container in the
     * structure of the model.
     */
    @JsonProperty
    public boolean hasSetContainer;

    /**
     * Indicates whether the associated model contains a list-based container.
     * This flag is used during the serialization and deserialization process
     * to represent the presence of a {@code List}-like container in the structure
     * of the model.
     */
    @JsonProperty
    public boolean hasListContainer;

    /**
     * Indicates whether the associated model contains an array-based container.
     * This boolean flag is utilized in the serialization and deserialization
     * process to represent the presence of an array-like container in the
     * structure of the model.
     */
    @JsonProperty
    public boolean isArray;

    /**
     * Indicates whether the associated model contains a stream-based container.
     * This boolean flag is used during the serialization and deserialization
     * process to represent the presence of a {@code Stream}-like container in
     * the structure of the model.
     */
    @JsonProperty
    public boolean hasStreamContainer;

    /**
     * Constructs an instance of AssertedContainableTypeModelMixin. Used to control
     * the deserialization process of types that may be wrapped
     * in various container types such as Optional, List, Set, and Stream.
     *
     * @param typeName             The name of the type being modeled.
     * @param domainType           The domain type describing the type in a domain-driven design context.
     * @param assertions           A list of assertions mirrored for the type.
     * @param hasOptionalContainer Indicates if the type is wrapped in an Optional container.
     * @param hasCollectionContainer Indicates if the type is wrapped in a collection container.
     * @param hasListContainer      Indicates if the type is specifically wrapped in a List container.
     * @param hasSetContainer       Indicates if the type is specifically wrapped in a Set container.
     * @param hasStreamContainer    Indicates if the type is wrapped in a Stream container.
     * @param isArray               Indicates if the type is represented as an array.
     * @param containerTypeName     The name of the container type wrapping the type if present.
     * @param containerAssertions   A list of assertions mirrored for the container type.
     * @param resolvedGenericType   The resolved generic type information for the type.
     */
    @JsonCreator
    public AssertedContainableTypeModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("domainType") DomainType domainType,
        @JsonProperty("assertions") List<AssertionMirror> assertions,
        @JsonProperty("hasOptionalContainer") boolean hasOptionalContainer,
        @JsonProperty("hasCollectionContainer") boolean hasCollectionContainer,
        @JsonProperty("hasListContainer") boolean hasListContainer,
        @JsonProperty("hasSetContainer") boolean hasSetContainer,
        @JsonProperty("hasStreamContainer") boolean hasStreamContainer,
        @JsonProperty("isArray") boolean isArray,
        @JsonProperty("containerTypeName") String containerTypeName,
        @JsonProperty("containerAssertions") List<AssertionMirror> containerAssertions,
        @JsonProperty("resolvedGenericType") ResolvedGenericTypeMirror resolvedGenericType
    ) {}
}

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

package io.domainlifecycles.mirror.serialize.jackson2.model;

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
@Deprecated
public abstract class AssertedContainableTypeModelMixin {

    /**
     * Indicates whether the type has an optional container associated with it.
     * This property is primarily used to control serialization behavior in the
     * mixin class without modifying the actual model class.
     */
    @JsonProperty
    public boolean hasOptionalContainer;

    /**
     * Indicates whether the type has a collection container (e.g., List, Set, etc.)
     * associated with it. This property is primarily used to control serialization
     * behavior in conjunction with Jackson annotations.
     */
    @JsonProperty
    public boolean hasCollectionContainer;

    /**
     * Indicates whether the type has a Set container (e.g., Set) associated with it.
     * This property is primarily used to control serialization behavior in the
     * associated Jackson mixin class without making changes to the actual model class.
     */
    @JsonProperty
    public boolean hasSetContainer;

    /**
     * Indicates whether the type has a list container.
     *
     * This field is used in the context of serialization to signal the presence
     * of list-based containment in the associated type. It serves as a metadata
     * flag for deserialization and serialization logic.
     */
    @JsonProperty
    public boolean hasListContainer;

    /**
     * Indicates whether the type is an array.
     * This field is used to identify entities that are represented as arrays
     * in their data structure, allowing for proper serialization and deserialization.
     */
    @JsonProperty
    public boolean isArray;

    /**
     * Indicates whether the type is associated with a stream container.
     * This field is primarily used to control serialization and deserialization
     * when working with Jackson's JSON processing.
     */
    @JsonProperty
    public boolean hasStreamContainer;

    /**
     * Constructor for the AssertedContainableTypeModelMixin.
     * Controls deserialization without modifying the actual model class.
     *
     * @param typeName the name of the type
     * @param domainType the domain type
     * @param assertions the assertions
     * @param hasOptionalContainer whether the type has optional container
     * @param hasCollectionContainer whether the type has collection container
     * @param hasListContainer whether the type has list container
     * @param hasSetContainer whether the type has set container
     * @param hasStreamContainer whether the type has stream container
     * @param isArray whether the type is array
     * @param containerTypeName the name of the container type
     * @param containerAssertions the assertions of the container
     * @param resolvedGenericType the resolved generic type
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

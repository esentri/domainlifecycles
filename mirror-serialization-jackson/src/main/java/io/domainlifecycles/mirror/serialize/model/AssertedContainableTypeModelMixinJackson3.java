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

package io.domainlifecycles.mirror.serialize.model;

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
public abstract class AssertedContainableTypeModelMixinJackson3 {

    @JsonProperty
    public boolean hasOptionalContainer;

    @JsonProperty
    public boolean hasCollectionContainer;

    @JsonProperty
    public boolean hasSetContainer;

    @JsonProperty
    public boolean hasListContainer;

    @JsonProperty
    public boolean isArray;

    @JsonProperty
    public boolean hasStreamContainer;

    @JsonCreator
    public AssertedContainableTypeModelMixinJackson3(
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

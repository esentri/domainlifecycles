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

package io.domainlifecycles.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model implementation of an {@link AssertedContainableTypeMirror}.
 *
 * @author Mario Herb
 */
public class AssertedContainableTypeModel implements AssertedContainableTypeMirror {
    private final String typeName;
    private final DomainType domainType;
    private final List<AssertionMirror> assertions;
    @JsonProperty
    private final boolean hasOptionalContainer;
    @JsonProperty
    private final boolean hasCollectionContainer;
    @JsonProperty
    private final boolean hasSetContainer;
    @JsonProperty
    private final boolean hasListContainer;
    @JsonProperty
    private final boolean isArray;
    @JsonProperty
    private final boolean hasStreamContainer;
    private final String containerTypeName;
    private final List<AssertionMirror> containerAssertions;
    private final ResolvedGenericTypeMirror resolvedGenericType;

    @JsonCreator
    public AssertedContainableTypeModel(@JsonProperty("typeName") String typeName,
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
                                        @JsonProperty("resolvedGenericType") ResolvedGenericTypeMirror resolvedGenericType) {
        this.typeName = Objects.requireNonNull(typeName);
        this.domainType = Objects.requireNonNull(domainType);
        Objects.requireNonNull(assertions);
        this.assertions = Collections.unmodifiableList(assertions);
        this.hasOptionalContainer = hasOptionalContainer;
        this.hasCollectionContainer = hasCollectionContainer;
        this.hasListContainer = hasListContainer;
        this.hasSetContainer = hasSetContainer;
        this.hasStreamContainer = hasStreamContainer;
        this.isArray = isArray;
        this.containerTypeName = containerTypeName;
        Objects.requireNonNull(containerAssertions);
        this.containerAssertions = Collections.unmodifiableList(containerAssertions);
        this.resolvedGenericType = resolvedGenericType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return typeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssertionMirror> getAssertions() {
        return assertions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return domainType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasOptionalContainer() {
        return hasOptionalContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCollectionContainer() {
        return hasCollectionContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasListContainer() {
        return hasListContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSetContainer() {
        return hasSetContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasStreamContainer() {
        return hasStreamContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isArray() {
        return isArray;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getContainerTypeName() {
        return Optional.ofNullable(containerTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssertionMirror> getContainerAssertions() {
        return containerAssertions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror getResolvedGenericType() {
        return resolvedGenericType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AssertedContainableTypeModel{" +
            "typeName='" + typeName + '\'' +
            ", domainType=" + domainType +
            ", assertions=" + assertions +
            ", hasOptionalContainer=" + hasOptionalContainer +
            ", hasCollectionContainer=" + hasCollectionContainer +
            ", hasListContainer=" + hasListContainer +
            ", hasSetContainer=" + hasSetContainer +
            ", hasStreamContainer=" + hasStreamContainer +
            ", isArray=" + isArray +
            ", containerTypeName='" + containerTypeName + '\'' +
            ", containerAssertions=" + containerAssertions +
            ", resolvedGenericType=" + resolvedGenericType +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssertedContainableTypeModel that = (AssertedContainableTypeModel) o;
        return hasOptionalContainer == that.hasOptionalContainer && hasCollectionContainer == that.hasCollectionContainer && hasListContainer == that.hasListContainer && hasSetContainer == that.hasSetContainer && hasStreamContainer == that.hasStreamContainer && isArray == that.isArray && typeName.equals(
            that.typeName) && domainType == that.domainType && assertions.equals(that.assertions) && Objects.equals(
            containerTypeName, that.containerTypeName) && containerAssertions.equals(
            that.containerAssertions) && Objects.equals(resolvedGenericType, that.resolvedGenericType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(typeName, domainType, assertions, hasOptionalContainer, hasCollectionContainer,
            hasListContainer, hasSetContainer, hasStreamContainer, isArray, containerTypeName, containerAssertions,
            resolvedGenericType);
    }
}

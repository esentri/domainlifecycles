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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Optional;

/**
 * Jackson mixin for {@link io.domainlifecycles.mirror.model.RepositoryModel}.
 * Controls JSON serialization/deserialization of repository metadata without touching the domain model class.
 *
 * @author leonvoellinger
 */
public abstract class RepositoryModelMixin extends ServiceKindModelMixin {

    /**
     * Serialized type name of the managed aggregate; written as a plain string field in JSON.
     */
    @JsonProperty
    public String managedAggregateTypeName;

    /**
     * Fully qualified type names of repository interfaces as they should appear in JSON.
     */
    @JsonProperty
    public List<String> repositoryInterfaceTypeNames;

    /**
     * Constructor used by Jackson to rebuild repository metadata from JSON.
     * Expects the provided property names to match the JSON payload.
     *
     * @param typeName name of the repository type from the {@code typeName} JSON field
     * @param isAbstract flag from {@code abstract} indicating whether the repository is abstract
     * @param allFields serialized field mirrors supplied via {@code allFields}
     * @param methods serialized method mirrors supplied via {@code methods}
     * @param managedAggregateTypeName JSON field {@code managedAggregateTypeName} with the aggregate type name
     * @param repositoryInterfaceTypeNames JSON field {@code repositoryInterfaceTypeNames} with all interface type names
     * @param inheritanceHierarchyTypeNames hierarchy type names from {@code inheritanceHierarchyTypeNames}
     * @param allInterfaceTypeNames interface type names from {@code allInterfaceTypeNames}
     */
    @JsonCreator
    public RepositoryModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("managedAggregateTypeName") String managedAggregateTypeName,
        @JsonProperty("repositoryInterfaceTypeNames") List<String> repositoryInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves the managed aggregate root mirror associated with this repository model.
     * This method is ignored by Jackson during serialization/deserialization to ensure
     * domain metadata is excluded from JSON representations.
     *
     * @return an {@code Optional} containing the {@link AggregateRootMirror} if one is managed,
     *         or an empty {@code Optional} if no managed aggregate is associated.
     */
    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getManagedAggregate();

    /**
     * Retrieves the domain type of the mirrored class associated with this repository model.
     * Ignored during serialization/deserialization.
     *
     * @return the {@link DomainType} of the mirrored class, representing its role in the domain.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

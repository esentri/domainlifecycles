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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.RepositoryModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class RepositoryModelMixin extends ServiceKindModelMixin {

    /**
     * Represents the name of the managed aggregate type associated with the repository.
     * Used for serialization and deserialization with Jackson.
     *
     * This field is intended to specify the domain entity type that defines the aggregate root
     * managed by the repository.
     */
    @JsonProperty
    public String managedAggregateTypeName;

    /**
     * Represents a list of repository interface type names associated with the
     * repository model. Each entry in the list corresponds to a repository interface
     * type that the repository model implements or relies on.
     *
     * This field is primarily used for serialization and deserialization purposes
     * to capture or propagate type hierarchy information in a structured form.
     */
    @JsonProperty
    public List<String> repositoryInterfaceTypeNames;

    /**
     * Constructor for the {@code RepositoryModelMixin} class.
     * Allows the creation and configuration of a mixin for the {@code RepositoryModel},
     * utilized for deserialization purposes using Jackson.
     *
     * @param typeName The name of the repository model type.
     * @param isAbstract Defines whether the repository model is abstract.
     * @param allFields A list of all fields that are part of the repository model.
     *                  Each field is represented by a {@link FieldMirror}.
     * @param methods A list of all methods that are part of the repository model.
     *                Each method is represented by a {@link MethodMirror}.
     * @param managedAggregateTypeName The name of the managed aggregate type associated with the repository.
     * @param repositoryInterfaceTypeNames A list of repository interface type names implemented by the repository model.
     * @param inheritanceHierarchyTypeNames A list of type names representing the inheritance hierarchy
     *                                       of the repository model.
     * @param allInterfaceTypeNames A list of all interface type names associated with the repository model.
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
     * Retrieves an {@link Optional} containing the associated {@link AggregateRootMirror},
     * which conceptually represents the managed aggregate root at the type level.
     * If the managed aggregate is not present, an empty {@link Optional} is returned.
     *
     * @return an {@link Optional} containing the managed {@link AggregateRootMirror},
     *         or an empty {@link Optional} if no managed aggregate is associated.
     */
    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getManagedAggregate();

    /**
     * Retrieves the {@link DomainType} of the current model instance, which defines
     * the specific type classification within the domain-driven design (DDD) context.
     * Ignored during serialization and deserialization.
     *
     * @return the {@link DomainType} representing the type of the current model instance.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

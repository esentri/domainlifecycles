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
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AggregateRootModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AggregateRootModelMixin extends EntityModelMixin {

    /**
     * Constructor of the {@code AggregateRootModelMixin}, used to customize JSON serialization and deserialization
     * for instances of {@link io.domainlifecycles.mirror.model.AggregateRootModel}.
     *
     * @param typeName the fully qualified name of the aggregate root model type.
     * @param isAbstract specifies whether the model type is abstract.
     * @param allFields a list of {@link FieldMirror} instances representing all fields in the model type.
     * @param methods a list of {@link MethodMirror} instances representing all methods in the model type.
     * @param identityField an {@link Optional} containing the {@link FieldMirror} that represents the identity field of the model, if one exists.
     * @param concurrencyVersionField an {@link Optional} containing the {@link FieldMirror} of the concurrency version field, if one exists.
     * @param inheritanceHierarchyTypeNames a list of fully qualified names of types in the inheritance hierarchy for the model type.
     * @param allInterfaceTypeNames a list of fully qualified names of all interfaces implemented by the model type.
     */
    @JsonCreator
    public AggregateRootModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("identityField") Optional<FieldMirror> identityField,
        @JsonProperty("concurrencyVersionField") Optional<FieldMirror> concurrencyVersionField,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, identityField, concurrencyVersionField, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves the domain type of the implementing class. Ignored for serialization purposes.
     *
     * @return the {@link DomainType} representing the classification of the implementing class
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

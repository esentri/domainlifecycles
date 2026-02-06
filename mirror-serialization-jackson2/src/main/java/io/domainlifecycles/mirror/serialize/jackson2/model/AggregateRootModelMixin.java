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
@Deprecated
public abstract class AggregateRootModelMixin extends EntityModelMixin {

    /**
     * Constructor for the {@code AggregateRootModelMixin} class used to manage JSON serialization
     * and deserialization of aggregate root model objects without altering the original model class.
     *
     * @param typeName the name of the type represented by the aggregate root model
     * @param isAbstract flag indicating whether the type is abstract
     * @param allFields the list of all fields mirrored in the aggregate root model
     * @param methods the list of all methods mirrored in the aggregate root model
     * @param identityField the optional field representing the identity attribute of the aggregate root model
     * @param concurrencyVersionField the optional field used for managing concurrency versioning in the aggregate root model
     * @param inheritanceHierarchyTypeNames the list of type names representing the inheritance hierarchy of the aggregate root model
     * @param allInterfaceTypeNames the list of interface type names implemented by the aggregate root model
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
     * Mixin method declaration. Ignored for serialization purposes.
     * @return the {@link DomainType}
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

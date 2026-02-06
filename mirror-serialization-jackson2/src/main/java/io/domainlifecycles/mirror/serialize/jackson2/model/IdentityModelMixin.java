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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.IdentityModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class IdentityModelMixin extends DomainTypeModelMixin {

    /**
     * Constructor for IdentityModelMixin, utilized to manage
     * deserialization of IdentityModel while preserving its intended behavior.
     *
     * @param typeName the name of the identity model type.
     * @param isAbstract a boolean flag indicating if the identity model type is abstract.
     * @param allFields the list of all fields contained in the identity model, represented by {@link FieldMirror}.
     * @param methods the list of all methods defined in the identity model, represented by {@link MethodMirror}.
     * @param valueTypeName an {@link Optional} containing the fully qualified name of the value type, if applicable.
     * @param inheritanceHierarchyTypeNames the list of fully qualified type names representing the
     *        inheritance hierarchy of the identity model.
     * @param allInterfaceTypeNames the list of fully qualified type names representing all the interfaces
     *        implemented by the identity model.
     */
    @JsonCreator
    public IdentityModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("valueTypeName") Optional<String> valueTypeName,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves the domain type associated with the implementing model.
     * Ignored for serialization purposes.
     *
     * @return the {@link DomainType} representing the classification of the model
     *         within the DDD context
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

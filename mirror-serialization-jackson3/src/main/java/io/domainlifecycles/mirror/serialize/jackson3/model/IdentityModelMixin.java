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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.IdentityModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class IdentityModelMixin extends DomainTypeModelMixin {

    /**
     * Constructs an instance of IdentityModelMixin with the provided properties.
     * Used to control deserialization without modifying the actual model class.
     *
     * @param typeName the name of the type being mirrored.
     * @param isAbstract a boolean indicating if the type is abstract.
     * @param allFields a list of {@link FieldMirror} instances representing all fields in the mirrored type.
     * @param methods a list of {@link MethodMirror} instances representing all methods in the mirrored type.
     * @param valueTypeName an {@link Optional} containing the value type name, if applicable.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy.
     * @param allInterfaceTypeNames a list of type names representing all interfaces implemented by the mirrored type.
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
     * Retrieves the domain type associated with the model. Ignored for serialization purposes.
     *
     * @return the {@link DomainType} representing the specific type of the domain model,
     *         such as AGGREGATE_ROOT, ENTITY, VALUE_OBJECT, or NON_DOMAIN, among others.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

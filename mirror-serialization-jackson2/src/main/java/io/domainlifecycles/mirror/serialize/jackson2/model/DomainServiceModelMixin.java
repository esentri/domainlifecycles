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

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class DomainServiceModelMixin extends ServiceKindModelMixin {

    /**
     * Constructs a {@code DomainServiceModelMixin} instance, serving as a Jackson Mix-in
     * to define custom deserialization for {@code DomainServiceModel}.
     *
     * @param typeName the name of the domain service type.
     * @param isAbstract a boolean indicating whether the domain service model is abstract.
     * @param allFields a list of {@code FieldMirror} objects representing all fields of the domain service.
     * @param methods a list of {@code MethodMirror} objects representing all methods of the domain service.
     * @param domainServiceInterfaceTypeNames a list of strings representing the names of all domain service interfaces implemented by the type.
     * @param inheritanceHierarchyTypeNames a list of strings representing the inheritance hierarchy of the domain service type.
     * @param allInterfaceTypeNames a list of strings representing all interface type names associated with the domain service type.
     */
    @JsonCreator
    public DomainServiceModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("domainServiceInterfaceTypeNames") List<String> domainServiceInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return the {@link DomainType} associated with the domain service.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

}

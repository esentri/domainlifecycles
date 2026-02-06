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

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainServiceModelMixin extends ServiceKindModelMixin {

    /**
     * Constructor for the DomainServiceModelMixin, used to control the deserialization and deserialization
     * of {@link io.domainlifecycles.mirror.model.DomainServiceModel}.
     *
     * @param typeName the name of the domain service model type.
     * @param isAbstract a flag indicating whether the domain service model type is abstract.
     * @param allFields the list of all fields present in the domain service model,
     *                  represented by {@link FieldMirror}.
     * @param methods the list of all methods present in the domain service model,
     *                represented by {@link MethodMirror}.
     * @param domainServiceInterfaceTypeNames the list of fully qualified type names representing
     *                                         the domain service interfaces implemented by the domain service model.
     * @param inheritanceHierarchyTypeNames the list of fully qualified type names representing the
     *                                      inheritance hierarchy of the domain service model type.
     * @param allInterfaceTypeNames the list of fully qualified type names representing
     *                              all interfaces implemented by the domain service model type.
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
     * Retrieves the domain type associated with the current model.
     * Ignored for serialization purposes.
     *
     * @return the {@link DomainType} representation of the current model.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

    /**
     * Retrieves a list of fully qualified type names representing the domain service
     * interfaces implemented by the domain service model.Ignored for serialization purposes.
     *
     * @return a list of String objects, each representing the fully qualified name
     *         of a domain service interface associated with the model.
     */
    @JsonIgnore
    public abstract List<String> getDomainServiceInterfaceTypeNames();
}

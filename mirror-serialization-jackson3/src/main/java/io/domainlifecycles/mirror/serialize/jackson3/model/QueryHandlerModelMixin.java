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
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.QueryHandlerModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class QueryHandlerModelMixin extends ServiceKindModelMixin {

    /**
     * Represents the type name of the read model that is explicitly provided.
     * This field is used in the context of serialization for configuring
     * or overriding the read model type associated with a query handler.
     */
    @JsonProperty
    public String providedReadModelTypeName;

    /**
     * Defines a list of type names representing interfaces implemented by
     * the query handler. This information is used during serialization to
     * capture and retain details about the implemented interfaces.
     */
    @JsonProperty
    public List<String> queryHandlerInterfaceTypeNames;

    /**
     * Constructor for the {@code QueryHandlerModelMixin}, used to control
     * the deserialization process for the {@code QueryHandlerModel} class.
     *
     * @param typeName The type name of the query handler model.
     * @param isAbstract Indicates whether the query handler model is abstract.
     * @param allFields A list of {@link FieldMirror} instances representing all fields in the query handler.
     * @param methods A list of {@link MethodMirror} instances representing all methods in the query handler.
     * @param queryHandlerInterfaceTypeNames A list of type names for interfaces implemented by the query handler.
     * @param inheritanceHierarchyTypeNames A list of type names representing the inheritance hierarchy of the query handler.
     * @param allInterfaceTypeNames A list of all interface type names implemented by the query handler.
     * @param providedReadModelTypeName The type name of the explicitly provided read model associated with the query handler.
     */
    @JsonCreator
    public QueryHandlerModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("queryHandlerInterfaceTypeNames") List<String> queryHandlerInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames,
        @JsonProperty("providedReadModelTypeName") String providedReadModelTypeName
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }
}

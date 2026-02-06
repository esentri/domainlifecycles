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
@Deprecated
public abstract class QueryHandlerModelMixin extends ServiceKindModelMixin {

    /**
     * Represents the name of the type of the provided read model associated with this instance.
     * This field is serialized and deserialized in JSON representation using Jackson.
     * It is part of the {@code QueryHandlerModel} serialization and deserialization process.
     */
    @JsonProperty
    public String providedReadModelTypeName;

    /**
     * Represents a list of fully qualified type names of interfaces implemented by a specific query handler.
     * This field is part of the serialization and deserialization process using Jackson.
     * It is used to capture or provide metadata about the implemented interfaces of the query handler within
     * the context of the {@code QueryHandlerModel}.
     */
    @JsonProperty
    public List<String> queryHandlerInterfaceTypeNames;

    /**
     * Constructs a new instance of the QueryHandlerModelMixin. Used for Jackson deserialization purposes.
     *
     * @param typeName the fully qualified name of the type this mixin models.
     * @param isAbstract indicates if the underlying type is abstract.
     * @param allFields a list of {@link FieldMirror} instances representing all fields in the type.
     * @param methods a list of {@link MethodMirror} instances representing all methods in the type.
     * @param queryHandlerInterfaceTypeNames a list of fully qualified names of the interfaces the query handler implements.
     * @param inheritanceHierarchyTypeNames a list of fully qualified names representing the inheritance hierarchy of the type.
     * @param allInterfaceTypeNames a list of fully qualified names of all interfaces implemented by the type.
     * @param providedReadModelTypeName the fully qualified name of the type of the provided read model associated with this query handler.
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

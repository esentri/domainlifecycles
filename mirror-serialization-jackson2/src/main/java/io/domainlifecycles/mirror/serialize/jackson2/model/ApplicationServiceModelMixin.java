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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ApplicationServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class ApplicationServiceModelMixin extends ServiceKindModelMixin {

    @JsonProperty
    private List<String> applicationServiceInterfaceTypeNames;

    /**
     * Constructor for the ApplicationServiceModelMixin.
     * Controls deserialization without modifying the actual model class.
     *
     * @param typeName the name of the application service
     * @param isAbstract whether the application service is abstract
     * @param allFields list of fields declared in the application service
     * @param methods list of methods declared in the application service
     * @param applicationServiceInterfaceTypeNames list of interface types implemented by the application service
     * @param inheritanceHierarchyTypeNames list of types in the inheritance hierarchy of the application service
     * @param allInterfaceTypeNames list of all interface types implemented by the application service
     */
    @JsonCreator
    public ApplicationServiceModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("applicationServiceInterfaceTypeNames") List<String> applicationServiceInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return the {@link DomainType}
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of interface types implemented by the application service
     */
    @JsonIgnore
    public abstract List<String> getApplicationServiceInterfaceTypeNames();
}

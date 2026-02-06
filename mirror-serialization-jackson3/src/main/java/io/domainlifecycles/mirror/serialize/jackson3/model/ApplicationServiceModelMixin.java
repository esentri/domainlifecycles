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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ApplicationServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class ApplicationServiceModelMixin extends ServiceKindModelMixin {

    @JsonProperty
    private List<String> applicationServiceInterfaceTypeNames;

    /**
     * Constructor for the ApplicationServiceModelMixin, used to control the serialization
     * and deserialization of {@link io.domainlifecycles.mirror.model.ApplicationServiceModel}.
     *
     * @param typeName the name of the application service type.
     * @param isAbstract a flag indicating whether the application service type is abstract.
     * @param allFields the list of all fields present in the application service type, represented by {@link FieldMirror}.
     * @param methods the list of all methods present in the application service type, represented by {@link MethodMirror}.
     * @param applicationServiceInterfaceTypeNames the list of fully qualified type names representing
     *        the application service interfaces implemented by the type.
     * @param inheritanceHierarchyTypeNames the list of fully qualified type names representing
     *        the inheritance hierarchy of the application service type.
     * @param allInterfaceTypeNames the list of fully qualified type names representing
     *        all interfaces implemented by the application service type.
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
     * Retrieves the {@link DomainType} associated with the application service model.
     * Ignored for serialization purposes.
     *
     * @return the domain type categorizing the application service model
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

    /**
     * Retrieves a list of fully qualified type names representing the application
     * service interfaces implemented by the model. This method is ignored during
     * serialization.
     *
     * @return a list of strings, where each string is the fully qualified name of
     *         an application service interface implemented by the model
     */
    @JsonIgnore
    public abstract List<String> getApplicationServiceInterfaceTypeNames();
}

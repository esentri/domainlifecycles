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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.OutboundServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class OutboundServiceModelMixin extends ServiceKindModelMixin {

    /**
     * Constructor for the OutboundServiceModelMixin class.
     * This mixin is used to define custom deserialization
     * behaviors for the OutboundServiceModel in Jackson, without modifying
     * the original model class.
     *
     * @param typeName the name of the type being represented.
     * @param isAbstract boolean indicating whether the type is abstract.
     * @param allFields a list of all fields in the mirrored type, represented as {@link FieldMirror} instances.
     * @param methods a list of methods in the mirrored type, represented as {@link MethodMirror} instances.
     * @param outboundServiceInterfaceTypeNames a list of fully qualified type names representing the outbound service interfaces implemented by the mirrored type.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance hierarchy of the mirrored type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented by the mirrored type.
     */
    @JsonCreator
    public OutboundServiceModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("outboundServiceInterfaceTypeNames") List<String> outboundServiceInterfaceTypeNames,
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

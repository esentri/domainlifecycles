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
import io.domainlifecycles.mirror.api.EnumOptionMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.EnumModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class EnumModelMixin extends DomainTypeModelMixin {

    /**
     * Constructs an instance of the EnumModelMixin class. This constructor is used
     * to define properties for deserialization of an enum model
     * using Jackson annotations. Instances of this class represent metadata about
     * enums, including their fields, methods, options, and hierarchy information.
     *
     * @param typeName the name of the type represented by this enum model.
     * @param isAbstract a boolean flag indicating whether the type is abstract.
     * @param allFields the list of fields associated with the enum model.
     * @param methods the list of methods associated with the enum model.
     * @param enumOptions the list of enum options associated with the enum model.
     * @param inheritanceHierarchyTypeNames the list of type names representing the
     *        inheritance hierarchy of this enum model.
     * @param allInterfaceTypeNames the list of type names representing all
     *        interfaces implemented by this enum model.
     */
    @JsonCreator
    public EnumModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("enumOptions") List<EnumOptionMirror> enumOptions,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves the domain type of the associated model. Ignored for serialization purposes.
     *
     * @return the {@code DomainType} representing the type of the domain object
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

}

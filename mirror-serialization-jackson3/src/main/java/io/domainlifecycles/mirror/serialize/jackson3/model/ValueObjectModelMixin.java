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
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ValueObjectModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class ValueObjectModelMixin extends DomainObjectModelMixin {

    /**
     * Constructor for the {@code ValueObjectModelMixin}. Controls deserialization without modifying the actual model class.
     *
     * @param typeName the type name of the value object model.
     * @param isAbstract indicates whether the value object model is abstract.
     * @param allFields the list of all fields associated with the value object model.
     * @param methods the list of methods associated with the value object model.
     * @param inheritanceHierarchyTypeNames the list of type names in the inheritance hierarchy of the value object model.
     * @param allInterfaceTypeNames the list of all interface type names implemented by the value object model.
     */
    @JsonCreator
    public ValueObjectModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return true if the model has a single-valued field, false otherwise
     */
    @JsonIgnore
    public abstract boolean isSingledValued();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return fields
     */
    @JsonIgnore
    public abstract Optional<FieldMirror> singledValuedField();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return DomainType of the value object model
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

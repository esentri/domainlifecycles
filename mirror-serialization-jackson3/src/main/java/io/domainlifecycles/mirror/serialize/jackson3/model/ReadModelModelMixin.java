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
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import java.util.List;

    /**
     * Jackson mixin for {@link io.domainlifecycles.mirror.model.ReadModelModel}.
     * Controls JSON shape (property names, type info) without touching the model class.
     *
     * @author leonvoellinger
     */
    public abstract class ReadModelModelMixin extends DomainTypeModelMixin {

        /**
         * Constructs a new instance of the {@code ReadModelModelMixin} class. This mixin is used
         * to configure Jackson deserialization for read models with specific properties.
         *
         * @param typeName the name of the type represented by this model.
         * @param isAbstract indicates whether the type is abstract.
         * @param allFields a list of {@link FieldMirror} objects representing all fields associated with the type.
         * @param methods a list of {@link MethodMirror} objects representing all methods associated with the type.
         * @param inheritanceHierarchyTypeNames a list of type names in the inheritance hierarchy of the type.
         * @param allInterfaceTypeNames a list of all interface type names implemented by the type.
         */
        @JsonCreator
        public ReadModelModelMixin(
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
         * Retrieves a list of basic fields for the associated read model.
         * This method is ignored during JSON serialization, meaning its output
         * is not included in the JSON representation of the object.
         *
         * @return a list of {@link FieldMirror} objects representing the basic fields of the read model.
         */
        @JsonIgnore
        public abstract List<FieldMirror> getBasicFields();

        /**
         * Retrieves a list of value reference mirrors associated with the corresponding model.
         * This method is ignored during JSON serialization, so its output will not appear
         * in the JSON representation of the object.
         *
         * @return a list of {@link ValueReferenceMirror} objects representing references to values
         *         mirrored by a {@link ValueMirror}.
         */
        @JsonIgnore
        public abstract List<ValueReferenceMirror> getValueReferences();

        /**
         * Retrieves a list of entity reference mirrors associated with the corresponding model.
         * This method is ignored during JSON serialization, so its output will not appear
         * in the JSON representation of the object.
         *
         * @return a list of {@link EntityReferenceMirror} objects representing references to entities.
         */
        @JsonIgnore
        public abstract List<EntityReferenceMirror> getEntityReferences();

        /**
         * Retrieves a list of aggregate root reference mirrors associated with the corresponding model.
         * This method is ignored during JSON serialization and its output will not appear
         * in the JSON representation of the object.
         *
         * @return a list of {@link AggregateRootReferenceMirror} instances representing references
         *         to aggregate root objects mirrored in the model.
         */
        @JsonIgnore
        public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

        /**
         * Retrieves the domain type associated with the model.
         * This method is ignored during JSON serialization, so its output will
         * not appear in the JSON representation of the object.
         *
         * @return the {@link DomainType} of the model, representing its
         *         categorization within a domain-driven design (DDD) context.
         */
        @JsonIgnore
        public abstract DomainType getDomainType();
    }

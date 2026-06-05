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
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.EntityModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class EntityModelMixin extends DomainObjectModelMixin {

    /**
     * Constructor for the {@code EntityModelMixin} class.
     * Enables the deserialization of {@code EntityModel} objects with specific fields
     * and behavior using Jackson annotations.
     *
     * @param typeName the name of the type represented by this entity model
     * @param isAbstract indicates whether this entity model represents an abstract type
     * @param allFields the list of fields represented in the entity model
     * @param methods the list of methods represented in the entity model
     * @param identityField an optional field representing the identity field of the entity
     * @param concurrencyVersionField an optional field representing the concurrency version field of the entity
     * @param inheritanceHierarchyTypeNames the list of type names in the inheritance hierarchy of the entity
     * @param allInterfaceTypeNames the list of all interface type names implemented by the entity
     */
    @JsonCreator
    public EntityModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("identityField") Optional<FieldMirror> identityField,
        @JsonProperty("concurrencyVersionField") Optional<FieldMirror> concurrencyVersionField,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves a list of entity references associated with this model. Ignored for serialization purposes.
     *
     * @return a list of {@code EntityReferenceMirror} instances representing the references to entities.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves a list of aggregate root references associated with this model. Ignored for serialization purposes.
     *
     * @return a list of {@code AggregateRootReferenceMirror} instances representing
     *         references to aggregate roots.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves a list of value references associated with this model. Ignored for serialization purposes.
     *
     * @return a list of {@code ValueReferenceMirror} instances representing the references to values.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves a list of basic fields associated with this model. Ignored for serialization purposes.
     *
     * @return a list of {@code FieldMirror} instances representing the basic fields of the entity.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves a list of {@code DomainCommandMirror} instances that represent the domain commands
     * processed within the context of this entity model. Each {@code DomainCommandMirror} provides
     * details about the mirrored domain command, including its references, fields, and associated
     * behaviors. Ignored for serialization purposes.
     *
     * @return a list of {@code DomainCommandMirror} instances representing the processed
     *         domain commands in this entity model.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> processedDomainCommands();

    /**
     * Retrieves a list of {@code DomainEventMirror} instances representing the domain events
     * that have been published within the context of this entity model. Ignored for serialization purposes.
     *
     * @return a list of {@code DomainEventMirror} instances representing the published domain events.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> publishedDomainEvents();

    /**
     * Retrieves a list of {@code DomainEventMirror} instances that represent the domain events
     * this entity is configured to listen to. Ignored for serialization purposes.
     *
     * @return a list of {@code DomainEventMirror} instances representing the domain events
     *         that are listened to in the context of this entity model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> listenedDomainEvents();

    /**
     * Retrieves the type of the domain associated with this model.
     * Ignored for serialization purposes.
     *
     * @return the {@code DomainType} instance representing the type of the domain.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

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
public abstract class EntityModelMixin extends DomainObjectModelMixin {

    /**
     * Constructs an instance of EntityModelMixin which serves as a Jackson Mixin
     * for {@link io.domainlifecycles.mirror.model.EntityModel}.
     * It is used to control the deserialization of entity models
     * without modifying the actual model class.
     *
     * @param typeName the name of the entity type being mirrored.
     * @param isAbstract a boolean indicating whether the entity type is abstract.
     * @param allFields a list of {@link FieldMirror} that mirrors all fields of the entity type.
     * @param methods a list of {@link MethodMirror} that mirrors all methods of the entity type.
     * @param identityField an {@link Optional} of {@link FieldMirror} representing the identity field of the entity, if present.
     * @param concurrencyVersionField an {@link Optional} of {@link FieldMirror} representing the concurrency version field, if present.
     * @param inheritanceHierarchyTypeNames a list of strings representing the names of the inheritance hierarchy for the entity type.
     * @param allInterfaceTypeNames a list of strings representing all interface types implemented by the entity type.
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
     * Retrieves a list of entity references associated with the model. This provides
     * metadata about references to other entity types within the domain model. The method
     * is marked with {@code @JsonIgnore} to exclude it from serialization.
     *
     * @return a list of {@code EntityReferenceMirror} representing the entity references.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves a list of references to aggregate root entities associated with the model.
     * This provides metadata about dependencies or relationships to aggregate roots within
     * the domain model. The method is marked with {@code @JsonIgnore} to exclude it from
     * serialization.
     *
     * @return a list of {@code AggregateRootReferenceMirror} representing the aggregate
     *         root references within the entity model.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves a list of value references associated with the model.
     * This method provides metadata about references to values within
     * the domain model. The method is marked with {@code @JsonIgnore}
     * to exclude it from serialization.
     *
     * @return a list of {@code ValueReferenceMirror} representing the
     *         value references within the entity model.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves a list of basic fields associated with the entity model.
     * This method provides metadata about fundamental fields of the entity.
     * The method is marked with {@code @JsonIgnore} to exclude it from serialization.
     *
     * @return a list of {@code FieldMirror} representing the basic fields of the entity model.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves a list of processed domain commands associated with the entity model.
     * This method provides metadata about domain commands that have been processed
     * within the lifecycle of the domain model. The method is marked with {@code @JsonIgnore}
     * to exclude it from serialization.
     *
     * @return a list of {@code DomainCommandMirror} representing the processed
     *         domain commands within the entity model.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> processedDomainCommands();

    /**
     * Retrieves a list of domain events that have been published by the entity model.
     * This method provides metadata about events emitted as part of the domain model's lifecycle.
     * The method is marked with {@code @JsonIgnore} to exclude it from serialization.
     *
     * @return a list of {@code DomainEventMirror} representing the domain events published by the entity model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> publishedDomainEvents();

    /**
     * Retrieves a list of domain events that the entity model listens to.
     * This method provides metadata about the domain events that the entity
     * is subscribed to as part of its lifecycle. The method is marked with
     * {@code @JsonIgnore} to exclude it from serialization.
     *
     * @return a list of {@code DomainEventMirror} representing the domain
     *         events that the entity model listens to.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> listenedDomainEvents();

    /**
     * Retrieves the domain type of the entity model.
     * This provides metadata about the type of the domain object
     * being represented by the model. The method is marked
     * with {@code @JsonIgnore} to exclude it from serialization.
     *
     * @return the {@code DomainType} representing the domain object type.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

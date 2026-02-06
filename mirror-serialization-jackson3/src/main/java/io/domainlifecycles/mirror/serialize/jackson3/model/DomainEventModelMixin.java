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
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainEventModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainEventModelMixin extends DomainTypeModelMixin {

    /**
     * Constructs a new instance of the DomainEventModelMixin. Used to customize deserialization.
     *
     * @param typeName The name of the domain event type.
     * @param isAbstract Indicates if the domain event is abstract.
     * @param allFields A list of all fields mirrored by the domain event.
     * @param methods A list of all methods mirrored by the domain event.
     * @param inheritanceHierarchyTypeNames A list of fully qualified names representing the inheritance hierarchy.
     * @param allInterfaceTypeNames A list of fully qualified names of all interfaces implemented by the domain event.
     */
    @JsonCreator
    public DomainEventModelMixin(
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
     * Retrieves a list of basic fields represented as {@link FieldMirror} objects.
     * This method provides access to a subset of fields associated with the domain event.
     * Ignored during serialization.
     *
     * @return a list of {@link FieldMirror} instances that represent the basic fields.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves a list of value references represented as {@code ValueReferenceMirror} objects.
     * This method provides access to all value references associated with the domain event.
     * Ignored during serialization.
     *
     * @return a list of {@code ValueReferenceMirror} instances representing the value references.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves a list of entity references represented as {@code EntityReferenceMirror} objects.
     * This method provides access to all entity references associated with the domain event.
     * Ignored during serialization.
     *
     * @return a list of {@code EntityReferenceMirror} instances representing the entity references.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves a list of aggregate root references represented as {@code AggregateRootReferenceMirror} objects.
     * This method provides access to all references to aggregate roots associated with the domain event.
     * Ignored during serialization.
     *
     * @return a list of {@code AggregateRootReferenceMirror} instances representing the aggregate root references.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves a list of aggregate roots represented as {@code AggregateRootMirror} objects.
     * This method provides access to all aggregate roots that are involved in publishing
     * domain events. Ignored during serialization.
     *
     * @return a list of {@code AggregateRootMirror} instances representing the aggregate roots
     *         associated with publishing domain events.
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getPublishingAggregates();

    /**
     * Retrieves a list of domain services represented as {@code DomainServiceMirror} objects.
     * This method provides access to all domain services involved in publishing domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code DomainServiceMirror} instances representing the domain services
     *         associated with publishing domain events.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getPublishingDomainServices();

    /**
     * Retrieves a list of repository mirrors represented as {@code RepositoryMirror} objects.
     * This method provides access to all repositories involved in publishing domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code RepositoryMirror} instances representing the repositories
     *         associated with publishing domain events.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getPublishingRepositories();

    /**
     * Retrieves a list of aggregate roots represented as {@code AggregateRootMirror} objects.
     * This method provides access to all aggregate roots that are involved in listening
     * to domain events. Ignored during serialization.
     *
     * @return a list of {@code AggregateRootMirror} instances that represent the aggregate roots
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getListeningAggregates();

    /**
     * Retrieves a list of domain services represented as {@code DomainServiceMirror} objects.
     * This method provides access to all domain services that are involved in listening to domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code DomainServiceMirror} instances representing the domain services
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getListeningDomainServices();

    /**
     * Retrieves a list of repository mirrors represented as {@code RepositoryMirror} objects.
     * This method provides access to all repositories that are involved in listening to domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code RepositoryMirror} instances representing the repositories
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getListeningRepositories();

    /**
     * Retrieves a list of application services represented as {@code ApplicationServiceMirror} objects.
     * This method provides access to all application services that are involved in listening to domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code ApplicationServiceMirror} instances representing the application services
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getListeningApplicationServices();

    /**
     * Retrieves a list of outbound services represented as {@code OutboundServiceMirror} objects.
     * This method provides access to all outbound services that are involved in listening to domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code OutboundServiceMirror} instances representing the outbound services
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getListeningOutboundServices();

    /**
     * Retrieves a list of query handlers represented as {@code QueryHandlerMirror} objects.
     * This method provides access to all query handlers that are involved in listening to domain events.
     * Ignored during serialization.
     *
     * @return a list of {@code QueryHandlerMirror} instances representing the query handlers
     *         associated with listening to domain events.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getListeningQueryHandlers();

    /**
     * Retrieves a list of service kinds that are being listened to. Ignored during serialization.
     *
     * @return a list of ServiceKindMirror objects representing the kinds of services being monitored.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getListeningServiceKinds();

    /**
     * Retrieves a list of application service mirrors that are published by the application.
     * Ignored during serialization.
     *
     * @return a list of {@link ApplicationServiceMirror} objects representing the publishing application services
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getPublishingApplicationServices();

    /**
     * Retrieves the list of outbound services that are designated for publishing.
     * Ignored during serialization.
     *
     * @return a list of {@code OutboundServiceMirror} objects representing the outbound services available for publishing
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getPublishingOutboundServices();

    /**
     * Retrieves a list of query handler mirrors that are registered for publishing queries.
     * Ignored during serialization.
     *
     * @return a list of {@code QueryHandlerMirror} objects representing the query handlers configured for publishing.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getPublishingQueryHandlers();

    /**
     * Retrieves a list of service kinds that are available for publishing.
     * Ignored during serialization.
     *
     * @return a list of ServiceKindMirror objects representing the publishing service kinds.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getPublishingServiceKinds();

    /**
     * Retrieves the domain type associated with the implementing class.
     * Ignored during serialization.
     *
     * @return the {@code DomainType} representing the type of the domain for the given implementation.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

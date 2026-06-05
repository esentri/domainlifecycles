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
@Deprecated
public abstract class DomainEventModelMixin extends DomainTypeModelMixin {

    /**
     * Constructor for the DomainEventModelMixin class, used for customizing the
     * Jackson deserialization of domain event model objects.
     *
     * @param typeName the name of the domain event type being serialized or deserialized
     * @param isAbstract a boolean indicating whether the domain event is abstract
     * @param allFields a list of FieldMirror objects representing all the fields of the domain event
     * @param methods a list of MethodMirror objects representing all the methods of the domain event
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance hierarchy of the domain event
     * @param allInterfaceTypeNames a list of fully qualified type names representing all the implemented interfaces of the domain event
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
     * Retrieves a list of basic fields represented by {@link FieldMirror} objects.
     * Ignored for serialization purposes.
     *
     * @return a list of {@link FieldMirror} objects representing the basic fields.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves a list of value references represented by {@link ValueReferenceMirror} objects.
     * Ignored during serialization.
     *
     * @return a list of {@link ValueReferenceMirror} objects representing value references in the domain event model.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves a list of entity references represented by {@link EntityReferenceMirror} objects.
     * Ignored during serialization.
     *
     * @return a list of {@link EntityReferenceMirror} objects representing entity references in the domain event model.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves a list of aggregate root references represented by {@link AggregateRootReferenceMirror} objects.
     * Ignored during serialization.
     *
     * @return a list of {@link AggregateRootReferenceMirror} objects that represent
     *         references to aggregate root instances in the domain event model.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves a list of publishing aggregates represented by {@link AggregateRootMirror} objects.
     * Ignored for serialization purposes.
     *
     * @return a list of {@link AggregateRootMirror} objects representing the publishing aggregates
     *         in the domain event model.
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getPublishingAggregates();

    /**
     * Retrieves a list of domain service mirrors represented by {@link DomainServiceMirror} objects.
     * This list indicates the domain services that are being published within the domain event model.
     * Ignored during serialization.
     *
     * @return a list of {@link DomainServiceMirror} objects representing the publishing domain services
     *         in the domain event model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getPublishingDomainServices();

    /**
     * Retrieves a list of publishing repositories represented by {@link RepositoryMirror} objects.
     * This list contains the repositories being published within the domain event model.
     * Ignored during serialization.
     *
     * @return a list of {@link RepositoryMirror} objects representing the publishing repositories
     *         in the domain event model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getPublishingRepositories();

    /**
     * Retrieves a list of listening aggregates represented by {@link AggregateRootMirror} objects.
     * Ignored during serialization.
     *
     * @return a list of {@link AggregateRootMirror} objects representing the aggregates
     *         that are listening for domain events in the domain event model.
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getListeningAggregates();

    /**
     * Retrieves a list of domain service mirrors represented by {@link DomainServiceMirror} objects.
     * This list indicates the domain services that are listening for domain events
     * within the domain event model.
     * Ignored during serialization.
     *
     * @return a list of {@link DomainServiceMirror} objects representing the listening domain services
     *         in the domain event model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getListeningDomainServices();

    /**
     * Retrieves a list of listening repositories represented by {@link RepositoryMirror} objects.
     * This list contains the repositories that are listening for domain events
     * within the domain event model.
     * Ignored during serialization.
     *
     * @return a list of {@link RepositoryMirror} objects representing the listening repositories
     *         in the domain event model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getListeningRepositories();

    /**
     * Retrieves a list of application services that are configured to listen for domain events
     * within the domain event model. Each application service is represented by an
     * {@link ApplicationServiceMirror} object.
     * Ignored for serialization purposes.
     *
     * @return a list of {@link ApplicationServiceMirror} objects representing the application
     *         services listening for domain events in the domain event model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getListeningApplicationServices();

    /**
     * Retrieves a list of outbound services that are configured to listen for domain events
     * within the domain event model. Each outbound service is represented by an {@link OutboundServiceMirror} object.
     * Ignored during serialization.
     *
     * @return a list of {@link OutboundServiceMirror} objects representing outbound services
     *         listening for domain events in the domain event model.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getListeningOutboundServices();

    /**
     * Retrieves a list of query handlers that are configured to listen for domain events
     * within the domain event model. Each query handler is represented by a {@link QueryHandlerMirror} object.
     * Ignored during serialization.
     *
     * @return a list of {@link QueryHandlerMirror} objects representing the query handlers
     *         listening for domain events in the domain event model.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getListeningQueryHandlers();

    /**
     * Retrieves a list of service kinds represented by {@link ServiceKindMirror} objects.
     * This list contains the service kinds that are configured to listen for domain events
     * within the domain event model. Ignored during serialization.
     *
     * @return a list of {@link ServiceKindMirror} objects representing the service kinds
     *         listening for domain events in the domain event model.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getListeningServiceKinds();

    /**
     * Retrieves a list of publishing application services associated with the current context.
     * Ignored during serialization.
     *
     * @return a list of ApplicationServiceMirror objects representing the publishing application services
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getPublishingApplicationServices();

    /**
     * Retrieves a list of publishing outbound services represented by
     * {@code OutboundServiceMirror} objects. Ignored during serialization.
     *
     * @return a list of {@code OutboundServiceMirror} instances representing
     *         the publishing outbound services.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getPublishingOutboundServices();

    /**
     * Retrieves a list of query handler mirrors that are designated for publishing.
     * Ignored during serialization.
     *
     * @return a list of QueryHandlerMirror objects representing the publishing query handlers.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getPublishingQueryHandlers();

    /**
     * Retrieves a list of ServiceKindMirror instances that represent the kinds of services being published.
     * Ignored during serialization.
     *
     * @return a list of ServiceKindMirror objects representing the publishing service kinds.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getPublishingServiceKinds();

    /**
     * Retrieves the domain type associated with the current instance.
     * Ignored during serialization.
     *
     * @return the domain type as a DomainType object.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

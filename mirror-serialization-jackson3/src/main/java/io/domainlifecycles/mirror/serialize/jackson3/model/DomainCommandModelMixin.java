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
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainCommandModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainCommandModelMixin extends DomainTypeModelMixin {

    /**
     * Represents an optional string property that specifies the type name
     * of the aggregate target's identity. This property is used to define
     * the identity type of the aggregate target in the domain model.
     *
     * Annotated with {@code @JsonProperty} to enable serialization and
     * deserialization using Jackson. The use of {@code Optional<String>}
     * allows for the absence of a value, making it explicit that this field
     * is not mandatory.
     */
    @JsonProperty
    public Optional<String> aggregateTargetIdentityTypeName;

    /**
     * Represents an optional string property that specifies the type name of the
     * target domain service. This property is used to define the identity or
     * classification of the domain service being referenced in the domain model.
     *
     * Annotated with {@code @JsonProperty} to enable serialization and deserialization
     * using Jackson. The use of {@code Optional<String>} indicates that this property
     * is not mandatory and explicitly allows for the absence of a value.
     */
    @JsonProperty
    public Optional<String> domainServiceTargetTypeName;

    /**
     * Constructs a new instance of the DomainCommandModelMixin class. Uses Jackson annotations
     * to define the structure and behavior of the command model during serialization and deserialization.
     *
     * @param typeName the name of the type represented by this command model.
     * @param isAbstract whether the type is abstract.
     * @param allFields the list of all fields associated with the command model.
     * @param methods the list of methods associated with the command model.
     * @param aggregateTargetIdentityTypeName an optional type name representing the target identity type in the aggregate context.
     * @param domainServiceTargetTypeName an optional type name representing the target type in the domain service context.
     * @param inheritanceHierarchyTypeNames the list of type names in the inheritance hierarchy.
     * @param allInterfaceTypeNames the list of all interface type names implemented by this command model.
     */
    @JsonCreator
    public DomainCommandModelMixin(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("aggregateTargetIdentityTypeName") Optional<String> aggregateTargetIdentityTypeName,
        @JsonProperty("domainServiceTargetTypeName") Optional<String> domainServiceTargetTypeName,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * Retrieves the basic fields associated with the domain command model.
     * This typically includes fields that are fundamental to the representation
     * of a domain command and are not filtered out by more specialized criteria.
     * Ignored during serialization.
     *
     * @return a list of {@link FieldMirror} objects representing the basic fields
     *         of the domain command model.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves the value references associated with the domain command model.
     * Value references typically represent references to values or objects
     * mirrored in the domain context. Ignored during serialization.
     *
     * @return a list of {@link ValueReferenceMirror} objects representing the value
     *         references in the domain command model. Each value reference provides
     *         detailed metadata about a referenced value.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves the entity references associated with the domain command model.
     * Entity references typically represent references to entities within
     * the domain context. Ignored during serialization.
     *
     * @return a list of {@code EntityReferenceMirror} objects that provide metadata
     *         about the referenced entities in the domain command model.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves the aggregate root references associated with the domain command model.
     * Aggregate root references typically represent references to root entities in an
     * aggregate context within the domain.
     * Ignored during serialization.
     *
     * @return a list of {@code AggregateRootReferenceMirror} objects representing the aggregate
     *         root references in the domain command model. Each reference contains metadata
     *         about the associated aggregate root.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves the aggregate target associated with the domain command model.
     * The aggregate target typically represents the root entity in the
     * aggregate context that this command is intended to act upon.
     * This method is ignored during serialization.
     *
     * @return an {@code Optional} containing the {@link AggregateRootMirror}
     *         instance representing the aggregate root target in the domain,
     *         or an empty {@code Optional} if no aggregate target is defined.
     */
    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getAggregateTarget();

    /**
     * Retrieves the domain service target associated with the domain command model.
     * The domain service target typically represents a service within the domain
     * context that this command interacts with or relies upon.
     * This method is ignored during serialization.
     *
     * @return an {@code Optional} containing the {@link DomainServiceMirror}
     *         instance representing the domain service target, or an empty
     *         {@code Optional} if no domain service target is defined.
     */
    @JsonIgnore
    public abstract Optional<DomainServiceMirror> getDomainServiceTarget();

    /**
     * Retrieves the list of processing service kinds associated with the domain command model.
     * Service kinds represent categories or types of services that interact with or process
     * domain commands within the application context.
     * This method is ignored during serialization.
     *
     * @return a list of {@link ServiceKindMirror} instances representing the processing service
     *         kinds associated with the domain command model.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getProcessingServiceKinds();

    /**
     * Retrieves the list of application service mirrors that are involved in processing
     * within the context of the domain command model.
     * This method is ignored during serialization.
     *
     * @return a list of {@link ApplicationServiceMirror} instances representing the application
     *         services associated with processing tasks within the domain command model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getProcessingApplicationServices();

    /**
     * Retrieves the list of domain service mirrors that are involved in processing
     * tasks within the context of the domain command model. These domain services
     * typically represent components or services responsible for handling operations
     * in the domain layer, facilitating core business logic processing and interactions.
     *
     * This method is ignored during serialization.
     *
     * @return a list of {@link DomainServiceMirror} instances representing the domain
     *         services responsible for processing within the domain command model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getProcessingDomainServices();

    /**
     * Retrieves the list of repository mirrors that are involved in processing
     * within the context of the domain command model. These repository mirrors
     * typically represent data storage components or abstractions that manage
     * the persistence and retrieval of domain entities or aggregates.
     * This method is ignored during serialization.
     *
     * @return a list of {@link RepositoryMirror} instances representing the repositories
     *         involved in processing within the specified domain command model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getProcessingRepositories();

    /**
     * Retrieves a list of outbound service mirrors that are currently in a processing state.
     * Ignored during serialization.
     *
     * @return a list of {@code OutboundServiceMirror} instances representing the outbound services in processing.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getProcessingOutboundServices();

    /**
     * Retrieves a list of query handler mirrors that are currently being processed.
     * Ignored during serialization.
     *
     * @return a list of QueryHandlerMirror objects representing the query handlers in processing.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getProcessingQueryHandlers();

    /**
     * Retrieves the domain type associated with the implementing class.
     * Ignored during serialization.
     *
     * @return the domain type as an instance of {@code DomainType}.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

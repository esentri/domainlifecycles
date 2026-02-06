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
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainCommandModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class DomainCommandModelMixin extends DomainTypeModelMixin {

    /**
     * Constructor for the DomainCommandModelMixin.
     * Controls serialization without modifying the actual model class.
     */
    @JsonProperty
    public Optional<String> aggregateTargetIdentityTypeName;

    /**
     * Represents the name of the target type associated with a domain service.
     * This value is optional and may or may not be provided, depending on the context.
     * It is used in the serialization and deserialization of domain-specific commands or models.
     */
    @JsonProperty
    public Optional<String> domainServiceTargetTypeName;

    /**
     * Constructor for the DomainCommandModelMixin class. Used for Jackson deserialization.
     *
     * @param typeName The name of the type.
     * @param isAbstract Indicates whether the type is abstract.
     * @param allFields A list of all fields in the type.
     * @param methods A list of all methods in the type.
     * @param aggregateTargetIdentityTypeName An optional type name for the aggregate target identity.
     * @param domainServiceTargetTypeName An optional type name for the domain service target.
     * @param inheritanceHierarchyTypeNames A list of type names representing the inheritance hierarchy of the type.
     * @param allInterfaceTypeNames A list of all interface type names implemented by the type.
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
     * Retrieves a list of basic fields defined in the associated domain model.
     * This method abstracts over the fields of the model and provides access
     * to their metadata using the {@link FieldMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link FieldMirror} objects representing the basic
     *         fields of the domain model.
     */
    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    /**
     * Retrieves a list of value references defined in the associated domain model.
     * This method abstracts over the references to values and provides access
     * to their metadata using the {@link ValueReferenceMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link ValueReferenceMirror} objects representing the value
     *         references of the domain model.
     */
    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    /**
     * Retrieves a list of entity references defined in the associated domain model.
     * This method abstracts over the references to entities and provides access
     * to their metadata using the {@link EntityReferenceMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link EntityReferenceMirror} objects representing the entity
     *         references of the domain model.
     */
    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    /**
     * Retrieves a list of aggregate root references defined in the associated domain model.
     * This method provides access to the metadata of the references using the
     * {@link AggregateRootReferenceMirror} type. It is annotated with {@code @JsonIgnore}
     * to exclude it from JSON serialization.
     *
     * @return a list of {@link AggregateRootReferenceMirror} objects representing the aggregate
     *         root references in the domain model.
     */
    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Retrieves the aggregate root target associated with the domain command model.
     * This method provides metadata about the aggregate root using the {@link AggregateRootMirror} type.
     * It is annotated with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return an {@link Optional} containing an {@link AggregateRootMirror} instance representing
     *         the aggregate root target, or an empty {@link Optional} if no target is defined.
     */
    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getAggregateTarget();

    /**
     * Retrieves the domain service target associated with the domain command model.
     * This method provides metadata about the domain service using the {@link DomainServiceMirror} type.
     * It is annotated with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return an {@link Optional} containing a {@link DomainServiceMirror} instance representing
     *         the domain service target, or an empty {@link Optional} if no target is defined.
     */
    @JsonIgnore
    public abstract Optional<DomainServiceMirror> getDomainServiceTarget();

    /**
     * Retrieves a list of service kinds associated with the processing logic
     * of the domain command model. This method provides access to the metadata
     * of service kinds using the {@link ServiceKindMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link ServiceKindMirror} objects representing the
     *         processing service kinds in the domain model.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getProcessingServiceKinds();

    /**
     * Retrieves a list of application service mirrors associated with the processing logic
     * of the domain command model. This method provides access to the metadata of application
     * services using the {@link ApplicationServiceMirror} type. It is annotated with {@code @JsonIgnore}
     * to exclude it from JSON serialization.
     *
     * @return a list of {@link ApplicationServiceMirror} objects representing the processing
     *         application services in the domain model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getProcessingApplicationServices();

    /**
     * Retrieves a list of domain service mirrors associated with the processing logic
     * of the domain command model. This method provides access to metadata about
     * domain services using the {@link DomainServiceMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link DomainServiceMirror} objects representing the
     *         processing domain services in the domain model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getProcessingDomainServices();

    /**
     * Retrieves a list of repository mirrors associated with the processing logic
     * of the domain command model. This method provides access to metadata about
     * repositories using the {@link RepositoryMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link RepositoryMirror} objects representing the
     *         processing repositories in the domain model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getProcessingRepositories();

    /**
     * Retrieves a list of outbound service mirrors associated with the processing logic
     * of the domain command model. This method provides access to metadata about
     * outbound services using the {@link OutboundServiceMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link OutboundServiceMirror} objects representing the
     *         processing outbound services in the domain model.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getProcessingOutboundServices();

    /**
     * Retrieves a list of query handler mirrors associated with the processing logic
     * of the domain command model. This method provides access to metadata about
     * query handlers using the {@link QueryHandlerMirror} type. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return a list of {@link QueryHandlerMirror} objects representing the
     *         processing query handlers in the domain model.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getProcessingQueryHandlers();

    /**
     * Retrieves the domain type associated with this entity. It is annotated
     * with {@code @JsonIgnore} to exclude it from JSON serialization.
     *
     * @return the domain type represented as a {@link DomainType} object.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();
}

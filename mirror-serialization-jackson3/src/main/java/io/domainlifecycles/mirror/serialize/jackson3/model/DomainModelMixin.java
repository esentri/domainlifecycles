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
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;

import java.util.List;
import java.util.Map;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainModelMixin {

    @JsonProperty
    private Map<String, ? extends DomainTypeMirror> allTypeMirrors;

    @JsonProperty
    private List<BoundedContextMirror> boundedContextMirrors;

    /**
     * Constructor for the DomainModelMixin class, primarily used to facilitate JSON deserialization
     * of domain model data.
     *
     * @param allTypeMirrors a map where the keys are string representations of type names, and the values
     *                       are objects extending the DomainTypeMirror class that represent information
     *                       about those types in the domain model.
     * @param boundedContextMirrors a list of BoundedContextMirror objects, where each object represents
     *                               a bounded context in the domain model.
     */
    @JsonCreator
    public DomainModelMixin(
        @JsonProperty("allTypeMirrors") Map<String, ? extends DomainTypeMirror> allTypeMirrors,
        @JsonProperty("boundedContextMirrors") List<BoundedContextMirror> boundedContextMirrors) {}

    /**
     * Retrieves all domain type mirrors for the associated domain model.
     * This method returns a collection of {@link DomainTypeMirror} instances
     * representing the mirrored domain types defined within the model.
     * Ignored for serialization purposes.
     *
     * @return a list of {@link DomainTypeMirror} objects representing all
     *         domain types in the associated domain model.
     */
    @JsonIgnore
    public abstract List<DomainTypeMirror> getAllDomainTypeMirrors();

    /**
     * Retrieves all {@link BoundedContextMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing a specific bounded context
     * as defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link BoundedContextMirror} objects representing all bounded contexts
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<BoundedContextMirror> getAllBoundedContextMirrors();

    /**
     * Retrieves all {@link AggregateRootMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents the metadata
     * of an aggregate root from the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link AggregateRootMirror} objects representing all aggregate roots
     *         defined within the associated domain model.
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getAllAggregateRootMirrors();

    /**
     * Retrieves all {@link EntityMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing a specific entity
     * as defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link EntityMirror} objects representing all entities
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<EntityMirror> getAllEntityMirrors();

    /**
     * Retrieves all {@link ValueObjectMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents the metadata
     * of a value object from the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link ValueObjectMirror} objects representing all value objects
     *         defined within the associated domain model.
     */
    @JsonIgnore
    public abstract List<ValueObjectMirror> getAllValueObjectMirrors();

    /**
     * Retrieves all {@link EnumMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing a specific enum
     * as defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link EnumMirror} objects representing all enums
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<EnumMirror> getAllEnumMirrors();

    /**
     * Retrieves all {@link ValueMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing a value type
     * within the domain model, such as enums, value objects, or identities.
     * Ignored for serialization purposes.
     *
     * @return a list of {@link ValueMirror} objects representing all value types
     *         defined in the associated domain model.
     */
    @JsonIgnore
    public abstract List<ValueMirror> getAllValueMirrors();

    /**
     * Retrieves all {@link DomainCommandMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents the metadata
     * of a domain command defined within the model. Ignored for serialization purposes.
     *
     * @return a list of {@link DomainCommandMirror} objects representing all domain commands
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getAllDomainCommandMirrors();

    /**
     * Retrieves all {@link DomainEventMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents the metadata
     * of a domain event defined within the model. Ignored for serialization purposes.
     *
     * @return a list of {@link DomainEventMirror} objects representing all domain events
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getAllDomainEventMirrors();

    /**
     * Retrieves all {@link ApplicationServiceMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents an application service
     * or driver as defined within the associated domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link ApplicationServiceMirror} objects representing all application services
     *         or drivers in the associated domain model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    /**
     * Retrieves all {@link DomainServiceMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, where each mirror represents metadata
     * for a domain service defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link DomainServiceMirror} objects representing all domain services
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getAllDomainServiceMirrors();

    /**
     * Retrieves all {@link RepositoryMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing metadata
     * for repositories defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link RepositoryMirror} objects representing all repositories
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getAllRepositoryMirrors();

    /**
     * Retrieves all {@link ReadModelMirror} instances associated with the domain model.
     * This method provides a collection of mirrors, each representing a read model
     * as defined within the domain model. Ignored for serialization purposes.
     *
     * @return a list of {@link ReadModelMirror} objects representing all read models
     *         in the associated domain model.
     */
    @JsonIgnore
    public abstract List<ReadModelMirror> getAllReadModelMirrors();

    /**
     * Retrieves a list of all query handler mirrors associated with the implementing class.
     * Ignored during serialization.
     *
     * @return a list of {@code QueryHandlerMirror} objects representing the query handler mirrors.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    /**
     * Retrieves a list of all outbound service mirrors. Ignored during serialization.
     *
     * @return a list of {@code OutboundServiceMirror} objects representing the outbound service mirrors.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    /**
     * Retrieves a list of all IdentityMirror objects associated with this instance.
     * Ignored during serialization.
     *
     * @return a list containing all IdentityMirror objects. If there are no identity mirrors, the list may be empty.
     */
    @JsonIgnore
    public abstract List<IdentityMirror> getAllIdentityMirrors();

    /**
     * Retrieves a list of all ServiceKindMirror objects that are classified as unspecified.
     * Ignored during serialization.
     *
     * @return a list of ServiceKindMirror objects that do not have a specified service kind.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    /**
     * Retrieves a list of all ServiceKindMirror objects.
     * Ignored during serialization.
     *
     * @return a list of ServiceKindMirror instances representing all available service kind mirrors.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllServiceKindMirrors();
}

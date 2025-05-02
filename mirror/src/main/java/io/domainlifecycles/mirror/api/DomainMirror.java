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

package io.domainlifecycles.mirror.api;

import java.util.List;
import java.util.Optional;

/**
 * Represents a mirror of a domain model, providing access to various domain-related elements,
 * such as domain type mirrors and bounded context mirrors.
 *
 * @author Mario Herb
 */
public interface DomainMirror {

    /**
     * Retrieves an optional DomainTypeMirror object of the specified type.
     *
     * @param <T>       the type parameter that extends DomainTypeMirror
     * @param typeName  the fully qualified name of the domain type to retrieve the mirror for
     * @return an Optional containing the DomainTypeMirror object if found, otherwise an empty Optional
     */
    <T extends DomainTypeMirror> Optional<T> getDomainTypeMirror(String typeName);

    /**
     * Retrieves a list of all domain type mirrors that represent the various domain types in the system.
     *
     * @return a list of DomainTypeMirror instances, each providing access to mirrored domain type metadata.
     */
    List<DomainTypeMirror> getAllDomainTypeMirrors();

    /**
     * Retrieves a list of all BoundedContextMirror instances,
     * each representing a mirrored Bounded Context within the domain model.
     *
     * @return a list of BoundedContextMirror instances, where each instance
     * mirrors a specific Bounded Context in the domain, providing access to
     * its structural and functional components.
     */
    List<BoundedContextMirror> getAllBoundedContextMirrors();

    /**
     * Retrieves a list of all AggregateRootMirror instances.
     * Each instance represents a mirrored domain aggregate root, providing access
     * to its type-level metadata and structural components.
     *
     * @return a list of AggregateRootMirror instances, where each instance
     * mirrors a domain aggregate root in the system.
     */
    List<AggregateRootMirror> getAllAggregateRootMirrors();

    /**
     * Retrieves a list of EntityMirror instances. Each EntityMirror provides access
     * to mirrored metadata and structural components of domain entities.
     *
     * @return a list of EntityMirror objects, representing all mirrored entities within the domain.
     */
    List<EntityMirror> getAllEntityMirrors();

    /**
     * Retrieves a list of all ValueObjectMirror instances.
     * Each ValueObjectMirror represents a mirrored Value Object
     * within the domain, providing access to its metadata and structure.
     *
     * @return a list of ValueObjectMirror instances, where each instance
     * represents a mirrored domain Value Object.
     */
    List<ValueObjectMirror> getAllValueObjectMirrors();

    /**
     * Retrieves a list of EnumMirror instances. Each EnumMirror represents a mirrored Java enum,
     * providing access to its metadata and structural components.
     *
     * @return a list of EnumMirror objects, where each instance mirrors a specific enum within the domain.
     */
    List<EnumMirror> getAllEnumMirrors();

    /**
     * Retrieves a list of all ValueMirror instances. Each ValueMirror may represent
     * either an EnumMirror, a ValueObjectMirror, or an IdentityMirror within the domain model.
     *
     * @return a list of ValueMirror objects, encompassing all mirrored domain values
     * such as enums, value objects, and identities.
     */
    List<ValueMirror> getAllValueMirrors();

    /**
     * Retrieves a list of all DomainCommandMirror instances.
     * Each DomainCommandMirror instance represents a mirrored domain command within the system,
     * providing access to its metadata and structural components.
     *
     * @return a list of DomainCommandMirror objects, where each instance mirrors a specific domain command.
     */
    List<DomainCommandMirror> getAllDomainCommandMirrors();

    /**
     * Retrieves a list of all DomainEventMirror instances.
     * Each DomainEventMirror represents a mirrored domain event within the system,
     * providing access to its metadata and structural components.
     *
     * @return a list of DomainEventMirror objects, where each instance mirrors a specific domain event.
     */
    List<DomainEventMirror> getAllDomainEventMirrors();

    /**
     * Retrieves a list of all ApplicationServiceMirror instances.
     * Each ApplicationServiceMirror represents a mirrored application service,
     * providing access to its metadata, structural components, and referenced entity definitions.
     *
     * @return a list of ApplicationServiceMirror objects, where each instance mirrors
     *         a specific application service in the domain model.
     */
    List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    /**
     * Retrieves a list of all DomainServiceMirror instances.
     * Each DomainServiceMirror represents a mirrored Domain Service
     * within the system, providing access to its metadata and referenced components.
     *
     * @return a list of DomainServiceMirror objects, where each instance mirrors a specific Domain Service in the domain model.
     */
    List<DomainServiceMirror> getAllDomainServiceMirrors();

    /**
     * Retrieves a list of all RepositoryMirror instances.
     * Each RepositoryMirror provides access to metadata about mirrored repository instances,
     * including their managed aggregates and implemented interfaces.
     *
     * @return a list of RepositoryMirror objects, where each instance mirrors a specific repository
     *         within the domain model, providing structural and metadata information about the repository.
     */
    List<RepositoryMirror> getAllRepositoryMirrors();

    /**
     * Retrieves a list of all ReadModelMirror instances. Each ReadModelMirror represents
     * a mirrored Read Model within the domain, providing access to its metadata and structural components.
     *
     * @return a list of ReadModelMirror objects, where each instance mirrors a specific Read Model in the domain.
     */
    List<ReadModelMirror> getAllReadModelMirrors();

    /**
     * Retrieves a list of all QueryHandlerMirror instances.
     * Each QueryHandlerMirror represents a mirrored Query Handler within the domain,
     * providing access to its metadata, structural components, and associated Read Model.
     *
     * @return a list of QueryHandlerMirror objects, where each instance mirrors
     *         a specific Query Handler in the domain.
     */
    List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    /**
     * Retrieves a list of all OutboundServiceMirror instances.
     * Each OutboundServiceMirror represents a mirrored outbound service,
     * providing access to its metadata and structural details.
     *
     * @return a list of OutboundServiceMirror instances, where each instance mirrors a specific outbound service
     * and provides information such as its interface type and associated metadata.
     */
    List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    /**
     * Retrieves a list of all IdentityMirror instances.
     * Each IdentityMirror represents a mirrored Identity within the domain,
     * providing access to its metadata and structural details.
     *
     * @return a list of IdentityMirror objects, where each instance mirrors
     *         a specific Identity in the domain model.
     */
    List<IdentityMirror> getAllIdentityMirrors();

    /**
     * Retrieves a list of all ServiceKindMirror instances that represent services
     * with unspecified classifications or characteristics within the domain model.
     * This method can be used to access ServiceKindMirror instances that do not belong
     * to a specified service category (RepositoryMirror,
     * DomainServiceMirror, ApplicationServiceMirror, OutboundServiceMirror, QueryHandlerMirror).
     *
     * @return a list of ServiceKindMirror objects, where each represents a service
     *         with unspecified characteristics within the domain model.
     */
    List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    /**
     * Retrieves a list of all ServiceKindMirror instances.
     * Each ServiceKindMirror represents a mirrored service kind
     * within the domain model, providing access to its metadata
     * and structural details.
     *
     * @return a list of ServiceKindMirror objects, representing
     *         all mirrored service kinds (specified or unspecified) in the domain.
     */
    List<ServiceKindMirror> getAllServiceKindMirrors();
}

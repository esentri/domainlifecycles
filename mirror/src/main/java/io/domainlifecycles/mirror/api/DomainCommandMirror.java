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
 *  Copyright 2019-2024 the original author or authors.
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

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.DomainCommand;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;

import java.util.List;
import java.util.Optional;

/**
 * A DomainCommandMirror mirrors a {@link DomainCommand}.
 *
 * @author Mario Herb
 */
public interface DomainCommandMirror extends DomainTypeMirror {

    /**
     * Returns the basic fields of the mirrored {@link DomainCommand}.
     *
     * @return list of {@link FieldMirror} instances, that are not Value references, Entity references or
     * AggregateRoot references
     */
    List<FieldMirror> getBasicFields();

    /**
     * @return a list mirrors for value references (references of {@link ValueObject}, Enums or {@link Identity}) of
     * the mirrored DomainCommand.
     */
    List<ValueReferenceMirror> getValueReferences();

    /**
     * @return a list mirrors for {@link Entity} references  of the mirrored DomainCommand.
     */
    List<EntityReferenceMirror> getEntityReferences();

    /**
     * @return a list mirrors for {@link AggregateRoot} references of the mirrored DomainCommand.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * @return the corresponding mirror when the mirrored DomainCommand targets a specific {@link AggregateRoot},
     * otherwise an empty Optional.
     */
    Optional<AggregateRootMirror> getAggregateTarget();

    /**
     * @return the corresponding mirror when the mirrored DomainCommand targets a specific {@link DomainService},
     * otherwise an empty Optional.
     */
    Optional<DomainServiceMirror> getDomainServiceTarget();

    /**
     * Returns a list of {@link ServiceKindMirror} instances that represent the kinds of services
     * involved in processing the mirrored {@link DomainCommand}. These service kinds typically describe
     * the processing capabilities or roles relevant to the command's lifecycle.
     *
     * @return a list of {@link ServiceKindMirror} defining the processing service kinds associated
     * with the mirrored {@link DomainCommand}.
     */
    List<ServiceKindMirror> getProcessingServiceKinds();

    /**
     * Retrieves a list of mirrors representing the {@link ApplicationServiceMirror} instances
     * involved in processing the mirrored {@link DomainCommand}. These mirrors reflect the
     * application services that play a role in the command's lifecycle or processing logic.
     *
     * @return a list of {@link ApplicationServiceMirror} instances that represent the application
     * services associated with processing the mirrored {@link DomainCommand}.
     */
    List<ApplicationServiceMirror> getProcessingApplicationServices();

    /**
     * Retrieves a list of mirrors representing the {@link DomainServiceMirror} instances
     * involved in processing the mirrored {@link DomainCommand}. These mirrors reflect
     * the domain services that play a role in the command's lifecycle or processing logic.
     *
     * @return a list of {@link DomainServiceMirror} instances that represent the domain
     * services associated with processing the mirrored {@link DomainCommand}.
     */
    List<DomainServiceMirror> getProcessingDomainServices();

    /**
     * Retrieves a list of mirrors representing the {@link RepositoryMirror} instances
     * involved in processing the mirrored {@link DomainCommand}. These mirrors reflect
     * the repositories that play a role in the command's lifecycle or processing logic.
     *
     * @return a list of {@link RepositoryMirror} instances that represent the repositories
     *         associated with processing the mirrored {@link DomainCommand}.
     */
    List<RepositoryMirror> getProcessingRepositories();

    /**
     * Retrieves a list of mirrors representing the {@link OutboundServiceMirror} instances
     * involved in processing the mirrored {@link DomainCommand}. These mirrors reflect
     * the outbound services that play a role in the command's lifecycle or processing logic.
     *
     * @return a list of {@link OutboundServiceMirror} instances that represent the outbound
     * services associated with processing the mirrored {@link DomainCommand}.
     */
    List<OutboundServiceMirror> getProcessingOutboundServices();

    /**
     * Retrieves a list of mirrors representing the {@link QueryHandlerMirror} instances involved in
     * processing the mirrored {@link DomainCommand}. These mirrors reflect the query handlers that
     * play a role in handling and processing query-related logic of the command.
     *
     * @return a list of {@link QueryHandlerMirror} instances that represent the query handlers
     *         associated with processing the mirrored {@link DomainCommand}.
     */
    List<QueryHandlerMirror> getProcessingQueryHandlers();

}

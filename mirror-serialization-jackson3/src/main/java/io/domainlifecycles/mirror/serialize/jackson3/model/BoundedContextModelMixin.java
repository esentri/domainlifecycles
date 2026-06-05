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
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.BoundedContextModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class BoundedContextModelMixin {

    /**
     * Constructs an instance of the BoundedContextModelMixin class. This constructor is
     * used to define properties for deserialization of a bounded context model
     * using Jackson annotations. It represents metadata about the specific package
     * associated with the bounded context model.
     *
     * @param packageName the name of the package associated with the bounded context model.
     */
    @JsonCreator
    public BoundedContextModelMixin(@JsonProperty("packageName") String packageName) {}

    /**
     * Retrieves the list of aggregate root mirrors associated with the bounded
     * context model. This method provides access to the aggregate roots within
     * the bounded context while being excluded from serialization.
     *
     * @return a list of {@code AggregateRootMirror} instances representing the
     *         aggregate roots defined in the bounded context model
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getAggregateRoots();

    /**
     * Retrieves the list of domain service mirrors associated with the bounded context model.
     * This method provides access to the domain services within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code DomainServiceMirror} instances representing the domain services
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getDomainServices();

    /**
     * Retrieves the list of repository mirrors associated with the bounded context model.
     * This method provides access to the repositories within the bounded context and
     * is excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code RepositoryMirror} instances representing the repositories
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getRepositories();

    /**
     * Retrieves the list of read model mirrors associated with the bounded context model.
     * This method provides access to the read models within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code ReadModelMirror} instances representing the read models
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<ReadModelMirror> getReadModels();

    /**
     * Retrieves the list of domain command mirrors associated with the bounded context model.
     * This method provides access to the domain commands within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code DomainCommandMirror} instances representing the domain commands
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getDomainCommands();

    /**
     * Retrieves the list of domain event mirrors associated with the bounded context model.
     * This method provides access to the domain events within the bounded context
     * and is excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code DomainEventMirror} instances representing the domain events
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getDomainEvents();

    /**
     * Retrieves the list of application service mirrors associated with the bounded context model.
     * This method provides access to the application services within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code ApplicationServiceMirror} instances representing the application services
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getApplicationServices();

    /**
     * Retrieves the list of query handler mirrors associated with the bounded context model.
     * This method provides access to the query handlers within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code QueryHandlerMirror} instances representing the query handlers
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getQueryHandlers();

    /**
     * Retrieves the list of outbound service mirrors associated with the bounded context model.
     * This method provides access to the outbound services within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code OutboundServiceMirror} instances representing the outbound services
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getOutboundServices();

    /**
     * Retrieves the list of service kind mirrors associated with the bounded context model.
     * This method provides access to the service kinds within the bounded context while being
     * excluded from serialization using Jackson annotations.
     *
     * @return a list of {@code ServiceKindMirror} instances representing the service kinds
     *         defined in the bounded context model.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getServiceKinds();
}

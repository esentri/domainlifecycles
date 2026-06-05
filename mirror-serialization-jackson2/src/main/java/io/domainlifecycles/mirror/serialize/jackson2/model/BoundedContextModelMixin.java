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
@Deprecated
public abstract class BoundedContextModelMixin {

    /**
     * Constructor for the BoundedContextModelMixin. Controls deserialization without
     * modifying the actual model class.
     *
     *
     * @param packageName the name of the package that contains the bounded context.
     */
    @JsonCreator
    public BoundedContextModelMixin(@JsonProperty("packageName") String packageName) {}

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of aggregate roots in the bounded context
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getAggregateRoots();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of domain services in the bounded context
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getDomainServices();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of repositories in the bounded context
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getRepositories();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of read models in the bounded context
     */
    @JsonIgnore
    public abstract List<ReadModelMirror> getReadModels();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of domain commands in the bounded context
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getDomainCommands();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of domain events in the bounded context
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getDomainEvents();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of application services in the bounded context
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getApplicationServices();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of query handlers in the bounded context
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getQueryHandlers();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of outbound services in the bounded context
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getOutboundServices();

    /**
     * Mixin method declaration. Ignored for serialization purposes.
     * @return list of service kinds in the bounded context
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getServiceKinds();
}

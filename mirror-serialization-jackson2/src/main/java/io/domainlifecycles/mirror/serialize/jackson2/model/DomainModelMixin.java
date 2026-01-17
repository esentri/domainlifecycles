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

    @JsonCreator
    public DomainModelMixin(
        @JsonProperty("allTypeMirrors") Map<String, ? extends DomainTypeMirror> allTypeMirrors,
        @JsonProperty("boundedContextMirrors") List<BoundedContextMirror> boundedContextMirrors) {}

    @JsonIgnore
    public abstract List<DomainTypeMirror> getAllDomainTypeMirrors();

    @JsonIgnore
    public abstract List<BoundedContextMirror> getAllBoundedContextMirrors();

    @JsonIgnore
    public abstract List<AggregateRootMirror> getAllAggregateRootMirrors();

    @JsonIgnore
    public abstract List<EntityMirror> getAllEntityMirrors();

    @JsonIgnore
    public abstract List<ValueObjectMirror> getAllValueObjectMirrors();

    @JsonIgnore
    public abstract List<EnumMirror> getAllEnumMirrors();

    @JsonIgnore
    public abstract List<ValueMirror> getAllValueMirrors();

    @JsonIgnore
    public abstract List<DomainCommandMirror> getAllDomainCommandMirrors();

    @JsonIgnore
    public abstract List<DomainEventMirror> getAllDomainEventMirrors();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getAllDomainServiceMirrors();

    @JsonIgnore
    public abstract List<RepositoryMirror> getAllRepositoryMirrors();

    @JsonIgnore
    public abstract List<ReadModelMirror> getAllReadModelMirrors();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    @JsonIgnore
    public abstract List<IdentityMirror> getAllIdentityMirrors();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllServiceKindMirrors();
}

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
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
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
@Deprecated
public abstract class DomainModelMixin {

    @JsonProperty
    private Map<String, ? extends DomainTypeMirror> allTypeMirrors;
    @JsonProperty
    private List<BoundedContextMirror> boundedContextMirrors;

    /**
     * Constructor for the DomainObjectModelMixin, used to control the deserialization and deserialization
     * of {@link io.domainlifecycles.mirror.model.DomainObjectModel}.
     *
     * @param allTypeMirrors all mirrors in this domain model
     * @param boundedContextMirrors all bounded context mirrors of this domain model
     */
    @JsonCreator
    public DomainModelMixin(
        @JsonProperty("allTypeMirrors") Map<String, ? extends DomainTypeMirror> allTypeMirrors,
        @JsonProperty("boundedContextMirrors") List<BoundedContextMirror> boundedContextMirrors) {}

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link DomainTypeMirror} instances
     */
    @JsonIgnore
    public abstract List<DomainTypeMirror> getAllDomainTypeMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link BoundedContextMirror} instances
     */
    @JsonIgnore
    public abstract List<BoundedContextMirror> getAllBoundedContextMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link AggregateRootMirror} instances
     */
    @JsonIgnore
    public abstract List<AggregateRootMirror> getAllAggregateRootMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link EntityMirror} instances
     */
    @JsonIgnore
    public abstract List<EntityMirror> getAllEntityMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link ValueObjectMirror} instances
     */
    @JsonIgnore
    public abstract List<ValueObjectMirror> getAllValueObjectMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link EnumMirror} instances
     */
    @JsonIgnore
    public abstract List<EnumMirror> getAllEnumMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link ValueMirror} instances
     */
    @JsonIgnore
    public abstract List<ValueMirror> getAllValueMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link DomainCommandMirror} instances
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getAllDomainCommandMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link DomainEventMirror} instances
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getAllDomainEventMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link ApplicationServiceMirror} instances
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link DomainServiceMirror} instances
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getAllDomainServiceMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link RepositoryMirror} instances
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getAllRepositoryMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link ReadModelMirror} instances
     */
    @JsonIgnore
    public abstract List<ReadModelMirror> getAllReadModelMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link QueryHandlerMirror} instances
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link OutboundServiceMirror} instances
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of  {@link IdentityMirror} instances
     */
    @JsonIgnore
    public abstract List<IdentityMirror> getAllIdentityMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of unspecific {@link ServiceKindMirror} instances
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    /**
     * Mixin method declaration. Ignored for serialization.
     * @return list of {@link ServiceKindMirror} instances
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllServiceKindMirrors();
}

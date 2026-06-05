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
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.List;

    /**
     * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ServiceKindModel}.
     * Controls serialization without modifying the actual model class.
     *
     * @author leonvoellinger
     */
    public abstract class ServiceKindModelMixin extends DomainTypeModelMixin {

        /**
         * Constructor for the ServiceKindModelMixin, used to control the deserialization and deserialization
         * of {@link io.domainlifecycles.mirror.model.ServiceKindModel}.
         *
         * @param typeName the name of the service kind type.
         * @param isAbstract a flag indicating whether the service kind type is abstract.
         * @param allFields the list of all fields present in the service kind type, represented by {@link FieldMirror}.
         * @param methods the list of all methods present in the service kind type, represented by {@link MethodMirror}.
         * @param inheritanceHierarchyTypeNames the list of fully qualified type names representing the
         *        inheritance hierarchy of the service kind type.
         * @param allInterfaceTypeNames the list of fully qualified type names representing
         *        all interfaces implemented by the service kind type.
         */
        @JsonCreator
        public ServiceKindModelMixin(
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
         * Mixin method declaration. Ignored for serialization.
         * @return {@link DomainType} of this service kind
         */
        @JsonIgnore
        public abstract DomainType getDomainType();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link ServiceKindMirror} instances representing referenced service kinds
         */
        @JsonIgnore
        public abstract List<ServiceKindMirror> getReferencedServiceKinds();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link RepositoryMirror} instances representing referenced repositories
         */
        @JsonIgnore
        public abstract List<RepositoryMirror> getReferencedRepositories();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link DomainServiceMirror} instances representing referenced domain services
         */
        @JsonIgnore
        public abstract List<DomainServiceMirror> getReferencedDomainServices();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link OutboundServiceMirror} instances representing referenced outbound services
         */
        @JsonIgnore
        public abstract List<OutboundServiceMirror> getReferencedOutboundServices();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link QueryHandlerMirror} instances representing referenced query handlers
         */
        @JsonIgnore
        public abstract List<QueryHandlerMirror> getReferencedQueryHandlers();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link ApplicationServiceMirror} instances representing referenced application services
         */
        @JsonIgnore
        public abstract List<ApplicationServiceMirror> getReferencedApplicationServices();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link DomainCommandMirror} instances representing processed domain commands
         */
        @JsonIgnore
        public abstract List<DomainCommandMirror> processedDomainCommands();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link DomainEventMirror} instances representing published domain events
         */
        @JsonIgnore
        public abstract List<DomainEventMirror> publishedDomainEvents();

        /**
         * Mixin method declaration. Ignored for serialization.
         * @return list of {@link DomainEventMirror} instances representing listened domain events
         */
        @JsonIgnore
        public abstract List<DomainEventMirror> listenedDomainEvents();
    }

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
@Deprecated
public abstract class ServiceKindModelMixin extends DomainTypeModelMixin {

    /**
     * Constructor for the ServiceKindModelMixin class.
     * This mixin is used to define custom deserialization behaviors
     * for the ServiceKindModel in Jackson, without modifying the original model class.
     *
     * @param typeName the name of the type being represented.
     * @param isAbstract boolean indicating whether the type is abstract.
     * @param allFields a list of all fields in the mirrored type, represented as {@link FieldMirror} instances.
     * @param methods a list of methods in the mirrored type, represented as {@link MethodMirror} instances.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance hierarchy of the mirrored type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented by the mirrored type.
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
     * Retrieves the domain type associated with a specific mirrored class.
     * Ignored for serialization purposes.
     *
     * @return the {@link DomainType} that corresponds to the mirrored class,
     *         or {@code NON_DOMAIN} if the class does not represent a domain type.
     */
    @JsonIgnore
    public abstract DomainType getDomainType();

    /**
     * Retrieves a list of {@link ServiceKindMirror} instances referenced by the current model.
     * This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link ServiceKindMirror} objects that are referenced by the model.
     */
    @JsonIgnore
    public abstract List<ServiceKindMirror> getReferencedServiceKinds();

    /**
     * Retrieves a list of {@link RepositoryMirror} instances referenced by the current model.
     * This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link RepositoryMirror} objects representing the repositories
     *         that are referenced by the model.
     */
    @JsonIgnore
    public abstract List<RepositoryMirror> getReferencedRepositories();

    /**
     * Retrieves a list of {@link DomainServiceMirror} instances referenced by the current model.
     * This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link DomainServiceMirror} objects representing the domain services
     *         that are referenced by the model.
     */
    @JsonIgnore
    public abstract List<DomainServiceMirror> getReferencedDomainServices();

    /**
     * Retrieves a list of {@link OutboundServiceMirror} instances that are referenced
     * by the current model. This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link OutboundServiceMirror} objects representing the outbound services
     *         that are referenced by the model.
     */
    @JsonIgnore
    public abstract List<OutboundServiceMirror> getReferencedOutboundServices();

    /**
     * Retrieves a list of {@link QueryHandlerMirror} instances that are referenced
     * by the current model. This method is ignored during JSON serialization or
     * deserialization.
     *
     * @return a list of {@link QueryHandlerMirror} objects representing the query
     *         handlers that are referenced by the model.
     */
    @JsonIgnore
    public abstract List<QueryHandlerMirror> getReferencedQueryHandlers();

    /**
     * Retrieves a list of {@link ApplicationServiceMirror} instances referenced by the current model.
     * This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link ApplicationServiceMirror} objects representing the application services
     *         referenced by the model.
     */
    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getReferencedApplicationServices();

    /**
     * Retrieves a list of {@link DomainCommandMirror} instances that represent
     * the processed domain commands in the current model.
     * This method is ignored during JSON serialization or deserialization.
     *
     * @return a list of {@link DomainCommandMirror} objects representing the processed
     *         domain commands associated with the model.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> processedDomainCommands();

    /**
     * Retrieves a list of {@link DomainEventMirror} instances that represent the domain events
     * published by the current model. This method is ignored during JSON serialization or
     * deserialization.
     *
     * @return a list of {@link DomainEventMirror} objects representing the published domain events
     *         associated with the model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> publishedDomainEvents();

    /**
     * Retrieves a list of {@link DomainEventMirror} instances that represent the domain events
     * listened to by the current model. This method is ignored during JSON serialization or
     * deserialization.
     *
     * @return a list of {@link DomainEventMirror} objects representing the domain events
     *         that are listened to by the model.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> listenedDomainEvents();
}

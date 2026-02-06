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
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.ParamMirror;

import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.MethodModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class MethodModelMixin {

    @JsonProperty
    private List<String> publishedEventTypeNames;

    @JsonProperty
    private Optional<String> listenedEventTypeName;

    /**
     * Constructor for the MethodModelMixin to set up the deserialization
     * of a method model.
     *
     * @param name the name of the method being modeled.
     * @param declaredByTypeName the name of the type that declares this method.
     * @param accessLevel the access level of the method (e.g., public, private, protected).
     * @param parameters the list of parameters associated with the method.
     * @param returnType the return type of the method.
     * @param overridden a flag indicating whether this method overrides a method in a superclass.
     * @param publishedEventTypeNames the list of event type names published by the method.
     * @param listenedEventTypeName an optional event type name listened to by the method.
     */
    @JsonCreator
    public MethodModelMixin(
        @JsonProperty("name") String name,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("parameters") List<ParamMirror> parameters,
        @JsonProperty("returnType") AssertedContainableTypeMirror returnType,
        @JsonProperty("overridden") boolean overridden,
        @JsonProperty("publishedEventTypeNames") List<String> publishedEventTypeNames,
        @JsonProperty("listenedEventTypeName") Optional<String> listenedEventTypeName
    ) {}

    /**
     * Retrieves the list of domain events that have been published by the associated method.
     * Ignored during serialization.
     *
     * @return a list of {@link DomainEventMirror} instances representing the published domain events.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getPublishedEvents();

    /**
     * Retrieves the domain event that the associated method is configured to listen for.
     * This method is ignored during the serialization process.
     *
     * @return an {@link Optional} containing a {@link DomainEventMirror} instance if
     *         the method listens to a specific domain event, or an empty {@link Optional}
     *         if no event is listened to.
     */
    @JsonIgnore
    public abstract Optional<DomainEventMirror> getListenedEvent();

    /**
     * Retrieves the list of domain commands that have been processed by the associated method.
     * This method is ignored during the serialization process.
     *
     * @return a list of {@link DomainCommandMirror} instances representing the domain commands
     *         that have been processed by the method.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getProcessedCommands();
}

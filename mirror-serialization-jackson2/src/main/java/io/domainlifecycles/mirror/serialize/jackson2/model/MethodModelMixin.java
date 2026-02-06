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
@Deprecated
public abstract class MethodModelMixin {

    /**
     * A list of names representing the types of events that are published by the model.
     * This field is used for serialization and deserialization purposes in conjunction
     * with Jackson.
     */
    @JsonProperty
    private List<String> publishedEventTypeNames;

    /**
     * Represents the name of a single event type that this method listens for, wrapped
     * in an {@code Optional}. This field is used to identify the specific event
     * being handled by the method. It facilitates serialization and deserialization
     * via Jackson.
     */
    @JsonProperty
    private Optional<String> listenedEventTypeName;

    /**
     * Constructs an instance of MethodModelMixin for use with Jackson
     * deserialization. This constructor initializes all fields required for representing
     * a method model in serialized form.
     *
     * @param name The name of the method being modeled.
     * @param declaredByTypeName The name of the type that declares this method.
     * @param accessLevel The access level of the method (e.g., public, protected, private, package).
     * @param parameters A list of parameters accepted by the method, represented as {@link ParamMirror}.
     * @param returnType The return type of the method, represented as {@link AssertedContainableTypeMirror}.
     * @param overridden Indicates whether the method is overridden from a superclass or interface.
     * @param publishedEventTypeNames A list of names representing the types of events published by the method.
     * @param listenedEventTypeName An {@code Optional} containing the name of a single event type that
     *                              this method listens for, if applicable.
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
     * Retrieves a list of domain events that have been published by this method model.
     * The events returned represent a detailed view of the types of actions or state
     * changes that the method is responsible for triggering within the domain lifecycle.
     * Ignored during serialization.
     *
     * @return a list of {@code DomainEventMirror} objects representing the published events.
     */
    @JsonIgnore
    public abstract List<DomainEventMirror> getPublishedEvents();

    /**
     * Retrieves the domain event that this method listens for, if applicable.
     * The event returned represents the specific type of action or state change
     * within the domain lifecycle that this method is configured to react to.
     * This method is ignored during JSON serialization.
     *
     * @return an {@code Optional} containing a {@code DomainEventMirror} object that represents
     *         the listened event, or an empty {@code Optional} if no event is being listened to.
     */
    @JsonIgnore
    public abstract Optional<DomainEventMirror> getListenedEvent();

    /**
     * Retrieves a list of domain commands that have been processed by this method model.
     * The commands returned represent the various operations or state changes within
     * the domain lifecycle that this method is designed to handle.
     * This method is ignored during JSON serialization.
     *
     * @return a list of {@code DomainCommandMirror} objects representing the processed commands.
     */
    @JsonIgnore
    public abstract List<DomainCommandMirror> getProcessedCommands();
}

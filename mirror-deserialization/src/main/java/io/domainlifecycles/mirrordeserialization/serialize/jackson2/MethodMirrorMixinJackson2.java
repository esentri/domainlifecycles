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
 *  Unless required by applicable law or written to in writing, software
 *  distributed under this License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.ParamMirror;

import io.domainlifecycles.mirror.model.MethodModel;
import java.util.List;
import java.util.Optional;

/**
 * Jackson mixin interface for proper serialization of {@link MethodModel}.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = MethodModel.class),
})
public interface MethodMirrorMixinJackson2 {

    @JsonProperty
    List<String> getPublishedEventTypeNames();

    @JsonProperty
    Optional<String> getListenedEventTypeName();

    @JsonCreator
    MethodMirrorMixinJackson2 init(
        @JsonProperty("name") String name,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("parameters") List<ParamMirror> parameters,
        @JsonProperty("returnType") AssertedContainableTypeMirror returnType,
        @JsonProperty("overridden") boolean overridden,
        @JsonProperty("publishedEventTypeNames") List<String> publishedEventTypeNames,
        @JsonProperty("listenedEventTypeName") Optional<String> listenedEventTypeName
    );

    @JsonIgnore
    List<DomainEventMirror> getPublishedEvents();

    @JsonIgnore
    Optional<DomainEventMirror> getListenedEvent();

    @JsonIgnore
    List<DomainCommandMirror> getProcessedCommands();
}
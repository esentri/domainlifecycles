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

package io.domainlifecycles.mirrordeserialization.model;

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
public abstract class MethodModelMixinJackson2 {

    @JsonProperty
    private List<String> publishedEventTypeNames;

    @JsonProperty
    private Optional<String> listenedEventTypeName;

    @JsonCreator
    public MethodModelMixinJackson2(
        @JsonProperty("name") String name,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("parameters") List<ParamMirror> parameters,
        @JsonProperty("returnType") AssertedContainableTypeMirror returnType,
        @JsonProperty("overridden") boolean overridden,
        @JsonProperty("publishedEventTypeNames") List<String> publishedEventTypeNames,
        @JsonProperty("listenedEventTypeName") Optional<String> listenedEventTypeName
    ) {}

    @JsonIgnore
    public abstract List<DomainEventMirror> getPublishedEvents();

    @JsonIgnore
    public abstract Optional<DomainEventMirror> getListenedEvent();

    @JsonIgnore
    public abstract List<DomainCommandMirror> getProcessedCommands();
}

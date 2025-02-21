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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainModel;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link MethodMirror}.
 *
 * @author Mario Herb
 */
public class MethodModel implements MethodMirror {

    private final String name;
    private final String declaredByTypeName;
    private final AccessLevel accessLevel;
    private final List<ParamMirror> parameters;
    private final AssertedContainableTypeMirror returnType;

    DomainModel domainModel;
    private boolean domainModelSet = false;

    @JsonProperty
    private final List<String> publishedEventTypeNames;

    @JsonProperty
    private final Optional<String> listenedEventTypeName;

    private final boolean overridden;

    @JsonCreator
    public MethodModel(@JsonProperty("name") String name,
                       @JsonProperty("declaredByTypeName") String declaredByTypeName,
                       @JsonProperty("accessLevel") AccessLevel accessLevel,
                       @JsonProperty("parameters") List<ParamMirror> parameters,
                       @JsonProperty("returnType") AssertedContainableTypeMirror returnType,
                       @JsonProperty("overridden") boolean overridden,
                       @JsonProperty("publishedEventTypeNames") List<String> publishedEventTypeNames,
                       @JsonProperty("listenedEventTypeName") Optional<String> listenedEventTypeName
    ) {
        this.name = Objects.requireNonNull(name);
        this.declaredByTypeName = Objects.requireNonNull(declaredByTypeName);
        this.accessLevel = Objects.requireNonNull(accessLevel);
        Objects.requireNonNull(parameters);
        this.parameters = Collections.unmodifiableList(parameters);
        this.returnType = Objects.requireNonNull(returnType);
        this.overridden = overridden;
        Objects.requireNonNull(publishedEventTypeNames);
        this.publishedEventTypeNames = Collections.unmodifiableList(publishedEventTypeNames);
        this.listenedEventTypeName = Objects.requireNonNull(listenedEventTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeclaredByTypeName() {
        return declaredByTypeName;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParamMirror> getParameters() {
        return parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssertedContainableTypeMirror getReturnType() {
        return returnType;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainEventMirror> getPublishedEvents() {
        return this.publishedEventTypeNames
            .stream()
            .map(n -> (DomainEventMirror) domainModel.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("DomainEventMirror not found for '%s'", n)))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Optional<DomainEventMirror> getListenedEvent() {
        return listenedEventTypeName
            .map(n -> (DomainEventMirror) domainModel.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("DomainEventMirror not found for '%s'", n)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        return getPublishedEvents().stream().anyMatch(p -> p.equals(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        return getListenedEvent().isPresent() && getListenedEvent().get().equals(domainEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        return parameters.stream().anyMatch(p -> p.getType().getTypeName().equals(command.getTypeName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGetter() {
        if (parameters.size() == 0 && AccessLevel.PUBLIC.equals(accessLevel)) {
            var declaringDomainTypeOptional = domainModel.getDomainTypeMirror(declaredByTypeName);
            if (declaringDomainTypeOptional.isPresent()) {
                var declaringDomainType = declaringDomainTypeOptional.get();
                var propName = name;
                if (name.startsWith("get")) {
                    propName = propName.substring(3);
                } else if (name.startsWith("is")) {
                    propName = propName.substring(2);
                }
                if (propName.length() > 0) {
                    propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
                    final var propNameFinal = propName;
                    return declaringDomainType
                        .getAllFields()
                        .stream()
                        .anyMatch(p -> p.getName().equals(propNameFinal)
                            && p.getType().getTypeName().equals(returnType.getTypeName()));
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSetter() {
        if (parameters.size() == 1 && AccessLevel.PUBLIC.equals(accessLevel)) {
            var declaringDomainTypeOptional = domainModel.getDomainTypeMirror(declaredByTypeName);
            if (declaringDomainTypeOptional.isPresent()) {
                var declaringDomainType = declaringDomainTypeOptional.get();
                var propName = name;
                if (name.startsWith("set")) {
                    propName = propName.substring(3);
                }
                if (propName.length() > 0) {
                    propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
                    final var propNameFinal = propName;
                    final var firstParam = parameters.get(0);
                    return declaringDomainType
                        .getAllFields()
                        .stream()
                        .anyMatch(p -> p.getName().equals(propNameFinal)
                            && p.getType().getTypeName().equals(firstParam.getType().getTypeName()));
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOverridden() {
        return overridden;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MethodModel{" +
            "name='" + name + '\'' +
            ", declaredByTypeName='" + declaredByTypeName + '\'' +
            ", accessLevel=" + accessLevel +
            ", parameters=" + parameters +
            ", returnType=" + returnType +
            ", overridden=" + overridden +
            ", publishedEventTypeNames=" + publishedEventTypeNames +
            ", listenedEventTypeName=" + listenedEventTypeName +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodModel that = (MethodModel) o;
        return name.equals(that.name) && declaredByTypeName.equals(
            that.declaredByTypeName) && accessLevel == that.accessLevel && parameters.equals(
            that.parameters) && returnType.equals(that.returnType) && publishedEventTypeNames.equals(
            that.publishedEventTypeNames) && listenedEventTypeName.equals(that.listenedEventTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, declaredByTypeName, accessLevel, parameters, returnType, publishedEventTypeNames,
            listenedEventTypeName);
    }

    public void setDomainModel(DomainModel domainModel) {
        if(!domainModelSet) {
            this.domainModel = domainModel;
            this.domainModelSet = true;
        }
    }

    public DomainModel innerDomainModelReference() {return this.domainModel;}
}

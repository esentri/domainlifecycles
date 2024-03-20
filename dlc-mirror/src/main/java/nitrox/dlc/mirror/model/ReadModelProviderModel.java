/*
 *
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

package nitrox.dlc.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.MethodMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.ReadModelProviderMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model implementation of a {@link ReadModelProviderMirror}.
 *
 * @author Mario Herb
 */
public class ReadModelProviderModel extends DomainTypeModel implements ReadModelProviderMirror {

    @JsonProperty
    private final String providedReadModelTypeName;
    @JsonProperty
    private final List<String> readModelProviderInterfaceTypeNames;

    @JsonCreator
    public ReadModelProviderModel(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("readModelProviderInterfaceTypeNames") List<String> readModelProviderInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames,
        @JsonProperty("providedReadModelTypeName") String providedReadModelTypeName
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.providedReadModelTypeName = Objects.requireNonNull(providedReadModelTypeName);
        this.readModelProviderInterfaceTypeNames = Collections.unmodifiableList(readModelProviderInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        return methods.stream()
            .anyMatch(m -> m.processes(command));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        return methods.stream()
            .anyMatch(m -> m.publishes(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        return methods.stream()
            .anyMatch(m -> m.listensTo(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ReadModelMirror> getProvidedReadModel() {
        return Optional.ofNullable((ReadModelMirror) Domain.typeMirror(providedReadModelTypeName).orElse(null));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getReadModelProviderInterfaceTypeNames() {
        return readModelProviderInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.READ_MODEL_PROVIDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ReadModelProviderModel{" +
            "providedReadModelTypeName='" + providedReadModelTypeName + '\'' +
            ", readModelProviderInterfaceTypeNames=" + readModelProviderInterfaceTypeNames +
            "} " + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReadModelProviderModel that = (ReadModelProviderModel) o;
        return providedReadModelTypeName.equals(that.providedReadModelTypeName) && readModelProviderInterfaceTypeNames.equals(that.readModelProviderInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), providedReadModelTypeName, readModelProviderInterfaceTypeNames);
    }
}

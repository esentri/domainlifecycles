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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.MethodMirror;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import nitrox.dlc.mirror.exception.MirrorException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link RepositoryMirror}.
 *
 * @author Mario Herb
 */
public class RepositoryModel extends DomainTypeModel implements RepositoryMirror {

    @JsonProperty
    private final String managedAggregateTypeName;
    @JsonProperty
    private final List<String> repositoryInterfaceTypeNames;

    @JsonProperty
    private final List<String> referencedOutboundServiceTypeNames;
    @JsonProperty
    private final List<String> referencedQueryClientTypeNames;

    @JsonCreator
    public RepositoryModel(@JsonProperty("typeName") String typeName,
                           @JsonProperty("abstract") boolean isAbstract,
                           @JsonProperty("allFields") List<FieldMirror> allFields,
                           @JsonProperty("methods") List<MethodMirror> methods,
                           @JsonProperty("managedAggregateTypeName") String managedAggregateTypeName,
                           @JsonProperty("referencedOutboundServiceTypeNames") List<String> referencedOutboundServiceTypeNames,
                           @JsonProperty("referencedQueryClientTypeNames") List<String> referencedQueryClientTypeNames,
                           @JsonProperty("repositoryInterfaceTypeNames") List<String> repositoryInterfaceTypeNames,
                           @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                           @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.managedAggregateTypeName = Objects.requireNonNull(managedAggregateTypeName);
        this.referencedOutboundServiceTypeNames = Collections.unmodifiableList(referencedOutboundServiceTypeNames);
        this.referencedQueryClientTypeNames = Collections.unmodifiableList(referencedQueryClientTypeNames);
        this.repositoryInterfaceTypeNames = Collections.unmodifiableList(repositoryInterfaceTypeNames);

    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Optional<AggregateRootMirror> getManagedAggregate() {
        return Optional.ofNullable((AggregateRootMirror) Domain.typeMirror(managedAggregateTypeName).orElse(null));
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<OutboundServiceMirror> getReferencedOutboundServices() {
        return referencedOutboundServiceTypeNames
            .stream()
            .map(n -> (OutboundServiceMirror)Domain.typeMirror(n).orElseThrow(()-> MirrorException.fail("OutboundServiceMirror not found for '%s'", n)))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<QueryClientMirror> getReferencedQueryClients() {
        return referencedQueryClientTypeNames
            .stream()
            .map(n -> (QueryClientMirror)Domain.typeMirror(n).orElseThrow(()-> MirrorException.fail("QueryClientMirror not found for '%s'", n)))
            .collect(Collectors.toList());
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
    public boolean processes(DomainCommandMirror command) {
        return methods.stream()
            .anyMatch(m -> m.processes(command));
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.REPOSITORY;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<String> getRepositoryInterfaceTypeNames() {
        return repositoryInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RepositoryModel{" +
            "managedAggregateTypeName='" + managedAggregateTypeName + '\'' +
            "referencedOutboundServiceTypeNames=" + referencedOutboundServiceTypeNames +
            "referencedQueryClientTypeNames=" + referencedQueryClientTypeNames +
            ", repositoryInterfaceTypeNames=" + repositoryInterfaceTypeNames +
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
        RepositoryModel that = (RepositoryModel) o;
        return managedAggregateTypeName.equals(that.managedAggregateTypeName)
            && repositoryInterfaceTypeNames.equals(that.repositoryInterfaceTypeNames)
            && referencedOutboundServiceTypeNames.equals(that.referencedOutboundServiceTypeNames)
            && referencedQueryClientTypeNames.equals(that.referencedQueryClientTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), managedAggregateTypeName, referencedOutboundServiceTypeNames, referencedQueryClientTypeNames, repositoryInterfaceTypeNames);
    }
}

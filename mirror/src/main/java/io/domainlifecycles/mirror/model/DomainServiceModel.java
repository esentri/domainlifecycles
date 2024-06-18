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

package io.domainlifecycles.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.QueryClientMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link DomainServiceMirror}.
 *
 * @author Mario Herb
 */
public class DomainServiceModel extends DomainTypeModel implements DomainServiceMirror {
    @JsonProperty
    private final List<String> referencedRepositoryTypeNames;

    @JsonProperty
    private final List<String> referencedDomainServiceTypeNames;

    @JsonProperty
    private final List<String> referencedOutboundServiceTypeNames;

    @JsonProperty
    private final List<String> referencedQueryClientTypeNames;

    @JsonProperty
    private final List<String> domainServiceInterfaceTypeNames;

    @JsonCreator
    public DomainServiceModel(@JsonProperty("typeName") String typeName,
                              @JsonProperty("abstract") boolean isAbstract,
                              @JsonProperty("allFields") List<FieldMirror> allFields,
                              @JsonProperty("methods") List<MethodMirror> methods,
                              @JsonProperty("referencedRepositoryTypeNames") List<String> referencedRepositoryTypeNames,
                              @JsonProperty("referencedDomainServiceTypeNames") List<String> referencedDomainServiceTypeNames,
                              @JsonProperty("referencedOutboundServiceTypeNames") List<String> referencedOutboundServiceTypeNames,
                              @JsonProperty("referencedQueryClientTypeNames") List<String> referencedQueryClientTypeNames,
                              @JsonProperty("domainServiceInterfaceTypeNames") List<String> domainServiceInterfaceTypeNames,
                              @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                              @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        Objects.requireNonNull(referencedRepositoryTypeNames);
        this.referencedRepositoryTypeNames = Collections.unmodifiableList(referencedRepositoryTypeNames);
        this.domainServiceInterfaceTypeNames = Collections.unmodifiableList(domainServiceInterfaceTypeNames);
        this.referencedDomainServiceTypeNames = Collections.unmodifiableList(referencedDomainServiceTypeNames);
        this.referencedOutboundServiceTypeNames = Collections.unmodifiableList(referencedOutboundServiceTypeNames);
        this.referencedQueryClientTypeNames = Collections.unmodifiableList(referencedQueryClientTypeNames);

    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<RepositoryMirror> getReferencedRepositories() {
        return referencedRepositoryTypeNames
            .stream()
            .map(n -> (RepositoryMirror)Domain.typeMirror(n).orElseThrow(()-> MirrorException.fail("RepositoryMirror not found for '%s'", n)))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainServiceMirror> getReferencedDomainServices() {
        return referencedDomainServiceTypeNames
            .stream()
            .map(n -> (DomainServiceMirror)Domain.typeMirror(n).orElseThrow(()-> MirrorException.fail("DomainServiceMirror not found for '%s'", n)))
            .collect(Collectors.toList());
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
        return DomainType.DOMAIN_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<String> getDomainServiceInterfaceTypeNames() {
        return domainServiceInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainServiceModel{" +
            "referencedRepositoryTypeNames=" + referencedRepositoryTypeNames +
            "referencedDomainServiceTypeNames=" + referencedDomainServiceTypeNames +
            "referencedOutboundServiceTypeNames=" + referencedOutboundServiceTypeNames +
            "referencedQueryClientTypeNames=" + referencedQueryClientTypeNames +
            "referencedDomainServiceTypeNames=" + referencedDomainServiceTypeNames +
            ", domainServiceInterfaceTypeNames=" + domainServiceInterfaceTypeNames +
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
        DomainServiceModel that = (DomainServiceModel) o;
        return referencedRepositoryTypeNames.equals(that.referencedRepositoryTypeNames)
            && referencedDomainServiceTypeNames.equals(that.referencedDomainServiceTypeNames)
            && referencedOutboundServiceTypeNames.equals(that.referencedOutboundServiceTypeNames)
            && referencedQueryClientTypeNames.equals(that.referencedQueryClientTypeNames)
            && domainServiceInterfaceTypeNames.equals(that.domainServiceInterfaceTypeNames)
            ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            referencedRepositoryTypeNames,
            referencedDomainServiceTypeNames,
            referencedOutboundServiceTypeNames,
            referencedQueryClientTypeNames,
            domainServiceInterfaceTypeNames
        );
    }
}

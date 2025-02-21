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
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceKindModel extends DomainTypeModel implements ServiceKindMirror {

    public ServiceKindModel(@JsonProperty("typeName") String typeName,
                            @JsonProperty("abstract") boolean isAbstract,
                            @JsonProperty("allFields") List<FieldMirror> allFields,
                            @JsonProperty("methods") List<MethodMirror> methods,
                            @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                            @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames) {

        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.publishes(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.listensTo(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        return getMethods().stream()
            .anyMatch(m -> m.processes(command));
    }

    @Override
    @JsonIgnore
    public DomainType getDomainType() {
        return DomainType.SERVICE_KIND;
    }

    @Override
    @JsonIgnore
    public List<ServiceKindMirror> getReferencedServiceKinds() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.SERVICE_KIND.equals(fieldMirror.getType().getDomainType())
                || DomainType.REPOSITORY.equals(fieldMirror.getType().getDomainType())
                || DomainType.DOMAIN_SERVICE.equals(fieldMirror.getType().getDomainType())
                || DomainType.OUTBOUND_SERVICE.equals(fieldMirror.getType().getDomainType())
                || DomainType.QUERY_HANDLER.equals(fieldMirror.getType().getDomainType())
                || DomainType.APPLICATION_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToServiceKindMirror).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public List<RepositoryMirror> getReferencedRepositories() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.REPOSITORY.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToRepositoryMirror).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public List<DomainServiceMirror> getReferencedDomainServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.DOMAIN_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToDomainServiceMirror).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public List<OutboundServiceMirror> getReferencedOutboundServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.OUTBOUND_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToOutboundServiceMirror).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public List<QueryHandlerMirror> getReferencedQueryHandlers() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.QUERY_HANDLER.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToQueryHandlerMirror).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public List<ApplicationServiceMirror> getReferencedApplicationServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.APPLICATION_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToApplicationServiceMirror).collect(Collectors.toList());
    }

    private ServiceKindMirror mapToServiceKindMirror(FieldMirror fieldMirror) {
        return (ServiceKindMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No ServiceKindMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));
    }

    private RepositoryMirror mapToRepositoryMirror(FieldMirror fieldMirror) {
        return (RepositoryMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No RepositoryMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));
    }

    private DomainServiceMirror mapToDomainServiceMirror(FieldMirror fieldMirror) {
        return (DomainServiceMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No DomainServiceMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));
    }

    private OutboundServiceMirror mapToOutboundServiceMirror(FieldMirror fieldMirror) {
        return (OutboundServiceMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No OutboundServiceMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));

    }

    private QueryHandlerMirror mapToQueryHandlerMirror(FieldMirror fieldMirror) {
        return (QueryHandlerMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No QueryHandlerMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));
    }

    private ApplicationServiceMirror mapToApplicationServiceMirror(FieldMirror fieldMirror) {
        return (ApplicationServiceMirror) domainModel.getDomainTypeMirror(fieldMirror.getType().getTypeName())
            .orElseThrow(() -> MirrorException.fail(String.format("No ApplicationServiceMirror found for FieldMirror with type name '%s'.", fieldMirror.getType().getTypeName())));
    }
}

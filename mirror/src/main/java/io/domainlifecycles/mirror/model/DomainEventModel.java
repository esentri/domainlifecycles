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
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link DomainEventMirror}.
 *
 * @author Mario Herb
 */
public class DomainEventModel extends DomainTypeModel implements DomainEventMirror {

    /**
     * Constructs a new instance of the DomainEventModel.
     *
     * @param typeName the name of the type being modeled. Must not be null.
     * @param isAbstract indicates whether the type being modeled is abstract.
     * @param allFields a list of all fields in the type being modeled. Must not be null.
     * @param methods a list of methods in the type being modeled. Must not be null.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy
     *                                       of the type being modeled. Must not be null.
     * @param allInterfaceTypeNames a list of all interface type names implemented by the type being modeled. Must not be null.
     */
    @JsonCreator
    public DomainEventModel(@JsonProperty("typeName") String typeName,
                            @JsonProperty("abstract") boolean isAbstract,
                            @JsonProperty("allFields") List<FieldMirror> allFields,
                            @JsonProperty("methods") List<MethodMirror> methods,
                            @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                            @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<FieldMirror> getBasicFields() {
        return allFields.stream().filter(p ->
                DomainType.NON_DOMAIN.equals(p.getType().getDomainType())
            )
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ValueReferenceMirror> getValueReferences() {
        return allFields.stream().filter(p ->
                DomainType.VALUE_OBJECT.equals(p.getType().getDomainType()) ||
                    DomainType.ENUM.equals(p.getType().getDomainType()) ||
                    DomainType.IDENTITY.equals(p.getType().getDomainType())
            )
            .map(p -> (ValueReferenceMirror) p)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<EntityReferenceMirror> getEntityReferences() {
        return allFields.stream().filter(p ->
                DomainType.ENTITY.equals(p.getType().getDomainType())
            )
            .map(p -> (EntityReferenceMirror) p)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<AggregateRootReferenceMirror> getAggregateRootReferences() {
        return allFields.stream().filter(p ->
                DomainType.AGGREGATE_ROOT.equals(p.getType().getDomainType())
            )
            .map(p -> (AggregateRootReferenceMirror) p)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<AggregateRootMirror> getPublishingAggregates() {
        return domainMirror.getAllAggregateRootMirrors()
            .stream()
            .filter(a ->
                a.publishes(this) || a.getEntityReferences()
                    .stream()
                    .map(EntityReferenceMirror::getEntity)
                    .anyMatch(e -> e.publishes(this))
            )
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainServiceMirror> getPublishingDomainServices() {
        return domainMirror
            .getAllDomainServiceMirrors()
            .stream()
            .filter(ds -> ds.publishes(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<RepositoryMirror> getPublishingRepositories() {
        return domainMirror
            .getAllRepositoryMirrors()
            .stream()
            .filter(r -> r.publishes(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<AggregateRootMirror> getListeningAggregates() {
        return domainMirror.getAllAggregateRootMirrors()
            .stream()
            .filter(a ->
                a.listensTo(this) || a.getEntityReferences()
                    .stream()
                    .map(EntityReferenceMirror::getEntity)
                    .anyMatch(e -> e.listensTo(this))
            )
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainServiceMirror> getListeningDomainServices() {
        return domainMirror
            .getAllDomainServiceMirrors()
            .stream()
            .filter(ds -> ds.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<RepositoryMirror> getListeningRepositories() {
        return domainMirror
            .getAllRepositoryMirrors()
            .stream()
            .filter(r -> r.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ApplicationServiceMirror> getListeningApplicationServices() {
        return domainMirror
            .getAllApplicationServiceMirrors()
            .stream()
            .filter(r -> r.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<OutboundServiceMirror> getListeningOutboundServices() {
        return domainMirror
            .getAllOutboundServiceMirrors()
            .stream()
            .filter(a -> a.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<QueryHandlerMirror> getListeningQueryHandlers() {
        return domainMirror
            .getAllQueryHandlerMirrors()
            .stream()
            .filter(a -> a.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ServiceKindMirror> getListeningServiceKinds() {
        return domainMirror
            .getAllServiceKindMirrors()
            .stream()
            .filter(a -> a.listensTo(this))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ApplicationServiceMirror> getPublishingApplicationServices() {
        return domainMirror.getAllApplicationServiceMirrors()
            .stream()
            .filter(s -> s.publishes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<OutboundServiceMirror> getPublishingOutboundServices() {
        return domainMirror.getAllOutboundServiceMirrors()
            .stream()
            .filter(s -> s.publishes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<QueryHandlerMirror> getPublishingQueryHandlers() {
        return domainMirror.getAllQueryHandlerMirrors()
            .stream()
            .filter(s -> s.publishes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ServiceKindMirror> getPublishingServiceKinds() {
        return domainMirror.getAllServiceKindMirrors()
            .stream()
            .filter(s -> s.publishes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.DOMAIN_EVENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainEventModel{} " + super.toString();
    }

}

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

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link DomainCommandMirror}.
 *
 * @author Mario Herb
 */
public class DomainCommandModel extends DomainTypeModel implements DomainCommandMirror {

    private final Optional<String> aggregateTargetIdentityTypeName;
    private final Optional<String> domainServiceTargetTypeName;

    /**
     * Constructs a new instance of the DomainCommandModel.
     *
     * @param typeName the name of the type being modeled. Must not be null.
     * @param isAbstract indicates whether the type being modeled is abstract.
     * @param allFields a list of all fields in the type being modeled. Must not be null.
     * @param methods a list of methods in the type being modeled. Must not be null.
     * @param aggregateTargetIdentityTypeName an optional name of the type used as the aggregate target identity.
     *                                         Must not be null.
     * @param domainServiceTargetTypeName an optional name of the type used as the domain service target.
     *                                     Must not be null.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy
     *                                       of the type being modeled. Must not be null.
     * @param allInterfaceTypeNames a list of all interface type names implemented by the type being modeled.
     *                              Must not be null.
     */
    public DomainCommandModel(String typeName,
                              boolean isAbstract,
                              List<FieldMirror> allFields,
                              List<MethodMirror> methods,
                              Optional<String> aggregateTargetIdentityTypeName,
                              Optional<String> domainServiceTargetTypeName,
                              List<String> inheritanceHierarchyTypeNames,
                              List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.aggregateTargetIdentityTypeName = Objects.requireNonNull(aggregateTargetIdentityTypeName);
        this.domainServiceTargetTypeName = Objects.requireNonNull(domainServiceTargetTypeName);
    }

    /**
     * {@inheritDoc}
     */
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
    @Override
    public Optional<AggregateRootMirror> getAggregateTarget() {
        var identity = aggregateTargetIdentityTypeName
            .map(n -> domainMirror.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("AggregateRootMirror not found for '%s'", n)))
            .map(m -> (IdentityMirror) m);
        return identity.flatMap(identityMirror -> domainMirror
            .getAllDomainTypeMirrors()
            .stream()
            .filter(tm -> tm instanceof AggregateRootMirror)
            .map(tm -> (AggregateRootMirror) tm)
            .filter(am -> am.getIdentityField().isPresent())
            .filter(am -> am.getIdentityField().get().getType().getTypeName().equals(identityMirror.getTypeName()))
            .findFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DomainServiceMirror> getDomainServiceTarget() {
        return domainServiceTargetTypeName
            .map(n -> domainMirror.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("DomainServiceMirror not found for '%s'", n)))
            .map(m -> (DomainServiceMirror) m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceKindMirror> getProcessingServiceKinds() {
        return domainMirror.getAllServiceKindMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationServiceMirror> getProcessingApplicationServices() {
        return domainMirror.getAllApplicationServiceMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainServiceMirror> getProcessingDomainServices() {
        return domainMirror.getAllDomainServiceMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RepositoryMirror> getProcessingRepositories() {
        return domainMirror.getAllRepositoryMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OutboundServiceMirror> getProcessingOutboundServices() {
        return domainMirror.getAllOutboundServiceMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryHandlerMirror> getProcessingQueryHandlers() {
        return domainMirror.getAllQueryHandlerMirrors()
            .stream()
            .filter(s -> s.processes(this))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.DOMAIN_COMMAND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainCommandModel{" +
            "aggregateTargetIdentityTypeName=" + aggregateTargetIdentityTypeName +
            ", domainServiceTargetTypeName=" + domainServiceTargetTypeName +
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
        DomainCommandModel that = (DomainCommandModel) o;
        return aggregateTargetIdentityTypeName.equals(
            that.aggregateTargetIdentityTypeName) && domainServiceTargetTypeName.equals(
            that.domainServiceTargetTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aggregateTargetIdentityTypeName, domainServiceTargetTypeName);
    }
}
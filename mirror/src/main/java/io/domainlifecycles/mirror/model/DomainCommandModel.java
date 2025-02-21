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
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
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

    @JsonProperty
    private final Optional<String> aggregateTargetIdentityTypeName;
    @JsonProperty
    private final Optional<String> domainServiceTargetTypeName;

    @JsonCreator
    public DomainCommandModel(@JsonProperty("typeName") String typeName,
                              @JsonProperty("abstract") boolean isAbstract,
                              @JsonProperty("allFields") List<FieldMirror> allFields,
                              @JsonProperty("methods") List<MethodMirror> methods,
                              @JsonProperty("aggregateTargetIdentityTypeName") Optional<String> aggregateTargetIdentityTypeName,
                              @JsonProperty("domainServiceTargetTypeName") Optional<String> domainServiceTargetTypeName,
                              @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                              @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.aggregateTargetIdentityTypeName = Objects.requireNonNull(aggregateTargetIdentityTypeName);
        this.domainServiceTargetTypeName = Objects.requireNonNull(domainServiceTargetTypeName);
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
    public Optional<AggregateRootMirror> getAggregateTarget() {
        var identity = aggregateTargetIdentityTypeName
            .map(n -> domainModel.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("AggregateRootMirror not found for '%s'", n)))
            .map(m -> (IdentityMirror) m);
        return identity.flatMap(identityMirror -> domainModel
            .allTypeMirrors()
            .values()
            .stream()
            .filter(tm -> tm instanceof AggregateRootMirror)
            .map(tm -> (AggregateRootMirror) tm)
            .filter(am -> am.getIdentityField().isPresent())
            .filter(am -> am.getIdentityField().get().getType().equals(identityMirror.getTypeName()))
            .findFirst());
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Optional<DomainServiceMirror> getDomainServiceTarget() {
        return domainServiceTargetTypeName
            .map(n -> domainModel.getDomainTypeMirror(n).orElseThrow(
                () -> MirrorException.fail("DomainServiceMirror not found for '%s'", n)))
            .map(m -> (DomainServiceMirror) m);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
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

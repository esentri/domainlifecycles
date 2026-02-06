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

import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.visitor.ContextDomainObjectVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link EntityMirror}.
 *
 * @author Mario Herb
 */
public class EntityModel extends DomainObjectModel implements EntityMirror, DomainObjectMirror {

    private final Optional<FieldMirror> identityField;
    private final Optional<FieldMirror> concurrencyVersionField;

    /**
     * Constructs an instance of {@code EntityModel}.
     *
     * @param typeName the name of the type being mirrored
     * @param isAbstract indicates whether the type is abstract
     * @param allFields a list of all fields in the mirrored type
     * @param methods a list of methods in the mirrored type
     * @param identityField the optional field representing the identity of the entity
     * @param concurrencyVersionField the optional field used for concurrency versioning
     * @param inheritanceHierarchyTypeNames a list of type names in the inheritance hierarchy of the mirrored type
     * @param allInterfaceTypeNames a list of all interface type names implemented by the mirrored type
     */
    public EntityModel(String typeName,
                       boolean isAbstract,
                       List<FieldMirror> allFields,
                       List<MethodMirror> methods,
                       Optional<FieldMirror> identityField,
                       Optional<FieldMirror> concurrencyVersionField,
                       List<String> inheritanceHierarchyTypeNames,
                       List<String> allInterfaceTypeNames

    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.identityField = Objects.requireNonNull(identityField);
        this.concurrencyVersionField = Objects.requireNonNull(concurrencyVersionField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FieldMirror> getIdentityField() {
        return identityField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FieldMirror> getConcurrencyVersionField() {
        return concurrencyVersionField;
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
    public EntityReferenceMirror entityReferenceByName(String name) {
        return allFields.stream()
            .filter(p ->
                DomainType.ENTITY.equals(p.getType().getDomainType()) && p.getName().equals(name)
            )
            .map(p -> (EntityReferenceMirror) p)
            .findFirst()
            .orElseThrow(() -> MirrorException.fail(
                String.format("EntityReferenceMirror not found for name '%s' within '%s'!", name, typeName)));
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
    public AggregateRootReferenceMirror aggregateRootReferenceByName(String name) {
        return allFields.stream().filter(p ->
                DomainType.AGGREGATE_ROOT.equals(p.getType().getDomainType()) && p.getName().equals(name)
            )
            .map(p -> (AggregateRootReferenceMirror) p)
            .findFirst()
            .orElseThrow(() -> MirrorException.fail(
                String.format("AggregateRootReferenceMirror not found for name '%s' within '%s'!", name, typeName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueReferenceMirror> getValueReferences() {
        return allFields.stream().filter(p ->
                DomainType.VALUE_OBJECT.equals(p.getType().getDomainType()) ||
                    DomainType.ENUM.equals(p.getType().getDomainType()) ||
                    (DomainType.IDENTITY.equals(p.getType().getDomainType()) && !p.getName().equals(
                        getIdentityField().map(FieldMirror::getName).orElse(null)))
            )
            .map(p -> (ValueReferenceMirror) p)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldMirror> getBasicFields() {
        return identityField.map(fieldMirror -> super.getBasicFields().stream().filter(p ->
                !p.getName().equals(fieldMirror.getName())
            )
            .collect(Collectors.toList())).orElseGet(super::getBasicFields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        AtomicBoolean publishes = new AtomicBoolean(false);
        var visitor = new ContextDomainObjectVisitor(this) {
            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                if (!publishes.get()) {
                    publishes.set(domainTypeMirror
                        .getMethods()
                        .stream()
                        .anyMatch(m -> m.publishes(domainEvent)));
                }
            }
        };
        visitor.start();
        return publishes.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        AtomicBoolean listensTo = new AtomicBoolean(false);
        var visitor = new ContextDomainObjectVisitor(this) {
            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                if (!listensTo.get()) {
                    listensTo.set(domainTypeMirror
                        .getMethods()
                        .stream()
                        .anyMatch(m -> m.listensTo(domainEvent)));
                }
            }
        };
        visitor.start();
        return listensTo.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        AtomicBoolean processes = new AtomicBoolean(false);
        var visitor = new ContextDomainObjectVisitor(this) {
            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                if (!processes.get()) {
                    processes.set(domainTypeMirror
                        .getMethods()
                        .stream()
                        .anyMatch(m -> m.processes(command)));
                }
            }
        };
        visitor.start();
        return processes.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainCommandMirror> processedDomainCommands() {
        return methods
            .stream()
            .flatMap(m -> m.getProcessedCommands().stream())
            .distinct()
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainEventMirror> publishedDomainEvents() {
        return methods
            .stream()
            .flatMap(m -> m.getPublishedEvents().stream())
            .distinct()
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainEventMirror> listenedDomainEvents() {
        return methods
            .stream()
            .flatMap(m -> m.getListenedEvent().stream())
            .distinct()
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.ENTITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EntityModel{" +
            "identityField=" + identityField +
            "concurrencyVersionField=" + concurrencyVersionField +
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
        EntityModel that = (EntityModel) o;
        return identityField.equals(that.identityField) && concurrencyVersionField.equals(that.concurrencyVersionField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identityField, concurrencyVersionField);
    }
}
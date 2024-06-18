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

package io.domainlifecycles.persistence.provider;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.persistence.configuration.DomainPersistenceConfiguration;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.ConverterRegistry;
import io.domainlifecycles.persistence.mirror.api.PersistenceMirror;
import io.domainlifecycles.persistence.mirror.api.RecordMirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Provides base class to build access models for domain objects from a persistence mirror.
 *
 * @author Mario Herb
 */
public abstract class DomainPersistenceProvider<BASE_RECORD> {

    /**
     * The registry for available converters.
     */
    public final ConverterRegistry converterRegistry;

    /**
     * The persistence mirror.
     */
    public final PersistenceMirror<BASE_RECORD> persistenceMirror;

    /**
     * The domain persistence configuration.
     */
    public final DomainPersistenceConfiguration domainPersistenceConfiguration;


    /**
     * Constructor.
     *
     * @param domainPersistenceConfiguration the domain persistence configuration
     */
    public DomainPersistenceProvider(DomainPersistenceConfiguration domainPersistenceConfiguration) {
        if(!Domain.isInitialized()){
            throw DLCPersistenceException.fail("The Domain must be initialized before creating the DomainPersistenceProvider!");
        }
        this.domainPersistenceConfiguration = domainPersistenceConfiguration;
        this.converterRegistry = new ConverterRegistry();
        this.persistenceMirror = buildPersistenceMirror();

    }

    protected abstract PersistenceMirror<BASE_RECORD> buildPersistenceMirror();

    /**
     * Builds the access model for the given aggregate instance.
     *
     * @param aggregateRoot the aggregate instance
     * @return the access model
     */
    public DomainObjectInstanceAccessModel<BASE_RECORD> buildAccessModel(AggregateRoot<?> aggregateRoot) {
        return buildAccessModelForInstance(aggregateRoot, null, null);
    }

    private DomainObjectInstanceAccessModel<BASE_RECORD> buildAccessModelForInstance(DomainObject instance,
                                                                        StructuralPosition parentPosition,
                                                                        String accessorFromParent) {

        StructuralPosition position = StructuralPosition.builder()
            .withInstance(instance)
            .withParentStructuralPosition(parentPosition)
            .withAccessorFromParent(accessorFromParent)
            .build();

        DomainObjectInstanceAccessModel.DomainObjectInstanceAccessModelBuilder<BASE_RECORD> builder = DomainObjectInstanceAccessModel.builder();
        builder.withStructuralPosition(position);
        builder.withRecordMirror(getRecordMirror(position));
        if (!position.isBackReference) {
            if (instance instanceof Entity) {
                EntityMirror em = Domain.entityMirrorFor((Entity<?>) instance);

                em.getEntityReferences().stream().filter(er -> !er.isStatic()).forEach(er -> addChildrenToBuilder(position, er, builder));
                em.getAggregateRootReferences().stream().filter(ar -> !ar.isStatic()).forEach(ar -> addChildrenToBuilder(position, ar, builder));
                em.getValueReferences().stream().filter(vr -> !vr.isStatic())
                    .filter(vr -> DomainType.VALUE_OBJECT.equals(vr.getType().getDomainType()))
                    .forEach(voc -> addChildrenToBuilder(position, voc, builder));
            } else {
                ValueObjectMirror vm = Domain.valueObjectMirrorFor((ValueObject) instance);
                vm.getValueReferences()
                    .stream()
                    .filter(vr -> !vr.isStatic())
                    .filter(vr -> DomainType.VALUE_OBJECT.equals(vr.getType().getDomainType()))
                    .forEach(voc -> addChildrenToBuilder(position, voc, builder));
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private void addChildrenToBuilder(StructuralPosition parentStructuralPosition,
                                      FieldMirror accessToChildMirror,
                                      DomainObjectInstanceAccessModel.DomainObjectInstanceAccessModelBuilder<BASE_RECORD> builder) {
        Object childObjectInstance = DlcAccess.accessorFor(parentStructuralPosition.instance).peek(accessToChildMirror.getName());
        if (childObjectInstance != null) {
            if (childObjectInstance instanceof Iterable iterable) {
                iterable.forEach(
                    child -> {
                        DomainObjectInstanceAccessModel<BASE_RECORD> childInstance = buildAccessModelForInstance((DomainObject) child, parentStructuralPosition, accessToChildMirror.getName());

                        builder.withChildInstance(childInstance);
                    }
                );
            } else if (accessToChildMirror.getType().hasOptionalContainer()) {
                Optional<?> opt = (Optional<?>) childObjectInstance;
                if (opt.isPresent()) {
                    DomainObjectInstanceAccessModel<BASE_RECORD> childInstance = buildAccessModelForInstance((DomainObject) opt.get(), parentStructuralPosition, accessToChildMirror.getName());
                    builder.withChildInstance(childInstance);
                }
            } else {
                DomainObjectInstanceAccessModel<BASE_RECORD> childInstance = buildAccessModelForInstance((DomainObject)childObjectInstance, parentStructuralPosition, accessToChildMirror.getName());
                builder.withChildInstance(childInstance);
            }
        }
    }

    private RecordMirror<BASE_RECORD> getRecordMirror(StructuralPosition structuralPosition) {
        if (structuralPosition.instance instanceof Entity) {
            Class<?> entityType = structuralPosition.instance.getClass();
            return persistenceMirror.getEntityRecordMirror(entityType.getName());
        } else {
            if (structuralPosition.accessPathFromRoot.size() == 0) {
                throw DLCPersistenceException.fail("A value object can only be persisted when being child of an aggregate. Something went wrong! Value object: " + structuralPosition.instance);
            }
            final var descendingIterator = structuralPosition.accessPathFromRoot.descendingIterator();
            Class<?> containingEntityType = null;
            final var pathSegments = new ArrayList<>();
            while (descendingIterator.hasNext()) {
                final var element = descendingIterator.next();
                pathSegments.add(element.accessorToNextElement);
                if (element.domainObject instanceof Entity) {
                    containingEntityType = element.domainObject.getClass();
                    break;
                }
            }
            Collections.reverse(pathSegments);
            if (containingEntityType == null) {
                throw DLCPersistenceException.fail("A value object can only be persisted when being contained within an entity. Something went wrong! Value object: " + structuralPosition.instance);
            }
            final var containingEntityTypeName = containingEntityType.getName();
            final var erm = persistenceMirror.getEntityRecordMirror(containingEntityTypeName);

            return erm.valueObjectRecords()
                .stream()
                .filter(vorm -> vorm.domainObjectTypeName().equals(structuralPosition.instance.getClass().getName()))
                .filter(vorm -> vorm.pathSegments().equals(pathSegments))
                .findFirst()
                .orElse(null);
        }
    }

    /**
     * Returns the {@link Identity} of the given {@link Entity}.
     *
     * @param entity the entity
     * @return the identity
     */
    public Identity<?> getId(Entity<?> entity) {
        var em = Domain.entityMirrorFor(entity);
        var idField = em.getIdentityField()
            .orElseThrow(() -> DLCPersistenceException.fail("Identity field not defined for '%s'", entity.getClass().getName()));
        return DlcAccess.accessorFor(entity).peek(idField.getName());
    }

    /**
     * Returns the record tye for the given entity type.
     *
     * @param fullQualifiedEntityClassName of Entity
     * @return the record type
     */
    public String entityRecordType(String fullQualifiedEntityClassName) {
        return persistenceMirror.getEntityRecordMirror(fullQualifiedEntityClassName)
            .recordTypeName();
    }

}

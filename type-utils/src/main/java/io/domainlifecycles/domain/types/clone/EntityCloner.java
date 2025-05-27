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

package io.domainlifecycles.domain.types.clone;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.exception.DLCTypesException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class create a deep clone of {@link Entity} instances using the mirror.
 * A cloned instance contains exactly the same values but is a new Java Object instance of the cloned type.
 *
 * @author Mario Herb
 */
public class EntityCloner {

    private final DomainObjectBuilderProvider domainObjectBuilderProvider;

    /**
     * Constructs an EntityCloner instance.
     *
     * @param domainObjectBuilderProvider the provider to obtain DomainObjectBuilders for creating deep clones of domain objects.
     */
    public EntityCloner(DomainObjectBuilderProvider domainObjectBuilderProvider) {
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
    }

    /**
     * Clone a given entity Object instance (deep clone).
     *
     * @param entity the Entity to be cloned
     * @return return a new Entity instance of the same type carrying the same values.
     */
    public Entity<?> clone(Entity<?> entity) {
        Set<Identity<?>> cloningEntityIds = new HashSet<>();
        List<BackReference> backReferences = new ArrayList<>();
        Map<Identity<?>, Entity<?>> clonedEntities = new HashMap<>();
        return cloneInternal(entity, cloningEntityIds, backReferences, clonedEntities);
    }

    private Entity<?> cloneInternal(Entity<?> entity,
                                    Set<Identity<?>> cloningEntityIds,
                                    List<BackReference> backReferences,
                                    Map<Identity<?>, Entity<?>> clonedEntities) {
        if (entity == null) {
            return null;
        }
        cloningEntityIds.add(getId(entity));
        var em = Domain.entityMirrorFor(entity);

        DomainObjectBuilder<Entity<?>> domainObjectBuilder = domainObjectBuilderProvider.provide(
            entity.getClass().getName());

        domainObjectBuilder.setFieldValue(getId(entity), domainObjectBuilder.getPrimaryIdentityFieldName());
        cloneEntityProperties(em, entity, domainObjectBuilder);
        cloneEntityValueObjectCompositions(em, entity, domainObjectBuilder);
        cloneEntityAssociations(em, entity, domainObjectBuilder, cloningEntityIds, backReferences, clonedEntities);

        Entity<?> clonedEntity = domainObjectBuilder.build();
        clonedEntities.put(getId(clonedEntity), clonedEntity);
        for (BackReference backReference : backReferences) {
            if (backReference.targetId.equals(getId(clonedEntity))) {
                Entity<?> clonedSourceEntity = clonedEntities.get(getId(backReference.source));
                var accessor = DlcAccess.accessorFor(clonedSourceEntity);
                if (backReference.entityReferenceMirror.getType().hasCollectionContainer()) {
                    Collection<Entity<?>> c = accessor.peek(backReference.entityReferenceMirror.getName());
                    c.add(clonedEntity);
                } else {
                    accessor.poke(backReference.entityReferenceMirror.getName(), clonedEntity);
                }
            }
        }
        return clonedEntity;
    }

    private void cloneEntityProperties(EntityMirror em,
                                       Entity<?> entity,
                                       DomainObjectBuilder<?> entityDomainObjectBuilder) {
        em.getBasicFields().stream().filter(fm -> !fm.isStatic()).forEach(fm -> {
            if (entityDomainObjectBuilder.canInstantiateField(fm.getName())) {
                Object value = DlcAccess.accessorFor(entity).peek(fm.getName());
                entityDomainObjectBuilder.setFieldValue(value, fm.getName());
            }
        });
    }

    private void cloneEntityValueObjectCompositions(EntityMirror em,
                                                    Entity<?> entity,
                                                    DomainObjectBuilder<?> entityDomainObjectBuilder) {
        em.getValueReferences().forEach(vrm -> {
            if (entityDomainObjectBuilder.canInstantiateField(vrm.getName())) {
                var accessor = DlcAccess.accessorFor(entity);
                if (vrm.getType().hasCollectionContainer()) {
                    Collection<?> c = accessor.peek(vrm.getName());
                    if (c != null) {
                        c.forEach(ai -> entityDomainObjectBuilder.addValueToCollection(ai, vrm.getName()));
                    }
                } else {
                    Object value = accessor.peek(vrm.getName());
                    entityDomainObjectBuilder.setFieldValue(value, vrm.getName());
                }
            }
        });
    }

    private void cloneEntityAssociations(EntityMirror em,
                                         Entity<?> entity,
                                         DomainObjectBuilder<?> domainObjectBuilder,
                                         Set<Identity<?>> cloningEntityIds,
                                         List<BackReference> backReferences,
                                         Map<Identity<?>, Entity<?>> clonedEntities
    ) {
        var refs = new ArrayList<FieldMirror>();
        refs.addAll(em.getEntityReferences().stream().filter(erm -> !erm.isStatic()).toList());
        refs.addAll(em.getAggregateRootReferences().stream().filter(arm -> !arm.isStatic()).toList());
        refs.forEach(erm -> {
            if (domainObjectBuilder.canInstantiateField(erm.getName())) {
                var accessor = DlcAccess.accessorFor(entity);
                if (erm.getType().hasCollectionContainer()) {
                    Collection<Entity<?>> c = accessor.peek(erm.getName());
                    if (c != null) {
                        c.forEach(ref -> {
                            if (ref != null && cloningEntityIds.contains(getId(ref))) {
                                Entity<?> clonedReference = clonedEntities.get(getId(ref));
                                if (clonedReference != null) {
                                    domainObjectBuilder.addValueToCollection(clonedReference, erm.getName());
                                } else {
                                    BackReference br = new BackReference(entity, erm, getId(clonedReference));
                                    backReferences.add(br);
                                }
                            } else {
                                Entity<?> clonedAssociation = cloneInternal(
                                    ref,
                                    cloningEntityIds,
                                    backReferences,
                                    clonedEntities
                                );
                                domainObjectBuilder.addValueToCollection(clonedAssociation, erm.getName());
                            }
                        });
                    }
                } else {
                    Entity<?> reference = accessor.peek(erm.getName());
                    if (reference != null && cloningEntityIds.contains(getId(reference))) {
                        Entity<?> clonedAssociation = clonedEntities.get(getId(reference));
                        if (clonedAssociation != null) {
                            domainObjectBuilder.setFieldValue(clonedAssociation, erm.getName());
                        } else {
                            BackReference br = new BackReference(entity, erm, getId(reference));
                            backReferences.add(br);
                        }
                    } else {
                        Entity<?> clonedEntityReference = cloneInternal(
                            reference,
                            cloningEntityIds,
                            backReferences,
                            clonedEntities
                        );
                        domainObjectBuilder.setFieldValue(clonedEntityReference, erm.getName());
                    }
                }
            }
        });

    }

    private Identity<?> getId(Entity<?> entity) {
        var em = Domain.entityMirrorFor(entity);
        var idField = em.getIdentityField().orElseThrow(() ->
            DLCTypesException.fail("Identity not defined for '%s'", entity.getClass().getName()));
        return DlcAccess.accessorFor(entity).peek(idField.getName());
    }

    private record BackReference(Entity<?> source,
                                 FieldMirror entityReferenceMirror,
                                 Identity<?> targetId) {
    }


}

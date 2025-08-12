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

package io.domainlifecycles.domain.types.companions;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.access.object.DynamicDomainObjectAccessor;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.exception.DLCTypesException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.reflect.utils.ReflectionEntityTypeUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Companion class which gives us static access to several typical Entity related utility functions
 * implemented in a general way based on meta information kept in the domain mirror.
 * A pure reflective way is used in cases where the mirror is not initialized, mostly to simplify unit testing.
 *
 * @author Mario Herb
 */
public class Entities {

    /**
     * Generic equals implementation for Entities depending on domain mirror.
     * In DDD and DLC entities are considered to be equal, if their identities are equal.
     *
     * @param thisEntity entity to be compared
     * @param thatEntity entity to be compared
     * @return true, if thisEntity is equal to thatEntity
     */
    public static boolean equals(Entity<?> thisEntity, Object thatEntity) {
        Objects.requireNonNull(thisEntity, "thisEntity is required to be not null, when calling 'equals'!");
        if (thatEntity == null) {
            return false;
        }
        if (!thisEntity.getClass().equals(thatEntity.getClass())) {
            return false;
        }
        var thisId = thisEntity.id();
        var thatId = ((Entity<?>) thatEntity).id();
        return thisId.equals(thatId);
    }

    /**
     * Generic hashCode implementation for Entities depending on domain mirror.
     *
     * @param thisEntity {@link Entity} instance
     * @return hashcode of given {@link Entity} instance
     */
    public static int hashCode(Entity<?> thisEntity) {
        Objects.requireNonNull(thisEntity, "thisEntity is required to be not null, when calling 'hashCode'!");
        Identity<?> thisId = thisEntity.id();
        return thisId == null ? 0 : thisId.hashCode();
    }

    /**
     * Generic id() implementation for Entities depending on domain mirror.
     *
     * @param thisEntity {@link Entity} instance
     * @return {@link Identity} given {@link Entity} instance
     */
    public static Identity<?> id(Entity<?> thisEntity) {
        Objects.requireNonNull(thisEntity, "thisEntity is required to be not null, when calling 'id'!");
        var accessor = DlcAccess.accessorFor(thisEntity);
        return accessor.peek(idFieldName(thisEntity));
    }

    private static String idFieldName(Entity<?> thisEntity) {
        if(Domain.isInitialized()) {
            var em = Domain.entityMirrorFor(thisEntity);
            var idField = em.getIdentityField()
                .orElseThrow(
                    () -> DLCTypesException.fail("Identity field not defined for '%s'", thisEntity.getClass().getName()));
            return idField.getName();
        }else{
            var idField = ReflectionEntityTypeUtils.identityField((Class<? extends Entity<? extends Identity<?>>>) thisEntity.getClass());
            return idField.orElseThrow(() -> DLCTypesException.fail("Identity field not defined for '%s'", thisEntity.getClass().getName())).getName();
        }
    }

    /**
     * Generic toString implementation for Entities depending on domain mirror.
     *
     * @param thisEntity {@link Entity} instance to get String representation of
     * @return String-representation of the given Entity instance
     */
    public static String toString(Entity<?> thisEntity) {
        Objects.requireNonNull(thisEntity, "thisEntity is required to be not null, when calling 'toString'!");
        var idFieldName = idFieldName(thisEntity);
        var accessor = DlcAccess.accessorFor(thisEntity);
        return thisEntity.getClass().getName()
            + "@" + System.identityHashCode(thisEntity)
            + "(" + idFieldName
            + "=" + accessor.peek(idFieldName) + ")";
    }

    /**
     * Detect changes for 2 given entity instances with the same id.
     *
     * @param entityInstanceBefore instance before change
     * @param entityInstanceAfter  instance after change
     * @param deep                 flag for deep change
     * @return a set of {@link DetectedChange}s
     */
    public static Set<DetectedChange> detectChanges(Entity<?> entityInstanceBefore, Entity<?> entityInstanceAfter,
                                                    boolean deep) {
        Objects.requireNonNull(entityInstanceBefore,
            "entityInstanceBefore is required to be not null, when calling 'detectChanges'!");
        Objects.requireNonNull(entityInstanceAfter,
            "entityInstanceAfter is required to be not null, when calling 'detectChanges'!");

        var em = Domain.entityMirrorFor(entityInstanceBefore);
        var accessorBefore = DlcAccess.accessorFor(entityInstanceBefore);
        var accessorAfter = DlcAccess.accessorFor(entityInstanceAfter);
        final Set<DetectedChange> changes = new HashSet<>();
        em.getValueReferences()
            .stream()
            .filter(fm -> !fm.isStatic())
            .forEach(v -> changes.addAll(changesForField(v, accessorBefore, accessorAfter, deep)));
        em.getBasicFields()
            .stream()
            .filter(fm -> !fm.isStatic())
            .forEach(f -> changes.addAll(changesForField(f, accessorBefore, accessorAfter, deep)));
        if (deep) {
            em.getEntityReferences()
                .forEach(er -> changes.addAll(changesForField(er, accessorBefore, accessorAfter, deep)));
        }
        return changes;
    }

    private static Set<DetectedChange> changesForField(FieldMirror fm,
                                                       DynamicDomainObjectAccessor accessorBefore,
                                                       DynamicDomainObjectAccessor accessorAfter,
                                                       boolean deep) {
        Object newValue = accessorAfter.peek(fm.getName());
        Object oldValue = accessorBefore.peek(fm.getName());
        Set<DetectedChange> changes = new HashSet<>();
        if (!fm.getType().hasCollectionContainer()) {
            if (newValue == null && oldValue != null) {
                changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                    (Entity<?>) accessorAfter.getAssigned(), oldValue, newValue, DetectedChange.ChangeType.REMOVED,
                    null));
            } else if (newValue != null && oldValue == null) {
                changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                    (Entity<?>) accessorAfter.getAssigned(), oldValue, newValue, DetectedChange.ChangeType.ADDED,
                    null));
            } else if ((newValue != null && !newValue.equals(oldValue))) {
                changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                    (Entity<?>) accessorAfter.getAssigned(), oldValue, newValue, DetectedChange.ChangeType.CHANGED,
                    null));
            } else if (newValue != null && newValue.equals(oldValue)) {
                var type =
                    Domain.typeMirror(newValue.getClass().getName())
                        .map(DomainTypeMirror::getDomainType)
                        .orElse(DomainType.NON_DOMAIN);
                if (DomainType.ENTITY.equals(type)) {
                    Set<DetectedChange> innerChanges = detectChanges((Entity<?>) oldValue, (Entity<?>) newValue, deep);
                    if (innerChanges.size() > 0) {
                        changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                            (Entity<?>) accessorAfter.getAssigned(), oldValue, newValue,
                            DetectedChange.ChangeType.CHANGED, innerChanges));
                    }
                }
            }
        } else {
            Collection<?> newValues = (Collection<?>) newValue;
            Collection<?> oldValues = (Collection<?>) oldValue;
            if (newValues == null || newValues.isEmpty()) {
                if (oldValues != null && !((Collection<?>) oldValue).isEmpty()) {
                    oldValues.forEach(ov -> changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                        (Entity<?>) accessorAfter.getAssigned(), ov, null, DetectedChange.ChangeType.REMOVED, null)));
                }
            } else {
                newValues.forEach(nv -> {
                    if (oldValues == null || !oldValues.contains(nv)) {
                        changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                            (Entity<?>) accessorAfter.getAssigned(), null, nv, DetectedChange.ChangeType.ADDED, null));
                    } else {
                        @SuppressWarnings("OptionalGetWithoutIsPresent") Object ov = oldValues.stream().filter(
                            e -> e.equals(nv)).findFirst().get();
                        var type =
                            Domain.typeMirror(newValue.getClass().getName())
                                .map(dtm -> dtm.getDomainType())
                                .orElse(DomainType.NON_DOMAIN);
                        if (DomainType.ENTITY.equals(type)) {
                            Set<DetectedChange> innerChanges = detectChanges((Entity<?>) ov, (Entity<?>) nv, deep);
                            if (innerChanges.size() > 0) {
                                changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                                    (Entity<?>) accessorAfter.getAssigned(), ov, nv, DetectedChange.ChangeType.CHANGED,
                                    innerChanges));
                            }
                        }
                    }
                });
                if (oldValues != null) {
                    oldValues.forEach(ov -> {
                        if (!newValues.contains(ov)) {
                            changes.add(new DetectedChange(fm, (Entity<?>) accessorBefore.getAssigned(),
                                (Entity<?>) accessorAfter.getAssigned(), ov, null, DetectedChange.ChangeType.REMOVED,
                                null));
                        }
                    });
                }

            }
        }
        return changes;
    }

    /**
     * Value carrier for the details of a detected change reported by "detectChanges".
     *
     * @param changedField the field which value has been changed
     * @param entityBefore state of entity before change
     * @param entityAfter  state of entity after change
     * @param valueBefore  value before change
     * @param valueAfter   value after change
     * @param changeType   type of change
     * @param innerChanges inner changes done
     */
    public record DetectedChange(FieldMirror changedField,
                                 Entity<?> entityBefore, Entity<?> entityAfter,
                                 Object valueBefore, Object valueAfter,
                                 Entities.DetectedChange.ChangeType changeType,
                                 Set<DetectedChange> innerChanges) {
        /**
         * Represents the type of change detected in an entity.
         */
        public enum ChangeType {
            /**
             * Indicates that an existing entity or value was modified.
             * This constant is part of the {@code ChangeType} enumeration,
             * which is used to represent the type of change detected in an entity.
             */
            CHANGED,
            /**
             * Represents the addition of a new entity or value.
             * This constant is part of the {@code ChangeType} enumeration,
             * which is used to represent the type of change detected in an entity.
             */
            ADDED,
            /**
             * Represents the removal of an existing entity or value.
             * This constant is part of the {@code ChangeType} enumeration,
             * which is used to represent the type of change detected in an entity.
             */
            REMOVED
        }
    }


}

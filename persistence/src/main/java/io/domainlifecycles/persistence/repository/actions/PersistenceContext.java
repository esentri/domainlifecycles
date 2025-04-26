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

package io.domainlifecycles.persistence.repository.actions;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.clone.EntityCloner;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * The persistence context keeps all the actions, that were detected on all the
 * entities of the processed aggregate. To be able to sort those actions in the
 * order in which they must be applied we keep the in a map. Within this map the
 * actions are partitioned by it's entity class and by it's action type () eg.
 * INSERT, UPDATE,...)
 * <p>
 * We also keep all the actions in notification order. We want the notifications
 * to be emitted in post order (bottom up). but the changes have to be applied
 * to the database in a different order, which depends on the constraints of the
 * database relations and database structures.
 *
 * @param <BASE_RECORD_TYPE> the type of the record that is being persisted
 * @author Mario Herb
 */
public class PersistenceContext<BASE_RECORD_TYPE> {

    /**
     * The root class of the aggregate that is being persisted.
     */
    public final Class<? extends AggregateRoot<?>> rootClass;
    //actions partitioned by entity type and action type
    private final Map<String, List<PersistenceAction<BASE_RECORD_TYPE>>> partitionedActionsMap = new HashMap<>();
    //needed when the same entity (same id) is contained in tree within different java references
    //we allow that to make things simpler, when aggregates are created by deserialization for example
    private final Map<Entity<?>, List<Entity<?>>> entityDuplicates = new HashMap<>();

    private boolean rootContainsChange = false;

    private boolean rootUpdatedDirectly = false;

    private final AggregateRoot<?> updatedRoot;
    private final FetcherResult<?, BASE_RECORD_TYPE> databaseStateRootFetched;

    /**
     * The access model of the updated root.
     */
    public final DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> updatedRootAccessModel;

    /**
     * The access model of the database state root.
     */
    public final DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> databaseStateRootAccessModel;

    private final Map<ValueObject, BASE_RECORD_TYPE> newValueObjectRecords = new IdentityHashMap<>();
    private final EntityCloner entityCloner;
    private final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider;

    /**
     * Constructs a PersistenceContext instance to manage and track persistence operations and changes
     * for a specific aggregate root in a domain-driven design context. The context allows comparison
     * between the updated state and the database state, detects changes, and organizes persistence actions.
     *
     * @param domainPersistenceProvider the persistence provider responsible for handling domain-specific
     *                                  persistence logic and configurations
     * @param updatedRoot              the aggregate root containing the updated state that needs to be persisted.
     *                                  This can be null if no updates occur
     * @param databaseStateRootFetched the previously persisted state of the aggregate root as fetched from
     *                                  the database, which is used for comparison to detect changes
     */
    public PersistenceContext(
        DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider,
        AggregateRoot<?> updatedRoot,
        FetcherResult<?, BASE_RECORD_TYPE> databaseStateRootFetched) {
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.updatedRoot = updatedRoot;
        this.databaseStateRootFetched = databaseStateRootFetched;

        this.entityCloner = new EntityCloner(
            domainPersistenceProvider.domainPersistenceConfiguration.domainObjectBuilderProvider);
        this.rootClass = (Class<? extends AggregateRoot<?>>) Objects.requireNonNullElseGet(updatedRoot,
            () -> databaseStateRootFetched.resultValue().get()).getClass();
        if (updatedRoot != null) {
            updatedRootAccessModel = domainPersistenceProvider.buildAccessModel(updatedRoot);
        } else {
            updatedRootAccessModel = null;
        }

        if (databaseStateRootFetched != null && databaseStateRootFetched.resultValue().isPresent()) {
            databaseStateRootAccessModel = domainPersistenceProvider.buildAccessModel(
                databaseStateRootFetched.resultValue().get());
        } else {
            databaseStateRootAccessModel = null;
        }

        detectChanges();
    }

    /**
     * Gets the record type for a value object
     *
     * @param IValueObject the value object
     * @return the root class of the value object that is being persisted
     */
    public BASE_RECORD_TYPE getNewValueObjectRecord(ValueObject IValueObject) {
        return newValueObjectRecords.get(IValueObject);
    }

    /**
     * Adds a new ValueObject to the persistence context.
     *
     * @param valueObject the value object
     * @param record      the record
     */
    public void addNewValueObjectRecord(ValueObject valueObject, BASE_RECORD_TYPE record) {
        newValueObjectRecords.put(valueObject, record);
    }

    private PersistenceAction<BASE_RECORD_TYPE> addToPartitionedActionsMap(PersistenceAction<BASE_RECORD_TYPE> a) {
        checkActionConsistency(a);
        var key = a.instanceAccessModel.instanceType().getName() + "-" + a.actionType.name();
        var actionList = partitionedActionsMap.computeIfAbsent(key, k -> new ArrayList<>());
        boolean contained = actionList
            .stream()
            .anyMatch(t -> t.instanceAccessModel.domainObject().equals(a.instanceAccessModel.domainObject()));
        if (a.instanceAccessModel.isValueObject() || (a.instanceAccessModel.isEntity() && !contained)) {
            actionList.add(a);
        } else if (a.instanceAccessModel.isEntity()) {
            actionList.iterator().forEachRemaining(
                aq -> {
                    if (aq.instanceAccessModel.domainObject().equals(a.instanceAccessModel.domainObject())) {
                        if (hasChangesCompareFieldByField((Entity<?>) aq.instanceAccessModel.domainObject(),
                            (Entity<?>) a.instanceAccessModel.domainObject())) {
                            throw DLCPersistenceException.fail("""
                                    Inconsistent aggregate. The same entity is contained with different values.

                                    Entity 1: {0}\s

                                    Entity 2: {1}""", a.instanceAccessModel.domainObject(),
                                aq.instanceAccessModel.domainObject());
                        } else {
                            //this domain object instance is contained multiple times in the aggregate
                            //but it's not the same reference (which should be allowed, see DDD equality rules)
                            //e.g. happens sometimes at m-n relationships and e.g. deserialization
                            //we store duplicate references, to make them represent the changes applied to the db
                            if (aq.instanceAccessModel.domainObject() != a.instanceAccessModel.domainObject()) {
                                var duplicates = entityDuplicates.get(aq.instanceAccessModel.domainObject());
                                if (duplicates == null) {
                                    duplicates = new ArrayList<>();
                                    entityDuplicates.put((Entity<?>) aq.instanceAccessModel.domainObject(), duplicates);
                                }
                                duplicates.add((Entity<?>) a.instanceAccessModel.domainObject());
                            }
                        }
                    }
                }
            );
        }

        return a;
    }

    /**
     * if an action of another type was detected for the same entity, the
     * aggregate is not consistent, we should throw an exception (probably that
     * was some kind of programming error)
     *
     * @param a the action to check the consistency for
     */
    private void checkActionConsistency(PersistenceAction<BASE_RECORD_TYPE> a) {
        if (Entity.class.isAssignableFrom(a.instanceAccessModel.instanceType())) {
            List<PersistenceAction<BASE_RECORD_TYPE>> actionList;
            if (!PersistenceAction.ActionType.INSERT.equals(a.actionType)) {
                var insertKey =
                    a.instanceAccessModel.instanceType().getName() + "-" + PersistenceAction.ActionType.INSERT;
                actionList = partitionedActionsMap.get(insertKey);
                if (actionList != null) {
                    actionList.forEach(aq -> {
                        if (aq.instanceAccessModel.domainObject().equals(a.instanceAccessModel.domainObject())) {
                            throw DLCPersistenceException.fail("""
                                Inconsistent aggregate. The same entity is contained with different actions.

                                Action 1: %s\s

                                Action 2: %s""", a, aq);
                        }
                    });
                }
            }
            if (!(PersistenceAction.ActionType.UPDATE.equals(
                a.actionType) || PersistenceAction.ActionType.DELETE_UPDATE.equals(a.actionType))) {
                var updateKey =
                    a.instanceAccessModel.instanceType().getName() + "-" + PersistenceAction.ActionType.UPDATE;
                actionList = partitionedActionsMap.get(updateKey);
                if (actionList != null) {
                    actionList.forEach(aq -> {
                        if (aq.instanceAccessModel.domainObject().equals(a.instanceAccessModel.domainObject())) {
                            throw DLCPersistenceException.fail("""
                                Inconsistent aggregate. The same entity is contained with different actions.

                                Action 1: %s\s

                                Action 2: %s""", a, aq);
                        }
                    });
                }
            }
            if (!PersistenceAction.ActionType.DELETE.equals(a.actionType)) {
                var updateKey =
                    a.instanceAccessModel.instanceType().getName() + "-" + PersistenceAction.ActionType.DELETE;
                actionList = partitionedActionsMap.get(updateKey);
                if (actionList != null) {
                    actionList.forEach(aq -> {
                        if (aq.instanceAccessModel.domainObject().equals(a.instanceAccessModel.domainObject())) {
                            throw DLCPersistenceException.fail("""
                                Inconsistent aggregate. The same entity is contained with different actions.

                                Action 1: %s\s

                                Action 2: %s""", a, aq);
                        }
                    });
                }
            }
        }
    }

    private void addActionToPersistenceContext(PersistenceAction<BASE_RECORD_TYPE> a) {
        addToPartitionedActionsMap(a);
    }

    /**
     * Gets the required persistence actions
     *
     * @param domainObjectTypeName the full qualified domain object type
     * @param actionType           the action type
     * @return the actions
     */
    public List<PersistenceAction<BASE_RECORD_TYPE>> getActionsPartitioned(String domainObjectTypeName,
                                                                          PersistenceAction.ActionType actionType) {
        var actions = this.partitionedActionsMap.get(domainObjectTypeName + "-" + actionType.name());
        if (actions == null) {
            return new ArrayList<>();
        }
        return actions;
    }

    /**
     * Gets the required persistence acctions in the order in which they need to published for notifications.
     *
     * @return the actions
     */
    public List<PersistenceAction<BASE_RECORD_TYPE>> getActionsInNotificationOrder() {
        return this.partitionedActionsMap
            .values()
            .stream()
            .flatMap(Collection::stream)
            .sorted((a1, a2) -> {
                //descending order by access path lengths --> ordered from leafs to root
                return Integer.compare(a2.instanceAccessModel.structuralPosition.accessPathFromRoot.size(),
                    a1.instanceAccessModel.structuralPosition.accessPathFromRoot.size());
            })
            .collect(Collectors.toList());
    }

    /**
     * Gets the state root fetched from the database
     *
     * @return the state root
     */
    public FetcherResult<?, BASE_RECORD_TYPE> getDatabaseStateRootFetched() {
        return databaseStateRootFetched;
    }

    /**
     * Compares two entities field by field to detect if there are differences in their values.
     * The method checks specific types of fields such as value objects, identities, enums, and non-domain types.
     * It considers collections and their elements for changes, ensuring a comprehensive comparison.
     *
     * @param a the first entity to compare
     * @param b the second entity to compare
     * @return true if there are changes between the two entities, false otherwise
     */
    protected boolean hasChangesCompareFieldByField(Entity<?> a, Entity<?> b) {
        var em = Domain.entityMirrorFor(a);

        return em.getAllFields()
            .stream()
            .filter(f ->
                f.getType().getDomainType().equals(DomainType.VALUE_OBJECT) ||
                    f.getType().getDomainType().equals(DomainType.IDENTITY) ||
                    f.getType().getDomainType().equals(DomainType.ENUM) ||
                    f.getType().getDomainType().equals(DomainType.NON_DOMAIN)
            ).anyMatch(f -> {
                var propertyValueE1 = DlcAccess.accessorFor(a).peek(f.getName());
                var propertyValueE2 = DlcAccess.accessorFor(b).peek(f.getName());

                if (f.getType().hasCollectionContainer()) {
                    if (propertyValueE1 == null && propertyValueE2 != null && ((Collection<?>) propertyValueE2).isEmpty()) {
                        return false;
                    } else if (propertyValueE2 == null && propertyValueE1 != null && ((Collection<?>) propertyValueE1).isEmpty()) {
                        return false;
                    } else if (propertyValueE2 == null && propertyValueE1 == null) {
                        return false;
                    } else if (propertyValueE2 != null && propertyValueE1 != null) {
                        Collection<?> c1 = (Collection<?>) propertyValueE1;
                        Collection<?> c2 = (Collection<?>) propertyValueE2;
                        return !c1.containsAll(c2) || !c2.containsAll(c1);
                    }
                    return true;
                } else
                    return (propertyValueE1 != null && !propertyValueE1.equals(propertyValueE2)) ||
                        (propertyValueE1 == null && propertyValueE2 != null);
            });

    }

    /**
     * Gets the entity duplicates
     *
     * @return the entity duplicates
     */
    public Map<Entity<?>, List<Entity<?>>> getEntityDuplicates() {
        return entityDuplicates;
    }

    /**
     * Whether the root was updated directly
     *
     * @return true if the root was updated directly
     */
    public boolean isRootUpdatedDirectly() {
        return this.rootUpdatedDirectly;
    }

    /**
     * Whether the root contains a change
     *
     * @return true if the root contains a change
     */
    public boolean containsChange() {
        return this.rootContainsChange;
    }

    /**
     * Gets the processed root
     *
     * @return the processed root
     */
    public AggregateRoot<?> getProcessedRoot() {
        if (updatedRoot == null) {
            return databaseStateRootFetched.resultValue().get();
        }
        return updatedRoot;
    }

    private void detectChanges() {
        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedDatabaseDomainObjects;
        if (databaseStateRootAccessModel != null) {
            recordMappedDatabaseDomainObjects = databaseStateRootAccessModel
                .getAllContainedInstances()
                .stream()
                .filter(DomainObjectInstanceAccessModel::isRecordMapped)
                .filter(i -> !i.structuralPosition.isBackReference)
                .collect(Collectors.toList());
        } else {
            recordMappedDatabaseDomainObjects = new ArrayList<>();
        }

        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedUpdatedDomainObjects;
        if (updatedRootAccessModel != null) {
            recordMappedUpdatedDomainObjects = updatedRootAccessModel
                .getAllContainedInstances()
                .stream()
                .filter(DomainObjectInstanceAccessModel::isRecordMapped)
                .filter(i -> !i.structuralPosition.isBackReference)
                .collect(Collectors.toList());
            //check consistency
            recordMappedUpdatedDomainObjects.stream()
                .filter(DomainObjectInstanceAccessModel::isEntity)
                .map(i -> ((Entity<?>) i.domainObject()))
                .collect(Collectors.groupingBy(Function.identity()))
                .values()
                .stream()
                .filter(x -> x.size() > 1)
                .forEach(x -> {
                    for (Entity<?> a : x) {
                        for (Entity<?> b : x) {
                            if (hasChangesCompareFieldByField(a, b)) {
                                throw DLCPersistenceException.fail(
                                    "Inconsistent Aggregate: Entity is contained multiple times with different values\n"
                                        + " Entity instance 1: " + a + "\n"
                                        + " Entity instance 2: " + b);
                            }
                        }
                    }
                });
        } else {
            recordMappedUpdatedDomainObjects = new ArrayList<>();
        }

        //first we detect all DELETEs = domain objects that were in the database and are not contained in roots
        // current representation anymore
        detectDeletes(recordMappedDatabaseDomainObjects, recordMappedUpdatedDomainObjects);
        //now we detect all UPDATEs and INSERTs
        detectUpdatesOrInserts(recordMappedDatabaseDomainObjects, recordMappedUpdatedDomainObjects);
    }

    private void detectDeletes(
        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedDatabaseDomainObjects,
        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedUpdatedDomainObjects
    ) {
        Set<Entity<?>> deletedEntities = new HashSet<>();
        Map<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>, Long> voDeletesDetected = new HashMap<>();
        recordMappedDatabaseDomainObjects
            .forEach(
                i -> {
                    if(i.isValueObject()){
                        var dbInstancesCnt = recordMappedDatabaseDomainObjects
                            .stream()
                            .filter(inst-> inst.equals(i))
                            .count();
                        var updatedCnt = recordMappedUpdatedDomainObjects
                            .stream()
                            .filter(inst-> inst.equals(i))
                            .count();
                        var toDeleteCnt = dbInstancesCnt- updatedCnt;
                        if(dbInstancesCnt>updatedCnt && (!voDeletesDetected.keySet().contains(i)||voDeletesDetected.get(i)<toDeleteCnt)) {
                            addActionToPersistenceContext(
                                new PersistenceAction<>(i, PersistenceAction.ActionType.DELETE, null)
                            );
                            this.rootContainsChange = true;
                            var cnt = voDeletesDetected.get(i);
                            if (cnt == null) {
                                cnt = Long.valueOf(0);
                            }
                            cnt = cnt + 1;
                            voDeletesDetected.put(i, cnt);
                        }
                    }else {
                        if (!recordMappedUpdatedDomainObjects.contains(i)) {
                            //check if entity is still referenced elsewhere
                            boolean stillReferenced = recordMappedUpdatedDomainObjects
                                .stream()
                                .filter(u -> !u.equals(i))
                                .map(DomainObjectInstanceAccessModel::domainObject).toList()
                                .contains(i.domainObject());
                            if (!stillReferenced) {
                                deletedEntities.add((Entity<?>) i.domainObject());
                                addActionToPersistenceContext(
                                    new PersistenceAction<>(i, PersistenceAction.ActionType.DELETE, null));
                                this.rootContainsChange = true;
                            }
                        }

                    }
                }
            );
        addActionsToResetForwardReferencesOnDeletedEntities(
            deletedEntities,
            recordMappedDatabaseDomainObjects,
            recordMappedUpdatedDomainObjects
        );
    }

    private void detectUpdatesOrInserts(
        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedDatabaseDomainObjects,
        List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedUpdatedDomainObjects
    ) {
        Map<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>, Long> voInsertsDetected = new HashMap<>();
        recordMappedUpdatedDomainObjects.forEach(
            i -> {
                List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> dbStateInstances = fetchInstances(
                    recordMappedDatabaseDomainObjects, i);
                List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> updatedInstances = fetchInstances(
                    recordMappedUpdatedDomainObjects, i);
                var addedCnt = updatedInstances.size() - dbStateInstances.size();
                if(addedCnt > 0) {
                    if(i.isValueObject() && (!voInsertsDetected.containsKey(i) || voInsertsDetected.get(i)<addedCnt)) {
                        addActionToPersistenceContext(
                            new PersistenceAction<>(i, PersistenceAction.ActionType.INSERT, null));
                        this.rootContainsChange = true;
                        var cnt = voInsertsDetected.get(i);
                        if (cnt == null) {
                            cnt = Long.valueOf(0);
                        }
                        cnt = cnt + 1;
                        voInsertsDetected.put(i, cnt);
                    }else if(i.isEntity()){
                        if(addedCnt>1){
                            throw DLCPersistenceException.fail("More than one instance of the same entity passed to be inserted %s", i);
                        }
                        addActionToPersistenceContext(
                            new PersistenceAction<>(i, PersistenceAction.ActionType.INSERT, null));
                        this.rootContainsChange = true;
                        if (i.domainObject().equals(this.updatedRoot)) {
                            this.rootUpdatedDirectly = true;
                        }
                    }
                } else if (addedCnt == 0 && i.isEntity() && !dbStateInstances.isEmpty()) {
                    var dbStateInstance = dbStateInstances.get(0);
                    if( hasChangesCompareFieldByField(
                            (Entity<?>) i.domainObject(),
                            (Entity<?>) dbStateInstance.domainObject()
                        )
                    ) {
                        //change detected when comparing database state to a given entity - value objects cannot be updated
                        addActionToPersistenceContext(
                            new PersistenceAction<>(i, PersistenceAction.ActionType.UPDATE, dbStateInstance));
                        this.rootContainsChange = true;
                        if (i.domainObject().equals(this.updatedRoot)) {
                            this.rootUpdatedDirectly = true;
                        }
                    }
                }
            }
        );
    }

    private void addActionsToResetForwardReferencesOnDeletedEntities(final Set<Entity<?>> deletedEntities,
                                                                     List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedDatabaseDomainObjects,
                                                                     List<DomainObjectInstanceAccessModel<BASE_RECORD_TYPE>> recordMappedUpdatedDomainObjects) {
        if (!deletedEntities.isEmpty()) {
            recordMappedDatabaseDomainObjects
                .stream()
                .filter(DomainObjectInstanceAccessModel::isEntity)
                .filter(i -> !deletedEntities.contains(i.domainObject()))
                .forEach(i -> {
                    var resetted = resetForwardReferenceOnDeletedEntity((Entity<?>) i.domainObject(), deletedEntities);
                    if (resetted != null) {
                        var oldInstances = fetchInstances(recordMappedUpdatedDomainObjects, i);
                        if(oldInstances.size() != 1){
                            throw DLCPersistenceException.fail("Resetting forward reference failed on %s", i);
                        }
                        var oldInstance = oldInstances.get(0);
                        var resettedMirror = Domain.entityMirrorFor(resetted);

                        if (resettedMirror.getConcurrencyVersionField().isPresent()) {
                            //needed because of update to reset reference
                            Entity<?> toIncreaseVersion = (Entity<?>) oldInstance.domainObject();
                            var em = Domain.entityMirrorFor(toIncreaseVersion);
                            if (em.getConcurrencyVersionField().isPresent()) {
                                DlcAccess.accessorFor(toIncreaseVersion)
                                    .poke(
                                        em.getConcurrencyVersionField().get().getName(),
                                        toIncreaseVersion.concurrencyVersion() + 1
                                    );
                                if (updatedRoot.equals(resetted)) {
                                    this.rootUpdatedDirectly = true;
                                }
                            }
                        }
                        var action = new PersistenceAction<>(oldInstance.cloneWithReplacement(resetted),
                            PersistenceAction.ActionType.DELETE_UPDATE, null);
                        addActionToPersistenceContext(action);
                    }
                });
        }
    }

    private Entity<?> resetForwardReferenceOnDeletedEntity(Entity<?> referencingEntity,
                                                           Set<Entity<?>> deletedEntities) {

        var referencingRecordMirror = domainPersistenceProvider.persistenceMirror.getEntityRecordMirror(
            referencingEntity.getClass().getName());

        if (referencingRecordMirror
            .enforcedReferences()
            .stream()
            .anyMatch(ref -> deletedEntities.stream().anyMatch(del -> del.getClass().getName().equals(ref)))) {
            //an enforced reference is deleted
            var referencingMirror = Domain.entityMirrorFor(referencingEntity);
            Entity<?> clone = entityCloner.clone(referencingEntity);
            var accessor = DlcAccess.accessorFor(clone);
            AtomicBoolean resetted = new AtomicBoolean(false);
            for (Entity<?> deletedEntity : deletedEntities) {
                referencingMirror
                    .getEntityReferences()
                    .stream()
                    .filter(er -> er.getType().getTypeName().equals(deletedEntity.getClass().getName()))
                    .forEach(er -> {
                        var referenced = accessor.peek(er.getName());
                        if (referenced != null) {
                            if (er.getType().hasOptionalContainer()) {
                                var referencedOptional = ((Optional<?>) referenced);
                                if (referencedOptional.isPresent() && referencedOptional.get().equals(deletedEntity)) {
                                    accessor.poke(er.getName(), Optional.empty());
                                    resetted.set(true);
                                }
                            } else if (deletedEntity.equals(referenced)) {
                                accessor.poke(er.getName(), null);
                                resetted.set(true);
                            }
                        }
                    });
            }
            if (resetted.get()) {
                return clone;
            }
        }
        return null;
    }

    private <T> List<T> fetchInstances(List<T> list, T toSearch) {
        var returnList = new ArrayList<T>();
        for (T instance : list) {
            if (instance.equals(toSearch)) {
                returnList.add(instance);
            }
        }
        return returnList;
    }

}

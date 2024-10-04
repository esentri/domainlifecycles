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

package io.domainlifecycles.persistence.repository;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;
import io.domainlifecycles.persistence.repository.order.PersistenceActionOrderProvider;
import io.domainlifecycles.persistence.repository.order.TopologicalPersistenceActionOrderProvider;
import io.domainlifecycles.persistence.repository.persister.Persister;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 *  <p>
 * Aggregate roots need to offer INSERT, UPDATE
 * and DELETE operations in a consistent way for all entities that are part of
 * the aggregate. In DDD the only way to manipulate entities persistently is
 * through the repository of the aggregate. The repository implementation must
 * ensure the consistency of the aggregate. To be able to offer INSERT, UPDATE
 * DELETE on a possibly complex object tree (of entities and value objects) that is formed by
 * the instances of an aggregate, we have to deal with several standard
 * problems, which are solved by this implementation.
 * </p>
 * <p>
 * As such, it's no more in the reponsibility of the developer to keep all
 * these things in mind when implementing a repository ;-) --> e.g. just call
 * "update aggregate" and can be happy with that
 * </p>
 * <p>
 * Having "real DDD style" entities without getters and setters for all
 * persisted fields, we need to updates values by reflection, because we deny an
 * "aenemic" entity model, with getters and setters for each and everything
 * </p>
 * Dealing with database constraints:
 * <ul>
 * <li>INSERTs and DELETEs on database representations of entities must be done in a specific order, because we have
 * foreign key constraints, that enforce us to to do these operations in an order
 * that doesn't produce constraint violations (we know some databases offer
 * "deferred" constraints but not all databases do so). E.g. a table entry is
 * tried to be deleted and we still have "child" table entries of 1-n, m-n, or
 * 1-1 relations, which reference the entry being deleted</li>
 * <li>When deleting an
 * entry and another table references this entry by a foreign key, before the
 * delete an update must be performed to set the reference "null"</li>
 * <li>We could
 * have unique constraints: INSERTs, UPDATEs and DELETEs performed on an entity
 * (on a table) which might have a unique constraint must be done in the order
 * DELETE -> UPDATE -> INSERT. So a violation on that constraint should only
 * occur if a "real" violation happens and not because of wrong order of
 * persistence operations in the implementation</li>
 * </ul>
 * In case of "optimistic locking":
 * <ul>
 * <li>Securing the aggregate root of possible inconsistencies in case
 * of concurrent operations, by increasing the version number of the aggregate
 * root entity (and its db representation), even in cases if no direct update
 * happened on the root entity.
 * </li>
 * </ul>
 * This implementation provides a basis for "after persistence" domain events:
 * <ul>
 * <li>
 * After having performed all persistence operations on the database, we can
 * expect the transaction to COMMIT. Then we offer the ability to emit events on any
 * kind of eventing mechanism.
 * </li>
 * <li>
 * We make sure we're applying persistence actions
 * (INSERT, UPDATE, DELETE) on any kind of aggregate. All those changes are
 * consistently represented in the java reference of the root object that was
 * processed. So we don't have any kind of stale object representation
 * afterwards.
 * </li>
 * <li>
 * This is especially important when dealing with any kind of
 * optimistic locking. An updated version number must be delivered back to be
 * able to the caller, so that he still can apply other "persistence" calls later
 * on.
 * </li>
 * </ul>
 *
 * @param <I> the type of the identity of the aggregate root
 * @param <A> the type of the aggregate root
 * @param <BASE_RECORD_TYPE> the type of the database representation of the record
 *
 * @author Mario Herb
 */
public abstract class DomainStructureAwareRepository<I extends Identity<?>, A extends AggregateRoot<I>, BASE_RECORD_TYPE> implements Repository<I, A> {

    private final Persister<BASE_RECORD_TYPE> persister;

    private final PersistenceActionOrderProvider persistenceActionOrderProvider;

    private final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider;


    protected DomainStructureAwareRepository(final Persister<BASE_RECORD_TYPE> persister,
                                             final DomainPersistenceProvider<BASE_RECORD_TYPE> domainPersistenceProvider) {
        this.persister = persister;
        this.persistenceActionOrderProvider = new TopologicalPersistenceActionOrderProvider(domainPersistenceProvider);
        this.domainPersistenceProvider = domainPersistenceProvider;
    }


    /**
     * To insert an aggregate into the database. The aggregate root an all its
     * contained entities are inserted in a consistent way into the database.
     *
     * @param root the aggregate root entity
     * @return the reference of the passed root object to be inserted
     */
    public A insert(A root) {
        Objects.requireNonNull(root);
        processAggregates(root, null);
        return root;
    }

    /**
     * To update an aggregate to the database. The aggregate root an all its
     * contained entities are updated in a consistent way into the database. An
     * update of the root can also mean that entities contained werde added to
     * (INSERT) or deleted (DELETE) from the database.
     *
     * @param root the aggregate root entity
     * @return the reference of the passed root object to be inserted
     */
    public A update(A root) {
        Objects.requireNonNull(root);
        var rootCurrentDatabaseState = findResultById((I) domainPersistenceProvider.getId(root));
        if (rootCurrentDatabaseState.resultValue().isPresent()) {
            processAggregates(root, rootCurrentDatabaseState);
            return root;
        }
        throw DLCPersistenceException.fail("The given root was not found in the database! Root:" + root);
    }

    /**
     * Increases the version of the aggregate root entity.
     *
     * @param root the aggregate root entity
     * @return the reference of the passed root object to be inserted
     */
    public A increaseVersion(A root) {
        Objects.requireNonNull(root);
        var rootCurrentDatabaseState = findResultById((I) domainPersistenceProvider.getId(root));
        if (rootCurrentDatabaseState.resultValue().isPresent()) {
            var pc = new PersistenceContext<>(domainPersistenceProvider, root, rootCurrentDatabaseState);
            persister.increaseVersion(rootCurrentDatabaseState.resultValue().get(), pc);
            return root;
        }
        throw DLCPersistenceException.fail("The given root was not found in the database! Root:" + root);
    }

    /**
     * To delete an aggregate from the database. The aggregate root an all its
     * contained entities are deleted in a consistent way from the database.
     *
     * @param id the id of the aggregate root entity
     * @return the reference of the passed root object to be inserted
     */
    public Optional<A> deleteById(I id) {
        Objects.requireNonNull(id);
        var rootCurrentDatabaseState = findResultById(id);
        if (rootCurrentDatabaseState.resultValue().isPresent()) {
            processAggregates(null, rootCurrentDatabaseState);
            return rootCurrentDatabaseState.resultValue();
        }
        return Optional.empty();
    }

    protected void processAggregates(A rootUpdated, FetcherResult<A, BASE_RECORD_TYPE> databaseStateRoot) {
        var pc = new PersistenceContext<>(domainPersistenceProvider, rootUpdated, databaseStateRoot);

        //after having detected all PersistenceActions we apply them on the database
        this.processPersistenceActions(pc);

        //must be done after having applied all the other actions
        boolean isRootVersionIncreasedWithoutPropertyUpdate = false;
        if (rootUpdated != null && pc.containsChange() && !pc.isRootUpdatedDirectly()) {
            //this ensures a consistent aggregate in case of concurrent database transactions
            //as long as we use optimistic locking
            persister.increaseVersion(rootUpdated, pc);
            isRootVersionIncreasedWithoutPropertyUpdate = true;
        }

        this.notifyChanges(pc, rootUpdated);

        if (isRootVersionIncreasedWithoutPropertyUpdate) {
            var rm = (RecordMapper<BASE_RECORD_TYPE, DomainObject, AggregateRoot<?>>) pc.updatedRootAccessModel.recordMirror.recordMapper();
            BASE_RECORD_TYPE rootRecord = rm.from(pc.updatedRootAccessModel.domainObject(), (AggregateRoot<?>) pc.updatedRootAccessModel.domainObject());
            PersistenceAction<BASE_RECORD_TYPE> updateAction = new PersistenceAction<>(pc.updatedRootAccessModel, PersistenceAction.ActionType.UPDATE, pc.databaseStateRootAccessModel);
            updateAction.setActionRecord(rootRecord);
            publish(updateAction);
        }
    }

    /**
     * To notify about changes already applied to the database
     *
     * @param pc   PersistenceContext which keeps all detected actions (in
     *              notification order --> "bottom up")
     * @param root the root entity of the aggregate
     */
    protected void notifyChanges(PersistenceContext<BASE_RECORD_TYPE> pc, A root) {
        pc.getActionsInNotificationOrder().forEach(action -> {
            if (PersistenceAction.ActionType.DELETE_UPDATE.equals(action.actionType)) {
                //delete updates must only be published, if not another update had happened on that entity
                //that means only if the reference of a deleted child entity was "nulled"
                // the update on the root is published in any case, if it works with optimistic locking
                // (@see ConcurrencySafeEntity)
                if (!updateActionContainedForAccessModel(action.instanceAccessModel, pc)
                    && (root != null && !root.equals(action.instanceAccessModel.domainObject()))) {
                    publish(action);
                }
            } else {
                publish(action);
            }
        });
    }

    private boolean updateActionContainedForAccessModel(DomainObjectInstanceAccessModel<BASE_RECORD_TYPE> accessModel, PersistenceContext<BASE_RECORD_TYPE> pc) {
        return pc.getActionsInNotificationOrder()
            .stream()
            .anyMatch(a -> a.instanceAccessModel.equals(accessModel) && PersistenceAction.ActionType.UPDATE.equals(a.actionType));
    }

    /**
     * This method takes care of applying the detected change actions (@see
     * EntityPersistenceAction) to the underlying database. The order in which
     * these changes are applied to the database is very important. It depends
     * on the structure constraints that the database relations have to comply
     * with. For most relational databases foreign key constraints and unique
     * constraint have to be considered.
     * <p>
     * The persistenceActionOrderProvider delivers the order of the entity
     * classes that has to be considered.
     *
     * @param context maintaining all the changes detected.
     */
    protected void processPersistenceActions(final PersistenceContext<BASE_RECORD_TYPE> context) {

        var insertionOrderClasses = persistenceActionOrderProvider.insertionOrder(context.rootClass.getName());
        if (insertionOrderClasses == null || insertionOrderClasses.isEmpty()) {
            throw DLCPersistenceException.fail("The insertion order was not defined! Check the mirror! RootClass '%s'.", context.rootClass);
        }

        insertionOrderClasses.forEach(c ->
            context.getActionsPartitioned(c, PersistenceAction.ActionType.DELETE_UPDATE)
                .forEach(a -> {
                        persister.updateOne(a, context);
                        applyChangesToAllDuplicates((Entity<?>) a.instanceAccessModel.domainObject(), context);
                    }
                ));

        var deletionOrderClasses = persistenceActionOrderProvider.deletionOrder(context.rootClass.getName());
        if (deletionOrderClasses == null || deletionOrderClasses.isEmpty()) {
            throw DLCPersistenceException.fail("The deletion order was not defined! Check the mirror! RootClass '%s'.", context.rootClass);
        }

        //Below: Integer.compare actions --> are sorted in descending order by their structural position
        //which means form leafs to root --> within hierarchical structures we need this order of applying deletes
        deletionOrderClasses.forEach(c ->
            context.getActionsPartitioned(c, PersistenceAction.ActionType.DELETE)
                .stream()
                .sorted((a1, a2) -> Integer.compare(
                    a2.instanceAccessModel
                        .structuralPosition
                        .accessPathFromRoot
                        .size(),
                    a1.instanceAccessModel
                        .structuralPosition
                        .accessPathFromRoot
                        .size()))
                .forEach(a -> persister.deleteOne(a, context))
        );
        //UPDATEs and INSERTs must be executed in insertion order ordered by record typ
        //but executing all UPDATES first and then all INSERTs causes problems with 1-1-forward referenced
        //entity relations
        insertionOrderClasses.forEach(c -> {
            context.getActionsPartitioned(c, PersistenceAction.ActionType.UPDATE)
                .forEach(a -> {
                    persister.updateOne(a, context);
                    applyChangesToAllDuplicates((Entity<?>) a.instanceAccessModel.domainObject(), context);
                });
            //Below: Integer.compare actions --> actions are sorted in ascending order by their structural position
            //which means form root to leafs --> within hierarchical structures we need this order of applying inserts
            context.getActionsPartitioned(c, PersistenceAction.ActionType.INSERT)
                .stream()
                .sorted(Comparator.comparingInt(a -> a.instanceAccessModel.structuralPosition.accessPathFromRoot.size()))
                .forEach(a -> {
                        persister.insertOne(a, context);
                        if (a.instanceAccessModel.isEntity()) {
                            applyChangesToAllDuplicates((Entity<?>) a.instanceAccessModel.domainObject(), context);
                        }
                    }
                );
        });

    }

    /**
     * This method delivers the current database state of the root by its root
     * id
     *
     * @param id of the aggregate root to be fetched.
     * @return Optional of the root entity instance, with the given id.
     */
    public abstract FetcherResult<A, BASE_RECORD_TYPE> findResultById(I id);

    public Optional<A> findById(I id){
        return findResultById(id).resultValue();
    }

    /**
     * Publish {@link PersistenceAction}. To be overridden by implementor, if needed
     */
    public void publish(PersistenceAction<?> action) {
        // to be overridden by implementor, if needed
    }

    /**
     * E.g. Adapt increased version values or result values set or updated by
     * database triggers into the entity, so that its reference represents the
     * current database state.
     */
    private void adaptChangesFromEntityWithAppliedChangesToDuplicateEntity(
        final Entity<?> entityTo,
        final Entity<?> adaptFrom
    ) {
        Objects.requireNonNull(entityTo);
        Objects.requireNonNull(adaptFrom);
        var em = Domain.entityMirrorFor(entityTo.getClass().getName());

        var accessorTo = DlcAccess.accessorFor(entityTo);
        var accessorFrom = DlcAccess.accessorFor(adaptFrom);

        em.getAllFields()
            .stream()
            .filter(f ->
                f.getType().getDomainType().equals(DomainType.VALUE_OBJECT) ||
                    f.getType().getDomainType().equals(DomainType.IDENTITY) ||
                    f.getType().getDomainType().equals(DomainType.ENUM) ||
                    f.getType().getDomainType().equals(DomainType.NON_DOMAIN)
            ).forEach(f -> {
            Object valueOriginal = accessorTo.peek(f.getName());
            Object valueResult = accessorFrom.peek(f.getName());

            if ((valueOriginal == null && valueResult != null) ||
                (valueOriginal != null && !valueOriginal.equals(valueResult))) {
                accessorTo.poke(f.getName(), valueResult);
            }
        });
    }

    private void applyChangesToAllDuplicates(Entity<?> from, PersistenceContext<BASE_RECORD_TYPE> pc) {
        List<Entity<?>> duplicates = pc.getEntityDuplicates().get(from);
        if (duplicates != null) {
            duplicates.forEach(
                d -> adaptChangesFromEntityWithAppliedChangesToDuplicateEntity(d, from)
            );
        }
    }
}


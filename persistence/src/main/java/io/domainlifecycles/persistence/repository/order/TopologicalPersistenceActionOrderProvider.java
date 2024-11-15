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

package io.domainlifecycles.persistence.repository.order;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.RecordMirror;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.order.graph.DirectedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The order of persistence actions performed of the database is depending on the foreign key constraints
 * of the underlying database relations. Those are represented by {@link DatabaseDependencyEdge}.
 * <p>
 * In this class we calculate the total
 * order on how the changes can be applied - a topological order algorithm.
 */
public class TopologicalPersistenceActionOrderProvider implements PersistenceActionOrderProvider {

    private final Map<String, List<String>> topologicallyOrderedAggregateRoots = new HashMap<>();

    private final DomainPersistenceProvider<?> domainPersistenceProvider;

    public TopologicalPersistenceActionOrderProvider(DomainPersistenceProvider<?> domainPersistenceProvider) {
        this.domainPersistenceProvider = domainPersistenceProvider;
        Domain.getInitializedDomain().allTypeMirrors()
            .values()
            .stream()
            .filter(t -> DomainType.AGGREGATE_ROOT.equals(t.getDomainType()) && !t.isAbstract())
            .forEach(
                ar -> calculateTopologicalOrderForAggregateRoot(ar.getTypeName())
            );
    }

    /**
     * {@inheritDoc}
     *
     * @param aggregateRootClassName the aggregate root full qualified class name
     * @return
     */
    @Override
    public List<String> insertionOrder(String aggregateRootClassName) {
        return topologicallyOrderedAggregateRoots.get(aggregateRootClassName);
    }

    /**
     * {@inheritDoc}
     *
     * @param aggregateRootClassName the aggregate root full qualified class name
     * @return
     */
    @Override
    public List<String> deletionOrder(String aggregateRootClassName) {
        var toBeReversed = new ArrayList<>(topologicallyOrderedAggregateRoots.get(aggregateRootClassName));
        Collections.reverse(toBeReversed);
        return toBeReversed;
    }


    private void calculateContainedEntities(String aggregateRootOrEntityType, Set<String> currentlyContained) {
        var entityMirror = Domain.entityMirrorFor(aggregateRootOrEntityType);

        Set<String> contained = entityMirror.getEntityReferences()
            .stream()
            .filter(r -> !r.isStatic())
            .map(r -> r.getType().getTypeName())
            .collect(Collectors.toSet());
        for (String e : contained) {
            if (!currentlyContained.contains(e)) {
                currentlyContained.add(e);
                calculateContainedEntities(e, currentlyContained);
            }
        }
    }

    private void calculateTopologicalOrderForAggregateRoot(String aggregateRootClassName) {
        Set<String> containedEntityTypes = new HashSet<>();
        containedEntityTypes.add(aggregateRootClassName);
        calculateContainedEntities(aggregateRootClassName, containedEntityTypes);

        Set<RecordMirror<?>> recordMirrors = new HashSet<>();

        var graph = new DirectedGraph<String>();

        containedEntityTypes
            .forEach(
                et -> {
                    EntityRecordMirror<?> erm = domainPersistenceProvider
                        .persistenceMirror.getEntityRecordMirror(et);
                    recordMirrors.add(erm);
                    recordMirrors.addAll(erm.valueObjectRecords());
                    graph.addVertex(erm.domainObjectTypeName());
                    erm.valueObjectRecords().forEach(vorm -> graph.addVertex(vorm.domainObjectTypeName()));
                }
            );
        var edges = getDatabaseDependencyEdges(recordMirrors);


        if (edges != null) {
            edges.forEach(e -> {
                if (!e.sourceClassName().equals(e.targetClassName())) {
                    //do not add self references simple hierarchies should be ok
                    graph.addEdge(new DirectedGraph.Edge<>(e.sourceClassName(), e.targetClassName()));
                }
            });
        }

        if (graph.checkCycle()) {
            throw DLCPersistenceException.fail("Cycle detected. Please have a look at your foreign key structure!");
        }

        var topological = graph.topologicalSort();
        topologicallyOrderedAggregateRoots.put(aggregateRootClassName, topological);
    }

    private Set<DatabaseDependencyEdge> getDatabaseDependencyEdges(Set<RecordMirror<?>> recordMirrors) {
        return recordMirrors.stream()
            .flatMap(rm -> rm.enforcedReferences().stream().map(
                r -> new DatabaseDependencyEdge(r, rm.domainObjectTypeName())))
            .collect(Collectors.toSet());
    }
}

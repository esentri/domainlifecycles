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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link FlowAnalyzer} on top of a {@link DomainCalls} graph joined with the event and command
 * information of the {@link DomainMirror}.
 * <p>
 * Four kinds of edges are followed:
 * <ul>
 *     <li>{@link StepKind#CALL} - a resolved method call, taken from {@link DomainCalls},</li>
 *     <li>{@link StepKind#IMPLEMENTATION} - the overrides a method dispatches to at runtime,
 *     taken from the mirror. {@link DomainCalls} reports a call on the type written at the call
 *     site, which for a repository or outbound service is the interface; without this edge the
 *     flow would end there,</li>
 *     <li>{@link StepKind#EVENT_PUBLISH} / {@link StepKind#EVENT_LISTEN} - a published domain
 *     event and the methods listening to it, taken from the mirror. No static call analysis can
 *     see this edge, yet it is where the domain flow continues,</li>
 *     <li>{@link StepKind#COMMAND_PROCESS} - the methods processing a domain command, used when a
 *     flow starts at a command.</li>
 * </ul>
 * The traversal is breadth first. Every node is expanded at most once per flow, while each
 * incoming edge is reported, so the result stays bounded by the number of edges even for cyclic
 * domains. Steps closing a cycle are marked via {@link Step#cyclic()}.
 *
 * @author Mario Herb
 */
public class DomainCallFlowAnalyzer implements FlowAnalyzer {

    private final DomainMirror domainMirror;

    private final DomainCalls domainCalls;

    private final FlowConfig config;

    private final Map<String, List<DomainMethod>> listenersByEventTypeName;

    private final Map<String, List<DomainMethod>> processorsByCommandTypeName;

    private final Map<String, List<DomainMethod>> publishersByEventTypeName;

    private final Map<String, List<DomainTypeMirror>> implementationsBySupertypeName;

    /**
     * Creates an analyzer with {@link FlowConfig#defaults()}.
     *
     * @param domainMirror the mirror of the analyzed domain, must not be {@code null}
     * @param domainCalls  the previously analyzed domain calls, must not be {@code null}
     */
    public DomainCallFlowAnalyzer(DomainMirror domainMirror, DomainCalls domainCalls) {
        this(domainMirror, domainCalls, FlowConfig.defaults());
    }

    /**
     * @param domainMirror the mirror of the analyzed domain, must not be {@code null}
     * @param domainCalls  the previously analyzed domain calls, must not be {@code null}
     * @param config       the traversal options, must not be {@code null}
     */
    public DomainCallFlowAnalyzer(DomainMirror domainMirror, DomainCalls domainCalls,
                                  FlowConfig config) {
        this.domainMirror = Objects.requireNonNull(domainMirror, "A DomainMirror must be given!");
        this.domainCalls = Objects.requireNonNull(domainCalls, "DomainCalls must be given!");
        this.config = Objects.requireNonNull(config, "A FlowConfig must be given!");

        Map<String, List<DomainMethod>> listeners = new LinkedHashMap<>();
        Map<String, List<DomainMethod>> processors = new LinkedHashMap<>();
        Map<String, List<DomainMethod>> publishers = new LinkedHashMap<>();
        indexEventsAndCommands(listeners, processors, publishers);
        this.listenersByEventTypeName = Collections.unmodifiableMap(listeners);
        this.processorsByCommandTypeName = Collections.unmodifiableMap(processors);
        this.publishersByEventTypeName = Collections.unmodifiableMap(publishers);
        this.implementationsBySupertypeName =
            Collections.unmodifiableMap(indexImplementations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainMethod start) {
        Objects.requireNonNull(start, "A starting DomainMethod must be given!");
        return traverse(Step.start(start), this::successorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainEventMirror event) {
        Objects.requireNonNull(event, "A starting DomainEventMirror must be given!");
        return traverse(Step.start(event), this::successorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainCommandMirror command) {
        Objects.requireNonNull(command, "A starting DomainCommandMirror must be given!");
        return traverse(Step.start(command), this::successorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Flow> flowFrom(String typeName, String methodName) {
        Optional<DomainTypeMirror> typeMirror = domainMirror.getDomainTypeMirror(typeName);
        return typeMirror.flatMap(mirror -> mirror.getMethods().stream()
            .filter(method -> method.getName().equals(methodName))
            .findFirst()
            .map(method -> new DomainMethod(mirror.getTypeName(), method)))
            .map(this::flowFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowTo(DomainMethod target) {
        Objects.requireNonNull(target, "A target DomainMethod must be given!");
        return traverse(Step.start(target), this::predecessorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowTo(DomainEventMirror event) {
        Objects.requireNonNull(event, "A target DomainEventMirror must be given!");
        return traverse(Step.start(event), this::predecessorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowTo(DomainTypeMirror target) {
        Objects.requireNonNull(target, "A target DomainTypeMirror must be given!");
        return traverse(Step.start(target), this::predecessorsOf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Flow> flowTo(String typeName, String methodName) {
        Optional<DomainTypeMirror> typeMirror = domainMirror.getDomainTypeMirror(typeName);
        return typeMirror.flatMap(mirror -> mirror.getMethods().stream()
            .filter(method -> method.getName().equals(methodName))
            .findFirst()
            .map(method -> new DomainMethod(mirror.getTypeName(), method)))
            .map(this::flowTo);
    }

    // ---------------------------------------------------------------------
    // Traversal
    // ---------------------------------------------------------------------

    private Flow traverse(Step start, Function<Step, List<Step>> edgesOf) {
        List<Step> steps = new ArrayList<>();
        Set<String> expanded = new HashSet<>();
        Deque<Step> queue = new ArrayDeque<>();
        boolean truncated = false;

        steps.add(start);
        expanded.add(start.nodeKey());
        queue.add(start);

        while (!queue.isEmpty()) {
            Step current = queue.poll();
            List<Step> successors = edgesOf.apply(current);
            if (successors.isEmpty()) {
                continue;
            }
            if (current.depth() >= config.maxDepth()) {
                // there is more to follow, but the depth limit forbids it
                truncated = true;
                continue;
            }
            for (Step next : successors) {
                // every incoming edge is reported ...
                steps.add(next);
                if (next.cyclic()) {
                    continue;
                }
                // ... but each node is expanded only once, which bounds the traversal
                if (expanded.add(next.nodeKey())) {
                    queue.add(next);
                }
            }
        }

        return new Flow(start, steps, truncated);
    }

    private List<Step> successorsOf(Step current) {
        // no pattern matching switch: the module targets Java 17, where it is still a preview
        if (current instanceof Step.MethodStep methodStep) {
            return successorsOfMethod(methodStep);
        }
        if (current instanceof Step.EventStep eventStep) {
            return successorsOfEvent(eventStep);
        }
        if (current instanceof Step.CommandStep commandStep) {
            return successorsOfCommand(commandStep);
        }
        return List.of();
    }

    private List<Step> successorsOfMethod(Step.MethodStep current) {
        List<Step> successors = new ArrayList<>();

        for (DomainMethod called : domainCalls.callsFor(current.method()).methods()) {
            if (!config.methodFilter().test(called)) {
                continue;
            }
            successors.add(Step.called(current, called,
                isOnPath(current, Step.nodeKeyOf(called))));
        }

        if (config.followImplementations()) {
            for (DomainMethod implementation : implementationsOf(current.method())) {
                if (!config.methodFilter().test(implementation)) {
                    continue;
                }
                successors.add(Step.implementing(current, implementation,
                    isOnPath(current, Step.nodeKeyOf(implementation))));
            }
        }

        if (config.followEvents()) {
            for (DomainEventMirror published : current.method().mirror().getPublishedEvents()) {
                successors.add(Step.published(current, published,
                    isOnPath(current, Step.nodeKeyOf(published))));
            }
        }

        domainMirror.getDomainTypeMirror(current.method().typeName())
            .filter(QueryHandlerMirror.class::isInstance)
            .map(QueryHandlerMirror.class::cast)
            .flatMap(QueryHandlerMirror::getProvidedReadModel)
            .ifPresent(readModel -> successors.add(Step.providingReadModel(current, readModel,
                isOnPath(current, Step.nodeKeyOf(readModel)))));

        domainMirror.getDomainTypeMirror(current.method().typeName())
            .filter(RepositoryMirror.class::isInstance)
            .map(RepositoryMirror.class::cast)
            .flatMap(RepositoryMirror::getManagedAggregate)
            .ifPresent(aggregate -> successors.add(Step.managingAggregate(current, aggregate,
                isOnPath(current, Step.nodeKeyOf(aggregate)))));

        return successors;
    }

    private List<Step> successorsOfEvent(Step.EventStep current) {
        if (!config.followEvents()) {
            return List.of();
        }
        List<Step> successors = new ArrayList<>();
        for (DomainMethod listener : listenersOf(current.event())) {
            if (!config.methodFilter().test(listener)) {
                continue;
            }
            successors.add(Step.listening(current, listener,
                isOnPath(current, Step.nodeKeyOf(listener))));
        }
        return successors;
    }

    private List<Step> successorsOfCommand(Step.CommandStep current) {
        List<Step> successors = new ArrayList<>();
        for (DomainMethod processor : processorsOf(current.command())) {
            if (!config.methodFilter().test(processor)) {
                continue;
            }
            successors.add(Step.processing(current, processor,
                isOnPath(current, Step.nodeKeyOf(processor))));
        }
        return successors;
    }

    // ---------------------------------------------------------------------
    // Backward traversal (flowTo)
    // ---------------------------------------------------------------------

    private List<Step> predecessorsOf(Step current) {
        // no pattern matching switch: the module targets Java 17, where it is still a preview
        if (current instanceof Step.MethodStep methodStep) {
            return predecessorsOfMethod(methodStep, methodStep.method());
        }
        if (current instanceof Step.EventStep eventStep) {
            return predecessorsOfEvent(eventStep);
        }
        if (current instanceof Step.TypeStep typeStep) {
            return predecessorsOfType(typeStep);
        }
        // CommandStep: nothing in the analyzed data models where a command originates
        return List.of();
    }

    /**
     * The predecessors of the given method, attached to the given step. Split out from
     * {@link #predecessorsOf(Step)} so {@link #predecessorsOfType(Step.TypeStep)} can apply it to
     * every method of a type without an intermediate per-method step of its own.
     */
    private List<Step> predecessorsOfMethod(Step from, DomainMethod method) {
        List<Step> predecessors = new ArrayList<>();

        for (DomainMethod caller : domainCalls.callersOf(method)) {
            if (!config.methodFilter().test(caller)) {
                continue;
            }
            predecessors.add(Step.calledBy(from, caller, isOnPath(from, Step.nodeKeyOf(caller))));
        }

        if (config.followImplementations()) {
            for (DomainMethod overridden : overriddenSupertypeMethodsOf(method)) {
                if (!config.methodFilter().test(overridden)) {
                    continue;
                }
                predecessors.add(Step.implementedBy(from, overridden,
                    isOnPath(from, Step.nodeKeyOf(overridden))));
            }
        }

        if (config.followEvents()) {
            method.mirror().getListenedEvent().ifPresent(event ->
                predecessors.add(Step.listenedTo(from, event,
                    isOnPath(from, Step.nodeKeyOf(event)))));
        }

        for (DomainCommandMirror command : method.mirror().getProcessedCommands()) {
            predecessors.add(Step.processedCommand(from, command,
                isOnPath(from, Step.nodeKeyOf(command))));
        }

        return predecessors;
    }

    private List<Step> predecessorsOfEvent(Step.EventStep current) {
        if (!config.followEvents()) {
            return List.of();
        }
        List<Step> predecessors = new ArrayList<>();
        for (DomainMethod publisher : publishersOf(current.event())) {
            if (!config.methodFilter().test(publisher)) {
                continue;
            }
            predecessors.add(Step.publishedBy(current, publisher,
                isOnPath(current, Step.nodeKeyOf(publisher))));
        }
        return predecessors;
    }

    /**
     * The predecessors of a plain domain type: the callers of any of its own methods, unioned with
     * - for an Aggregate or ReadModel - the Repository or QueryHandler exposing it. The latter is
     * itself a {@link Step.TypeStep}, so the BFS expands it the same way on its next turn, which is
     * what lets "who calls the repository" chain in automatically.
     */
    private List<Step> predecessorsOfType(Step.TypeStep current) {
        List<Step> predecessors = new ArrayList<>();
        DomainTypeMirror type = current.type();

        for (MethodMirror method : type.getMethods()) {
            predecessors.addAll(
                predecessorsOfMethod(current, new DomainMethod(type.getTypeName(), method)));
        }

        if (type instanceof AggregateRootMirror) {
            for (DomainTypeMirror repository : repositoriesManaging(type.getTypeName())) {
                predecessors.add(Step.managedBy(current, repository,
                    isOnPath(current, Step.nodeKeyOf(repository))));
            }
        }

        if (type instanceof ReadModelMirror) {
            for (DomainTypeMirror queryHandler : queryHandlersProviding(type.getTypeName())) {
                predecessors.add(Step.providedBy(current, queryHandler,
                    isOnPath(current, Step.nodeKeyOf(queryHandler))));
            }
        }

        return predecessors;
    }

    /**
     * Whether the given node already occurs among the predecessors of the given step, which means
     * following it would close a cycle.
     */
    private boolean isOnPath(Step from, String nodeKey) {
        Optional<Step> current = Optional.of(from);
        while (current.isPresent()) {
            if (current.get().nodeKey().equals(nodeKey)) {
                return true;
            }
            current = current.get().from();
        }
        return false;
    }

    // ---------------------------------------------------------------------
    // Event / command index
    // ---------------------------------------------------------------------

    /**
     * Indexes, for every domain event, the methods listening to it and the methods publishing it,
     * and for every domain command the methods processing it. All are attributed to the concrete
     * owner type, matching the way {@link DomainCalls} attributes calls. The publishers index is
     * only needed for the backward traversal ({@link #predecessorsOfEvent(Step.EventStep)}): going
     * forward, a method's own published events are read directly off its mirror, with no need for
     * a global index.
     */
    private void indexEventsAndCommands(Map<String, List<DomainMethod>> listeners,
                                        Map<String, List<DomainMethod>> processors,
                                        Map<String, List<DomainMethod>> publishers) {

        for (DomainTypeMirror typeMirror : domainMirror.getAllDomainTypeMirrors()) {
            for (MethodMirror method : typeMirror.getMethods()) {
                var methodCall = new DomainMethod(typeMirror.getTypeName(), method);

                method.getListenedEvent().ifPresent(event ->
                    listeners.computeIfAbsent(event.getTypeName(), k -> new ArrayList<>())
                        .add(methodCall));

                for (DomainCommandMirror command : method.getProcessedCommands()) {
                    processors.computeIfAbsent(command.getTypeName(), k -> new ArrayList<>())
                        .add(methodCall);
                }

                for (DomainEventMirror published : method.getPublishedEvents()) {
                    publishers.computeIfAbsent(published.getTypeName(), k -> new ArrayList<>())
                        .add(methodCall);
                }
            }
        }
    }

    private List<DomainMethod> listenersOf(DomainEventMirror event) {
        return listenersByEventTypeName.getOrDefault(event.getTypeName(), List.of());
    }

    private List<DomainMethod> processorsOf(DomainCommandMirror command) {
        return processorsByCommandTypeName.getOrDefault(command.getTypeName(), List.of());
    }

    private List<DomainMethod> publishersOf(DomainEventMirror event) {
        return publishersByEventTypeName.getOrDefault(event.getTypeName(), List.of());
    }

    // ---------------------------------------------------------------------
    // Repository / QueryHandler lookup (backward MANAGES_AGGREGATE / PROVIDES_READ_MODEL)
    // ---------------------------------------------------------------------

    private List<DomainTypeMirror> repositoriesManaging(String aggregateTypeName) {
        List<DomainTypeMirror> repositories = new ArrayList<>();
        for (RepositoryMirror repository : domainMirror.getAllRepositoryMirrors()) {
            repository.getManagedAggregate()
                .filter(aggregate -> aggregate.getTypeName().equals(aggregateTypeName))
                .ifPresent(aggregate -> repositories.add(repository));
        }
        return repositories;
    }

    private List<DomainTypeMirror> queryHandlersProviding(String readModelTypeName) {
        List<DomainTypeMirror> queryHandlers = new ArrayList<>();
        for (QueryHandlerMirror queryHandler : domainMirror.getAllQueryHandlerMirrors()) {
            queryHandler.getProvidedReadModel()
                .filter(readModel -> readModel.getTypeName().equals(readModelTypeName))
                .ifPresent(readModel -> queryHandlers.add(queryHandler));
        }
        return queryHandlers;
    }

    // ---------------------------------------------------------------------
    // Implementation index
    // ---------------------------------------------------------------------

    /**
     * Indexes, for every mirrored supertype, the instantiable mirrored types realizing it. Only
     * those are worth recording: an abstract type never runs, so the flow can only continue on a
     * concrete one.
     */
    private Map<String, List<DomainTypeMirror>> indexImplementations() {
        Map<String, List<DomainTypeMirror>> implementations = new LinkedHashMap<>();

        for (DomainTypeMirror typeMirror : domainMirror.getAllDomainTypeMirrors()) {
            if (typeMirror.isAbstract()) {
                continue;
            }
            Stream.concat(typeMirror.getInheritanceHierarchyTypeNames().stream(),
                    typeMirror.getAllInterfaceTypeNames().stream())
                .distinct()
                .forEach(supertypeName -> implementations
                    .computeIfAbsent(supertypeName, k -> new ArrayList<>())
                    .add(typeMirror));
        }
        return implementations;
    }

    /**
     * The methods the given one dispatches to at runtime: the same method on every instantiable
     * subtype of its owner. Empty if nothing in the domain extends or implements the owner.
     * <p>
     * Deliberately not restricted to abstract owners. A concrete class can be overridden just as
     * an abstract one can, and then a call written against it has more than one possible target
     * as well - the difference is only that the owner itself is a possible target too, which it
     * already is by being the step the edges start from. The index holds instantiable subtypes
     * only and never the owner itself, so this cannot produce a self edge.
     */
    private List<DomainMethod> implementationsOf(DomainMethod method) {
        List<DomainMethod> implementations = new ArrayList<>();
        for (DomainTypeMirror implementation
            : implementationsBySupertypeName.getOrDefault(method.typeName(), List.of())) {

            implementation.getMethods().stream()
                .filter(candidate -> hasSameSignature(candidate, method.mirror()))
                .findFirst()
                .ifPresent(candidate -> implementations.add(
                    new DomainMethod(implementation.getTypeName(), candidate)));
        }
        return implementations;
    }

    /**
     * The abstract or interface methods the given one overrides: for every ancestor type
     * (superclass or interface) of its owner, the method with the same signature, if the ancestor
     * declares one. This is the inverse of {@link #implementationsOf(DomainMethod)}, walked from
     * the owner's own hierarchy rather than via a precomputed index - the owner is already known
     * from the single method being reversed, so no global index is needed here.
     */
    private List<DomainMethod> overriddenSupertypeMethodsOf(DomainMethod method) {
        List<DomainMethod> overridden = new ArrayList<>();
        domainMirror.getDomainTypeMirror(method.typeName()).ifPresent(owner ->
            Stream.concat(owner.getInheritanceHierarchyTypeNames().stream(),
                    owner.getAllInterfaceTypeNames().stream())
                .distinct()
                .forEach(supertypeName -> domainMirror.getDomainTypeMirror(supertypeName)
                    .ifPresent(supertype -> supertype.getMethods().stream()
                        .filter(candidate -> hasSameSignature(candidate, method.mirror()))
                        .findFirst()
                        .ifPresent(candidate -> overridden.add(
                            new DomainMethod(supertype.getTypeName(), candidate))))));
        return overridden;
    }

    /**
     * Whether two mirrored methods are the same method seen on different types, i.e. whether one
     * overrides the other. Compared by name and parameter types, which is what identifies a
     * method within its owner.
     */
    private static boolean hasSameSignature(MethodMirror candidate, MethodMirror declared) {
        if (!candidate.getName().equals(declared.getName())
            || candidate.getParameters().size() != declared.getParameters().size()) {
            return false;
        }
        for (int i = 0; i < candidate.getParameters().size(); i++) {
            if (!candidate.getParameters().get(i).getType().getTypeName()
                .equals(declared.getParameters().get(i).getType().getTypeName())) {
                return false;
            }
        }
        return true;
    }
}

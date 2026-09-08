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
 *  Copyright 2019-2025 the original author or authors.
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

import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

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

/**
 * {@link FlowAnalyzer} on top of a {@link DomainCalls} graph joined with the event and command
 * information of the {@link DomainMirror}.
 * <p>
 * Three kinds of edges are followed:
 * <ul>
 *     <li>{@link StepKind#CALL} - a resolved method call, taken from {@link DomainCalls},</li>
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
        indexEventsAndCommands(listeners, processors);
        this.listenersByEventTypeName = Collections.unmodifiableMap(listeners);
        this.processorsByCommandTypeName = Collections.unmodifiableMap(processors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainMethod start) {
        Objects.requireNonNull(start, "A starting DomainMethod must be given!");
        return traverse(Step.start(start));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainEventMirror event) {
        Objects.requireNonNull(event, "A starting DomainEventMirror must be given!");
        return traverse(Step.start(event));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flow flowFrom(DomainCommandMirror command) {
        Objects.requireNonNull(command, "A starting DomainCommandMirror must be given!");
        return traverse(Step.start(command));
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

    // ---------------------------------------------------------------------
    // Traversal
    // ---------------------------------------------------------------------

    private Flow traverse(Step start) {
        List<Step> steps = new ArrayList<>();
        Set<String> expanded = new HashSet<>();
        Deque<Step> queue = new ArrayDeque<>();
        boolean truncated = false;

        steps.add(start);
        expanded.add(start.nodeKey());
        queue.add(start);

        while (!queue.isEmpty()) {
            Step current = queue.poll();
            List<Step> successors = successorsOf(current);
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

        if (config.followEvents()) {
            for (DomainEventMirror published : current.method().mirror().getPublishedEvents()) {
                successors.add(Step.published(current, published,
                    isOnPath(current, Step.nodeKeyOf(published))));
            }
        }

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
     * Indexes, for every domain event, the methods listening to it, and for every domain command
     * the methods processing it. Both are attributed to the concrete owner type, matching the way
     * {@link DomainCalls} attributes calls.
     */
    private void indexEventsAndCommands(Map<String, List<DomainMethod>> listeners,
                                        Map<String, List<DomainMethod>> processors) {

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
            }
        }
    }

    private List<DomainMethod> listenersOf(DomainEventMirror event) {
        return listenersByEventTypeName.getOrDefault(event.getTypeName(), List.of());
    }

    private List<DomainMethod> processorsOf(DomainCommandMirror command) {
        return processorsByCommandTypeName.getOrDefault(command.getTypeName(), List.of());
    }
}

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

package io.domainlifecycles.diagram.domain.mapper;

import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.staticanalysis.DomainCallFlowAnalyzer;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import io.domainlifecycles.staticanalysis.FlowAnalyzer;
import io.domainlifecycles.staticanalysis.Flow;
import io.domainlifecycles.staticanalysis.FlowConfig;
import io.domainlifecycles.staticanalysis.Step;
import io.domainlifecycles.staticanalysis.StepKind;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Restricts a diagram to the domain types taking part in one or more flows, forward
 * ({@code includeFlowsFrom} - "what does this lead to") and/or backward ({@code includeFlowsTo} -
 * "what leads into this"). If both are configured, their reached types are united: a type survives
 * if reached by either direction.
 * <p>
 * The other trim settings walk the structural relations the mirror knows. A flow instead follows
 * the method calls of the analyzed domain, joined with the events and commands of the mirror -
 * information that is not in the mirror alone, which is why a {@link DomainCalls} result has to be
 * handed to the diagram generator for this filter to work.
 * <p>
 * This is a pure restriction: it can only remove types from a diagram, never add any. It is
 * therefore applied last, after all other settings had their say. The edges a diagram draws come
 * entirely from the mirror's structural data ({@link DomainRelationshipMapper}), regardless of
 * which direction caused a type to be included, so this filter never changes how a relationship is
 * drawn - only whether the types it connects are shown at all.
 *
 * @author Mario Herb
 */
public class DomainFlowFilter {

    /**
     * Separates the type name from the method name in a flow starting point.
     */
    private static final String METHOD_SEPARATOR = "#";

    /**
     * A filter that restricts nothing, used when no flow is configured.
     */
    static final DomainFlowFilter INACTIVE = new DomainFlowFilter();

    private final Set<String> reachedTypeNames;

    /** the methods called in the flows, see {@link #methodKey(String, MethodMirror)} */
    private final Set<String> reachedMethodKeys;

    /** see {@link #calls()} */
    private final Map<String, Map<String, Set<String>>> calls;

    private final boolean active;

    private DomainFlowFilter() {
        this.reachedTypeNames = Collections.emptySet();
        this.reachedMethodKeys = Collections.emptySet();
        this.calls = Collections.emptyMap();
        this.active = false;
    }

    /**
     * Computes the domain types reached by the flows starting at, and/or leading into, the given
     * points.
     *
     * @param domainMirror     the mirror of the diagrammed domain, must not be {@code null}
     * @param domainCalls      the result of the static analysis of that domain, must not be
     *                         {@code null}
     * @param flowConfig       how the flows are traversed, must not be {@code null}
     * @param includeFlowsFrom the forward starting points, must not be {@code null}; may be empty
     *                         if {@code includeFlowsTo} is not
     * @param includeFlowsTo   the backward target points, must not be {@code null}; may be empty
     *                         if {@code includeFlowsFrom} is not
     */
    public DomainFlowFilter(DomainMirror domainMirror,
                            DomainCalls domainCalls,
                            FlowConfig flowConfig,
                            List<String> includeFlowsFrom,
                            List<String> includeFlowsTo) {

        Objects.requireNonNull(domainMirror, "A DomainMirror must be given!");
        Objects.requireNonNull(domainCalls, "DomainCalls must be given!");
        Objects.requireNonNull(flowConfig, "A FlowConfig must be given!");
        Objects.requireNonNull(includeFlowsFrom, "The flow starting points must be given!");
        Objects.requireNonNull(includeFlowsTo, "The flow target points must be given!");

        FlowAnalyzer flowAnalyzer =
            new DomainCallFlowAnalyzer(domainMirror, domainCalls, flowConfig);

        this.domainCalls = domainCalls;
        this.collectedMethodKeys = new HashSet<>();
        this.collectedCalls = new LinkedHashMap<>();
        Set<String> reached = new LinkedHashSet<>();
        for (String startingPoint : includeFlowsFrom) {
            reached.addAll(reachedFrom(domainMirror, flowAnalyzer, startingPoint));
        }
        for (String targetPoint : includeFlowsTo) {
            reached.addAll(reachedTo(domainMirror, flowAnalyzer, targetPoint));
        }
        // an aggregate is shown as a whole: a flow reaching one of its entities reaches its root too
        reached.addAll(reached.stream()
            .flatMap(typeName -> DomainMapperUtils.aggregateRootsContaining(typeName, domainMirror).stream())
            .toList());
        this.reachedTypeNames = Collections.unmodifiableSet(reached);
        this.reachedMethodKeys = Collections.unmodifiableSet(collectedMethodKeys);
        this.calls = Collections.unmodifiableMap(collectedCalls);
        this.active = true;
        this.domainCalls = null;
        this.collectedMethodKeys = null;
        this.collectedCalls = null;
    }

    /** only needed while the flows are resolved */
    private DomainCalls domainCalls;
    private Set<String> collectedMethodKeys;
    private Map<String, Map<String, Set<String>>> collectedCalls;

    /**
     * Collects the methods called in a flow, returning the types it reaches. A method step is a called method. A
     * backward flow into a whole type records its callers only, so the methods of that type they call are added too.
     */
    private Set<String> collect(boolean backward, Flow flow) {
        for (Step step : flow.steps()) {
            if (step instanceof Step.MethodStep methodStep) {
                collectedMethodKeys.add(methodKey(methodStep.method().typeName(), methodStep.method().mirror()));
                if (step.kind() != StepKind.CALL) {
                    continue;
                }
                Step from = step.from().orElse(null);
                if (!backward && from instanceof Step.MethodStep caller) {
                    collectCall(caller.method().typeName(), methodStep.method());
                } else if (backward && from instanceof Step.MethodStep called) {
                    collectCall(methodStep.method().typeName(), called.method());
                } else if (backward && from instanceof Step.TypeStep typeStep) {
                    domainCalls.callsFor(methodStep.method()).methods().stream()
                        .filter(called -> called.typeName().equals(typeStep.typeName()))
                        .forEach(called -> {
                            collectedMethodKeys.add(methodKey(called.typeName(), called.mirror()));
                            collectCall(methodStep.method().typeName(), called);
                        });
                }
            }
        }
        return flow.reachedTypeNames();
    }

    private void collectCall(String callerTypeName, DomainMethod called) {
        if (!callerTypeName.equals(called.typeName())) {
            collectedCalls
                .computeIfAbsent(callerTypeName, key -> new LinkedHashMap<>())
                .computeIfAbsent(called.typeName(), key -> new LinkedHashSet<>())
                .add(called.mirror().getName());
        }
    }

    /**
     * The calls between the types of the flows: by the calling type name, the called type names with the names of
     * the called methods. Calls within one type are left out.
     *
     * @return the calls of the flows, empty if no flow is configured
     */
    public Map<String, Map<String, Set<String>>> calls() {
        return calls;
    }

    /**
     * Whether a domain type takes part in one of the flows - as opposed to being shown for another reason, e.g. as
     * part of a shown aggregate or read model.
     *
     * @param domainTypeMirror the type to check, must not be {@code null}
     * @return {@code true} if a flow reaches the type; {@code false} if not, or if no flow is configured
     */
    public boolean isReachedByFlow(DomainTypeMirror domainTypeMirror) {
        return active && reachedTypeNames.contains(domainTypeMirror.getTypeName());
    }

    /**
     * Whether a method of a domain type taking part in a flow is called in it: on the type itself, or on one of its
     * super types or interfaces - a call through an interface is recorded on the interface.
     *
     * @param domainTypeMirror the type the method is shown in, must not be {@code null}
     * @param methodMirror     the method, must not be {@code null}
     * @return {@code true} if the method is called in one of the flows; {@code false} if not, or if no flow is
     *     configured
     */
    public boolean isCalledInFlow(DomainTypeMirror domainTypeMirror, MethodMirror methodMirror) {
        if (!active) {
            return false;
        }
        if (reachedMethodKeys.contains(methodKey(domainTypeMirror.getTypeName(), methodMirror))) {
            return true;
        }
        var superTypeNames = new LinkedHashSet<String>(domainTypeMirror.getAllInterfaceTypeNames());
        superTypeNames.addAll(domainTypeMirror.getInheritanceHierarchyTypeNames());
        return superTypeNames.stream()
            .anyMatch(typeName -> reachedMethodKeys.contains(methodKey(typeName, methodMirror)));
    }

    private static String methodKey(String typeName, MethodMirror methodMirror) {
        return typeName + METHOD_SEPARATOR + methodMirror.getName() + methodMirror.getParameters().stream()
            .map(parameter -> parameter.getType().getTypeName())
            .collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * Whether this filter restricts anything at all.
     *
     * @return {@code false} if no flow was configured, in which case
     *     {@link #contains(DomainTypeMirror)} accepts everything
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Whether the given domain type takes part in one of the configured flows.
     *
     * @param domainTypeMirror the type to check, must not be {@code null}
     * @return {@code true} if the type is reached by a flow, or if no flow is configured
     */
    public boolean contains(DomainTypeMirror domainTypeMirror) {
        Objects.requireNonNull(domainTypeMirror, "A DomainTypeMirror must be given!");
        return !active || reachedTypeNames.contains(domainTypeMirror.getTypeName());
    }

    /**
     * The full qualified names of all domain types reached by the configured flows.
     *
     * @return the reached type names, empty if no flow is configured
     */
    public Set<String> reachedTypeNames() {
        return reachedTypeNames;
    }

    /**
     * Resolves one starting point against the mirror and returns the types its flow reaches.
     * <p>
     * A domain command or domain event starts the flow it triggers. Any other domain type starts
     * the flows of all of its methods, which answers "what can this service set in motion".
     * Appending {@code #methodName} narrows that down to the overloads of one method.
     */
    private Set<String> reachedFrom(DomainMirror domainMirror,
                                    FlowAnalyzer flowAnalyzer,
                                    String startingPoint) {

        int separatorIndex = startingPoint.indexOf(METHOD_SEPARATOR);
        String typeName = separatorIndex < 0
            ? startingPoint
            : startingPoint.substring(0, separatorIndex);
        String methodName = separatorIndex < 0
            ? null
            : startingPoint.substring(separatorIndex + METHOD_SEPARATOR.length());

        DomainTypeMirror typeMirror = domainMirror.getDomainTypeMirror(typeName)
            .orElseThrow(() -> new IllegalArgumentException(
                "The flow starting point '" + startingPoint + "' names the type '" + typeName
                    + "', which is unknown to the mirror."));

        if (methodName == null && typeMirror instanceof DomainCommandMirror commandMirror) {
            return collect(false, flowAnalyzer.flowFrom(commandMirror));
        }
        if (methodName == null && typeMirror instanceof DomainEventMirror eventMirror) {
            return collect(false, flowAnalyzer.flowFrom(eventMirror));
        }

        // Resolving the methods here instead of using FlowAnalyzer.flowFrom(String, String):
        // that one picks the first overload, which would be an arbitrary choice for a diagram.
        List<MethodMirror> startingMethods = typeMirror.getMethods().stream()
            .filter(method -> methodName == null || method.getName().equals(methodName))
            .toList();
        if (startingMethods.isEmpty()) {
            throw new IllegalArgumentException(
                "The flow starting point '" + startingPoint + "' names no method of the type '"
                    + typeName + "'.");
        }

        Set<String> reached = new LinkedHashSet<>();
        for (MethodMirror startingMethod : startingMethods) {
            reached.addAll(collect(false, flowAnalyzer
                .flowFrom(new DomainMethod(typeMirror.getTypeName(), startingMethod))));
        }
        return reached;
    }

    /**
     * Resolves one target point against the mirror and returns the types its backward flow
     * reaches - the entry channels through which the target is reached.
     * <p>
     * A domain event resolves to the methods publishing it. Any other domain type resolves to
     * everything leading into any of its methods, plus - for an Aggregate or ReadModel - the
     * Repository or QueryHandler exposing it. Appending {@code #methodName} narrows that down to
     * the overloads of one method. A domain command cannot be a target: nothing in the analyzed
     * data models where a command originates.
     */
    private Set<String> reachedTo(DomainMirror domainMirror,
                                  FlowAnalyzer flowAnalyzer,
                                  String targetPoint) {

        int separatorIndex = targetPoint.indexOf(METHOD_SEPARATOR);
        String typeName = separatorIndex < 0
            ? targetPoint
            : targetPoint.substring(0, separatorIndex);
        String methodName = separatorIndex < 0
            ? null
            : targetPoint.substring(separatorIndex + METHOD_SEPARATOR.length());

        DomainTypeMirror typeMirror = domainMirror.getDomainTypeMirror(typeName)
            .orElseThrow(() -> new IllegalArgumentException(
                "The flow target point '" + targetPoint + "' names the type '" + typeName
                    + "', which is unknown to the mirror."));

        if (methodName == null && typeMirror instanceof DomainCommandMirror) {
            throw new IllegalArgumentException(
                "The flow target point '" + targetPoint + "' names the domain command '"
                    + typeName + "'. Nothing in the analyzed data models where a command"
                    + " originates, so a command cannot be a backward flow target - it can only"
                    + " appear as a reached node on the way to one.");
        }
        if (methodName == null && typeMirror instanceof DomainEventMirror eventMirror) {
            return collect(true, flowAnalyzer.flowTo(eventMirror));
        }
        if (methodName == null) {
            // any other domain type resolves to everything leading into any of its methods, plus
            // its structural MANAGES_AGGREGATE / PROVIDES_READ_MODEL counterpart, if applicable
            return collect(true, flowAnalyzer.flowTo(typeMirror));
        }

        List<MethodMirror> targetMethods = typeMirror.getMethods().stream()
            .filter(method -> method.getName().equals(methodName))
            .toList();
        if (targetMethods.isEmpty()) {
            throw new IllegalArgumentException(
                "The flow target point '" + targetPoint + "' names no method of the type '"
                    + typeName + "'.");
        }

        Set<String> reached = new LinkedHashSet<>();
        for (MethodMirror targetMethod : targetMethods) {
            reached.addAll(collect(true, flowAnalyzer
                .flowTo(new DomainMethod(typeMirror.getTypeName(), targetMethod))));
        }
        return reached;
    }

    /**
     * Creates the filter for a diagram, or {@link #INACTIVE} if no flow is configured.
     *
     * @param domainMirror     the mirror of the diagrammed domain
     * @param domainCalls      the result of the static analysis, may be {@code null} as long as no
     *                         flow is configured
     * @param flowConfig       how the flows are traversed
     * @param includeFlowsFrom the configured forward starting points, possibly empty
     * @param includeFlowsTo   the configured backward target points, possibly empty
     * @return the filter to apply
     * @throws IllegalArgumentException if flows are configured without a {@link DomainCalls}
     */
    static DomainFlowFilter of(DomainMirror domainMirror,
                               DomainCalls domainCalls,
                               FlowConfig flowConfig,
                               List<String> includeFlowsFrom,
                               List<String> includeFlowsTo) {

        boolean hasFrom = includeFlowsFrom != null && !includeFlowsFrom.isEmpty();
        boolean hasTo = includeFlowsTo != null && !includeFlowsTo.isEmpty();
        if (!hasFrom && !hasTo) {
            return INACTIVE;
        }
        if (domainCalls == null) {
            throw new IllegalArgumentException(
                "Restricting a diagram to a flow (includeFlowsFrom " + includeFlowsFrom + ", includeFlowsTo "
                    + includeFlowsTo + ") needs the result of a static analysis. Hand a"
                    + " DomainCalls instance to the DomainDiagramGenerator constructor, or drop"
                    + " the includeFlowsFrom/includeFlowsTo setting.");
        }
        return new DomainFlowFilter(domainMirror, domainCalls, flowConfig,
            hasFrom ? includeFlowsFrom : List.of(),
            hasTo ? includeFlowsTo : List.of());
    }
}

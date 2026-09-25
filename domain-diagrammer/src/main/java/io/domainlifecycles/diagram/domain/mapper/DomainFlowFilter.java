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
import io.domainlifecycles.staticanalysis.FlowConfig;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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

    private final boolean active;

    private DomainFlowFilter() {
        this.reachedTypeNames = Collections.emptySet();
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

        Set<String> reached = new LinkedHashSet<>();
        for (String startingPoint : includeFlowsFrom) {
            reached.addAll(reachedFrom(domainMirror, flowAnalyzer, startingPoint));
        }
        for (String targetPoint : includeFlowsTo) {
            reached.addAll(reachedTo(domainMirror, flowAnalyzer, targetPoint));
        }
        this.reachedTypeNames = Collections.unmodifiableSet(reached);
        this.active = true;
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
            return flowAnalyzer.flowFrom(commandMirror).reachedTypeNames();
        }
        if (methodName == null && typeMirror instanceof DomainEventMirror eventMirror) {
            return flowAnalyzer.flowFrom(eventMirror).reachedTypeNames();
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
            reached.addAll(flowAnalyzer
                .flowFrom(new DomainMethod(typeMirror.getTypeName(), startingMethod))
                .reachedTypeNames());
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
            return flowAnalyzer.flowTo(eventMirror).reachedTypeNames();
        }
        if (methodName == null) {
            // any other domain type resolves to everything leading into any of its methods, plus
            // its structural MANAGES_AGGREGATE / PROVIDES_READ_MODEL counterpart, if applicable
            return flowAnalyzer.flowTo(typeMirror).reachedTypeNames();
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
            reached.addAll(flowAnalyzer
                .flowTo(new DomainMethod(typeMirror.getTypeName(), targetMethod))
                .reachedTypeNames());
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
                "Restricting a diagram to a flow (from " + includeFlowsFrom + ", to "
                    + includeFlowsTo + ") needs the result of a static analysis. Hand a"
                    + " DomainCalls instance to the DomainDiagramGenerator constructor, or drop"
                    + " the includeFlowsFrom/includeFlowsTo setting.");
        }
        return new DomainFlowFilter(domainMirror, domainCalls, flowConfig,
            hasFrom ? includeFlowsFrom : List.of(),
            hasTo ? includeFlowsTo : List.of());
    }
}

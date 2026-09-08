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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The domain flow reachable from one starting point.
 * <p>
 * A flow is a graph, not a sequence: a method may call several others, each of which continues on
 * its own. It is represented as a flat list of {@link Step}s, each linking back to its predecessor
 * ({@link Step#from()}), so the branching structure is preserved while staying easy to iterate,
 * filter and render.
 * <p>
 * The steps are ordered breadth first, starting with {@link #start()}: all steps at distance 1
 * come before those at distance 2, and so on. {@link Step#depth()} is therefore the shortest
 * distance from the start at which a node was discovered.
 * <p>
 * Cycles are contained, not silently dropped: a step reaching a node that already occurs among its
 * own predecessors is reported with {@link Step#cyclic()} and not expanded further. A node reached
 * from several places is reported once per incoming step but expanded only once.
 *
 * @author Mario Herb
 */
public class Flow {

    private final Step start;

    private final List<Step> steps;

    private final boolean truncated;

    Flow(Step start, List<Step> steps, boolean truncated) {
        this.start = Objects.requireNonNull(start, "A starting Step must be given!");
        this.steps = List.copyOf(steps);
        this.truncated = truncated;
    }

    /**
     * @return the starting point of the flow, the first entry of {@link #steps()}
     */
    public Step start() {
        return start;
    }

    /**
     * @return all steps of the flow in breadth first order, including {@link #start()}
     */
    public List<Step> steps() {
        return steps;
    }

    /**
     * @param depth the distance from the start
     * @return the steps at the given distance from the start
     */
    public List<Step> stepsAtDepth(int depth) {
        return steps.stream().filter(step -> step.depth() == depth).toList();
    }

    /**
     * The domain types touched by this flow - the primary question the flow analysis answers.
     *
     * @return the full qualified names of all types reached, in order of first occurrence
     */
    public Set<String> reachedTypeNames() {
        return steps.stream()
            .map(Step::typeName)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * @return the greatest distance from the start reached by any step
     */
    public int maxDepth() {
        return steps.stream().mapToInt(Step::depth).max().orElse(0);
    }

    /**
     * Whether the traversal stopped at the configured maximum depth while there was still
     * something left to expand. If {@code true}, the flow is incomplete.
     *
     * @return whether the flow was cut off by the depth limit
     */
    public boolean truncated() {
        return truncated;
    }

    /**
     * @return whether nothing is reachable from the starting point
     */
    public boolean isEmpty() {
        return steps.size() == 1;
    }

    /**
     * Renders the flow as an indented tree, one line per step.
     *
     * @return the rendered flow
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();
        appendSubTree(sb, start, 0);
        if (truncated) {
            sb.append("... (truncated at maximum depth)\n");
        }
        return sb.toString();
    }

    private void appendSubTree(StringBuilder sb, Step step, int indent) {
        sb.append("  ".repeat(indent));
        if (!step.isStart()) {
            sb.append(step.kind()).append(" -> ");
        }
        sb.append(step.describe());
        if (step.cyclic()) {
            sb.append(" (cycle)");
        }
        sb.append("\n");
        // identity comparison: the steps hold the very instances the traversal linked them to,
        // and it avoids the recursive equals of the whole predecessor chain
        steps.stream()
            .filter(candidate -> candidate.from().filter(from -> from == step).isPresent())
            .forEach(child -> appendSubTree(sb, child, indent + 1));
    }
}

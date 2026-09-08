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

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Options controlling a {@link FlowAnalyzer} traversal.
 * <p>
 * Nothing is filtered by default: a flow reports what the analysis found, including accessor
 * calls. Real domains produce a lot of those ({@code command.id()}, identity getters), so
 * {@link #excludingAccessors()} is provided as the filter most consumers will want.
 *
 * @param maxDepth     the maximum distance from the starting point that is expanded, must be
 *                     greater than {@code 0}
 * @param followEvents whether the flow continues across published domain events into their
 *                     listeners
 * @param methodFilter methods for which the predicate is {@code false} are neither reported nor
 *                     expanded; the starting point itself is never filtered
 * @author Mario Herb
 */
public record FlowConfig(int maxDepth,
                         boolean followEvents,
                         Predicate<DomainMethod> methodFilter) {

    /**
     * Depth limit meaning "no limit". Traversal still terminates, because every node is expanded
     * at most once per flow.
     */
    public static final int UNLIMITED_DEPTH = Integer.MAX_VALUE;

    public FlowConfig {
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("maxDepth must be greater than 0!");
        }
        Objects.requireNonNull(methodFilter, "A method filter must be given!");
    }

    /**
     * @return unlimited depth, following domain events, without filtering
     */
    public static FlowConfig defaults() {
        return new FlowConfig(UNLIMITED_DEPTH, true, methodCall -> true);
    }

    /**
     * @param maxDepth the maximum distance from the starting point that is expanded
     * @return a copy of this configuration with the given depth limit
     */
    public FlowConfig withMaxDepth(int maxDepth) {
        return new FlowConfig(maxDepth, followEvents, methodFilter);
    }

    /**
     * @param followEvents whether to continue across published domain events
     * @return a copy of this configuration with the given event behaviour
     */
    public FlowConfig withFollowEvents(boolean followEvents) {
        return new FlowConfig(maxDepth, followEvents, methodFilter);
    }

    /**
     * @param methodFilter the filter to apply to reached methods
     * @return a copy of this configuration with the given filter
     */
    public FlowConfig withMethodFilter(Predicate<DomainMethod> methodFilter) {
        return new FlowConfig(maxDepth, followEvents, methodFilter);
    }

    /**
     * Adds a filter that drops getters and setters, which otherwise dominate a flow without
     * carrying domain behaviour. Combines with an already configured filter.
     *
     * @return a copy of this configuration that additionally skips accessors
     */
    public FlowConfig excludingAccessors() {
        return withMethodFilter(methodFilter.and(
            domainMethod -> !domainMethod.mirror().isGetter()
                && !domainMethod.mirror().isSetter()));
    }
}

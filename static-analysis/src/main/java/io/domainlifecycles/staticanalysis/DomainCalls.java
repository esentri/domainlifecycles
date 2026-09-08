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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The result of a {@link StaticAnalyzer} run: which domain methods are called from which
 * other domain methods.
 * <p>
 * The graph is expressed over {@link DomainMethod} nodes. A node is a <i>callable</i>, being the
 * combination of the concrete owner type and the mirrored method. The owner type is deliberately
 * not the declaring type: an inherited method is attributed to the concrete class that owns it,
 * so {@code MyOverridingService.process} and {@code MyBaseService.process} are distinct nodes.
 * <p>
 * Both directions are indexed: {@link #callsFor(DomainMethod)} answers "what does this method call",
 * {@link #callersOf(DomainMethod)} answers "who calls this method".
 * <p>
 * A static analysis fails quietly, so the result also carries what could <i>not</i> be analyzed.
 * Check {@link #isComplete()} before trusting an empty answer, and {@link #diagnostics()} to find
 * out why something is missing.
 * <p>
 * Instances are immutable and are built through {@link #builder()}.
 *
 * @author Mario Herb
 */
public class DomainCalls {

    private final Map<DomainMethod, CalledMethods> callsByCaller;

    private final Map<DomainMethod, Set<DomainMethod>> callersByCalled;

    private final List<Diagnostic> diagnostics;

    private DomainCalls(Map<DomainMethod, CalledMethods> callsByCaller,
                        Collection<Diagnostic> diagnostics) {
        this.callsByCaller = Map.copyOf(callsByCaller);
        this.callersByCalled = buildCallersIndex(this.callsByCaller);
        this.diagnostics = List.copyOf(diagnostics);
    }

    /**
     * @return a new builder for a {@code DomainCalls} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the domain methods called from the given caller.
     * <p>
     * Never {@code null}: if the caller made no calls into the domain, or if it was not part of
     * the analysis at all, an empty {@link CalledMethods} is returned. Use {@link #callers()} to
     * distinguish "no calls" from "not present".
     *
     * @param domainMethod the calling method, must not be {@code null}
     * @return the called methods, empty if there are none
     */
    public CalledMethods callsFor(DomainMethod domainMethod) {
        Objects.requireNonNull(domainMethod, "A DomainMethod must be given!");
        return callsByCaller.getOrDefault(domainMethod, CalledMethods.EMPTY);
    }

    /**
     * @return all methods that make at least one call into the domain
     */
    public Set<DomainMethod> callers() {
        return callsByCaller.keySet();
    }

    /**
     * Returns the reverse direction of {@link #callsFor(DomainMethod)}: all domain methods known to
     * call the given method. Never {@code null}.
     *
     * @param domainMethod the called method, must not be {@code null}
     * @return the calling methods, empty if the method is never called from within the domain
     */
    public Set<DomainMethod> callersOf(DomainMethod domainMethod) {
        Objects.requireNonNull(domainMethod, "A DomainMethod must be given!");
        return callersByCalled.getOrDefault(domainMethod, Collections.emptySet());
    }

    /**
     * @return the number of callers held by this instance
     */
    public int size() {
        return callsByCaller.size();
    }

    /**
     * Everything the analysis could not do, in the order it was encountered.
     *
     * @return the diagnostics of the analysis run, empty if it ran cleanly
     */
    public List<Diagnostic> diagnostics() {
        return diagnostics;
    }

    /**
     * @param severity the severity to filter by, must not be {@code null}
     * @return the diagnostics of the given severity
     */
    public List<Diagnostic> diagnostics(Diagnostic.Severity severity) {
        Objects.requireNonNull(severity, "A Severity must be given!");
        return diagnostics.stream()
            .filter(diagnostic -> diagnostic.severity() == severity)
            .toList();
    }

    /**
     * Whether the analysis ran without hitting anything that makes the result unreliable, i.e.
     * without a {@link Diagnostic.Severity#WARNING} diagnostic. If this is {@code false}, an
     * empty {@link #callsFor(DomainMethod)} may mean "could not be analyzed" rather than "makes
     * no calls".
     *
     * @return whether the result can be read as complete
     */
    public boolean isComplete() {
        return diagnostics.stream()
            .noneMatch(diagnostic -> diagnostic.severity() == Diagnostic.Severity.WARNING);
    }

    private static Map<DomainMethod, Set<DomainMethod>> buildCallersIndex(
        Map<DomainMethod, CalledMethods> callsByCaller) {

        Map<DomainMethod, Set<DomainMethod>> index = new LinkedHashMap<>();
        callsByCaller.forEach((caller, called) ->
            called.methods().forEach(target ->
                index.computeIfAbsent(target, k -> new LinkedHashSet<>()).add(caller)));

        Map<DomainMethod, Set<DomainMethod>> unmodifiable = new LinkedHashMap<>();
        index.forEach((target, callers) ->
            unmodifiable.put(target, Collections.unmodifiableSet(callers)));
        return Collections.unmodifiableMap(unmodifiable);
    }

    /**
     * Collects the calls of an analysis run. Adding the same caller repeatedly merges the call
     * sites instead of replacing them, so a caller reachable through several signatures (an
     * inherited method, a bridge method) accumulates all of its targets.
     */
    public static final class Builder {

        private final Map<DomainMethod, List<CallSite>> collected = new LinkedHashMap<>();

        private final Set<Diagnostic> diagnostics = new LinkedHashSet<>();

        private Builder() {
        }

        /**
         * Adds call sites for a caller, merging with anything already collected for it.
         * Duplicate call sites (same target, same position) are dropped.
         *
         * @param caller    the calling method, must not be {@code null}
         * @param callSites the call sites found in the caller, must not be {@code null}
         * @return this builder
         */
        public Builder add(DomainMethod caller, List<CallSite> callSites) {
            Objects.requireNonNull(caller, "A calling DomainMethod must be given!");
            Objects.requireNonNull(callSites, "The call sites must be given!");
            List<CallSite> target = collected.computeIfAbsent(caller, k -> new ArrayList<>());
            callSites.stream()
                .filter(callSite -> !target.contains(callSite))
                .forEach(target::add);
            return this;
        }

        /**
         * Records something the analysis could not do. Identical diagnostics are collapsed.
         *
         * @param diagnostic the diagnostic to record, must not be {@code null}
         * @return this builder
         */
        public Builder add(Diagnostic diagnostic) {
            Objects.requireNonNull(diagnostic, "A Diagnostic must be given!");
            diagnostics.add(diagnostic);
            return this;
        }

        /**
         * Records several diagnostics. Identical diagnostics are collapsed.
         *
         * @param diagnostics the diagnostics to record, must not be {@code null}
         * @return this builder
         */
        public Builder addAll(Collection<Diagnostic> diagnostics) {
            Objects.requireNonNull(diagnostics, "The diagnostics must be given!");
            this.diagnostics.addAll(diagnostics);
            return this;
        }

        /**
         * @return the immutable analysis result
         */
        public DomainCalls build() {
            Map<DomainMethod, CalledMethods> result = new LinkedHashMap<>();
            collected.forEach((caller, callSites) ->
                result.put(caller, new CalledMethods(callSites)));
            return new DomainCalls(result, diagnostics);
        }
    }

    /**
     * The methods called from one caller.
     * <p>
     * {@link #callSites()} holds one entry per resolved invoke instruction, including its source
     * position, in the order the analysis encountered them: the direct statements of the caller's
     * body first, then the bodies it descends into (lambdas, method references). Because a target
     * may be called more than once, {@link #methods()} exposes the same targets deduplicated,
     * keeping the order of first occurrence.
     */
    public static final class CalledMethods {

        static final CalledMethods EMPTY = new CalledMethods(List.of());

        private final List<CallSite> callSites;

        private final List<DomainMethod> methods;

        CalledMethods(List<CallSite> callSites) {
            this.callSites = List.copyOf(callSites);
            this.methods = this.callSites.stream()
                .map(CallSite::called)
                .distinct()
                .toList();
        }

        /**
         * @return the resolved invoke instructions, one entry per call site
         */
        public List<CallSite> callSites() {
            return callSites;
        }

        /**
         * @return the called methods, deduplicated, in order of first occurrence
         */
        public List<DomainMethod> methods() {
            return methods;
        }

        /**
         * @return whether no call into the domain was found
         */
        public boolean isEmpty() {
            return callSites.isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CalledMethods that)) {
                return false;
            }
            return callSites.equals(that.callSites);
        }

        @Override
        public int hashCode() {
            return callSites.hashCode();
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            for (var callSite : callSites) {
                sb.append(callSite).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * One resolved invoke instruction.
     * <p>
     * {@code callSiteTypeName} is the type whose method body contained the instruction. It is not
     * necessarily the owner type of the caller: for an inherited method the body lives in the base
     * class, and a call found by descending into a method reference lives in the referenced type.
     * {@code lineNumber} refers to that type's source file and is {@code -1} when the analyzed
     * bytecode carries no line number table.
     *
     * @param called           the called method
     * @param callSiteTypeName the type whose body contained the invoke instruction
     * @param lineNumber       the source line of the invoke instruction, or {@code -1} if unknown
     */
    public record CallSite(DomainMethod called, String callSiteTypeName, int lineNumber) {

        /**
         * Marker for a call site whose source line could not be determined.
         */
        public static final int UNKNOWN_LINE = -1;

        public CallSite {
            Objects.requireNonNull(called, "A called DomainMethod must be given!");
        }

        /**
         * @return the source line of the invoke instruction, empty if unknown
         */
        public Optional<Integer> line() {
            return lineNumber > 0 ? Optional.of(lineNumber) : Optional.empty();
        }

        @Override
        public String toString() {
            if (lineNumber > 0) {
                return called + " [" + callSiteTypeName + ":" + lineNumber + "]";
            }
            return called.toString();
        }
    }
}

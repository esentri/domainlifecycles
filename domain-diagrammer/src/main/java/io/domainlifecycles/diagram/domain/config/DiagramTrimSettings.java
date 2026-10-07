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

package io.domainlifecycles.diagram.domain.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A configuration class that defines settings for trimming or filtering elements in a diagram.
 *
 * The class provides options to configure blacklisted classes, transitive filtering rules,
 * and package names to be explicitly included during the diagram generation process.
 * 
 * @author Mario Herb
 */
public class DiagramTrimSettings {

    private final List<String> classesBlacklist;
    private final List<String> explicitlyIncludedPackageNames;

    private final List<String> includeConnectedTo;
    private final List<String> includeConnectedToIngoing;
    private final List<String> includeConnectedToOutgoing;
    private final List<String> excludeConnectedToIngoing;
    private final List<String> excludeConnectedToOutgoing;
    private final List<String> includeFlowsFrom;
    private final List<String> includeFlowsTo;
    private final int includeConnectedToIngoingDepth;
    private final int includeConnectedToOutgoingDepth;

    /**
     * Gets the starting points of the flows the diagram is restricted to.
     * <p>
     * Unlike the connection based settings, which walk the structural relations of the mirror,
     * a flow is walked over the analyzed method calls of the domain. It therefore requires the
     * result of a static analysis to be handed to the generator.
     * <p>
     * An entry is a full qualified type name, optionally followed by {@code #} and a method name:
     * <ul>
     *     <li>a domain command or domain event starts the flow triggered by it,</li>
     *     <li>any other domain type starts the flows of all of its methods,</li>
     *     <li>{@code type#method} starts the flows of all overloads of that method.</li>
     * </ul>
     * Several entries are united, as the other include settings are.
     *
     * @return List of flow starting points, empty if the diagram is not restricted to a flow
     */
    public List<String> getIncludeFlowsFrom() {
        return includeFlowsFrom;
    }

    /**
     * Gets the target points of the flows the diagram is restricted to - the backward counterpart
     * of {@link #getIncludeFlowsFrom()}: instead of "what does this lead to", it answers "what
     * leads into this", i.e. the entry channels through which a type or method is reached.
     * <p>
     * Same entry syntax and requirements as {@link #getIncludeFlowsFrom()} (a full qualified type
     * name, optionally followed by {@code #} and a method name; requires the result of a static
     * analysis). There is one difference: a domain command can never be a target, since nothing in
     * the analyzed data models where a command originates - a command can still appear as a
     * reached node on the way to a target, just never as a target itself.
     * <p>
     * If both {@link #getIncludeFlowsFrom()} and this are configured, their reached types are
     * united: a type survives if reached by either direction.
     *
     * @return List of flow target points, empty if the diagram is not restricted to a backward flow
     */
    public List<String> getIncludeFlowsTo() {
        return includeFlowsTo;
    }

    /**
     * Determines whether the diagram is restricted to one or more flows, forward or backward.
     *
     * @return {@code true} if at least one flow starting or target point is configured
     */
    public boolean hasFlowSettings() {
        return !this.getIncludeFlowsFrom().isEmpty() || !this.getIncludeFlowsTo().isEmpty();
    }

    /**
     * Gets the list of blacklisted class names that should be excluded from the diagram.
     *
     * @return List of fully qualified class names to exclude
     */
    public List<String> getClassesBlacklist() {
        return classesBlacklist;
    }

    /**
     * Gets the list of class names that should be included in the diagram along with their connected nodes.
     *
     * @return List of fully qualified class names whose connected nodes should be included
     */
    public List<String> getIncludeConnectedTo() {
        return includeConnectedTo;
    }

    /**
     * Gets the list of class names that should be included in the diagram along with nodes that have ingoing connections to them.
     * A class may be named in {@link #getIncludeConnectedToOutgoing()} as well, to show what leads to it and what it
     * leads to.
     *
     * @return List of fully qualified class names whose ingoing connected nodes should be included
     */
    public List<String> getIncludeConnectedToIngoing() {
        return includeConnectedToIngoing;
    }

    /**
     * Gets the list of class names that should be included in the diagram along with nodes that have outgoing connections from them.
     *
     * @return List of fully qualified class names whose outgoing connected nodes should be included
     */
    public List<String> getIncludeConnectedToOutgoing() {
        return includeConnectedToOutgoing;
    }

    /**
     * Gets up to how many steps the nodes with ingoing connections to the classes of
     * {@link #getIncludeConnectedToIngoing()} are followed - "what leads to them". {@code 1} includes the nodes directly
     * connected to them, {@code 2} also the nodes connected to these, and so on. {@code 0} or a negative value
     * follows the complete path.
     * <p>
     * An interface and its implementations count as one step, and the classes drawn together with a class - e.g. the
     * entities of an aggregate - take no step.
     *
     * @return the depth of the ingoing connections, {@code 0} or negative for the complete path
     */
    public int getIncludeConnectedToIngoingDepth() {
        return includeConnectedToIngoingDepth;
    }

    /**
     * Gets up to how many steps the nodes with outgoing connections from the classes of
     * {@link #getIncludeConnectedToOutgoing()} are followed - "what they lead to". Counted like
     * {@link #getIncludeConnectedToIngoingDepth()}; {@code 0} or a negative value follows the complete path.
     *
     * @return the depth of the outgoing connections, {@code 0} or negative for the complete path
     */
    public int getIncludeConnectedToOutgoingDepth() {
        return includeConnectedToOutgoingDepth;
    }

    /**
     * Gets the list of class names whose ingoing connections should be excluded from the diagram.
     *
     * @return List of fully qualified class names whose ingoing connections should be excluded
     */
    public List<String> getExcludeConnectedToIngoing() {
        return excludeConnectedToIngoing;
    }

    /**
     * Gets the list of class names whose outgoing connections should be excluded from the diagram.
     *
     * @return List of fully qualified class names whose outgoing connections should be excluded
     */
    public List<String> getExcludeConnectedToOutgoing() {
        return excludeConnectedToOutgoing;
    }

    /**
     * Retrieves the list of explicitly included package names.
     *
     * @return List of fully qualified package names that are explicitly included in the diagram.
     */
    public List<String> getExplicitlyIncludedPackageNames() {
        return explicitlyIncludedPackageNames;
    }

    /**
     * Determines whether there are any included settings for connected types in the diagram.
     * Connected types can include incoming, outgoing, or directly connected nodes.
     *
     * @return {@code true} if there are any non-empty settings for included connected types,
     *         such as for directly connected nodes or for nodes with incoming or outgoing connections;
     *         {@code false} otherwise
     */
    public boolean hasIncludedConnectedTypeSettings(){
        return !(this.getIncludeConnectedToIngoing().isEmpty()
            && this.getIncludeConnectedToOutgoing().isEmpty()
            && this.getIncludeConnectedTo().isEmpty());
    }

    /**
     * Determines whether there are any excluded settings for connected types in the diagram.
     * Connected types can include nodes with either ingoing or outgoing excluded connections.
     *
     * @return {@code true} if there are any non-empty settings for excluded connected types,
     *         such as for nodes with ingoing or outgoing excluded connections; {@code false} otherwise.
     */
    public boolean hasExcludedConnectedTypeSettings(){
        return !(this.getExcludeConnectedToIngoing().isEmpty()
            && this.getExcludeConnectedToOutgoing().isEmpty());
    }

    private DiagramTrimSettings(
        List<String> classesBlacklist,
        List<String> explicitlyIncludedPackageNames, 
        List<String> includeConnectedTo, 
        List<String> includeConnectedToIngoing, 
        List<String> includeConnectedToOutgoing, 
        List<String> excludeConnectedToIngoing,
        List<String> excludeConnectedToOutgoing,
        List<String> includeFlowsFrom,
        List<String> includeFlowsTo,
        int includeConnectedToIngoingDepth,
        int includeConnectedToOutgoingDepth
    ) {
        this.classesBlacklist = classesBlacklist;
        this.explicitlyIncludedPackageNames = explicitlyIncludedPackageNames;
        this.includeConnectedTo = includeConnectedTo;
        this.includeConnectedToIngoing = includeConnectedToIngoing;
        this.includeConnectedToOutgoing = includeConnectedToOutgoing;
        this.excludeConnectedToIngoing = excludeConnectedToIngoing;
        this.excludeConnectedToOutgoing = excludeConnectedToOutgoing;
        this.includeFlowsFrom = includeFlowsFrom;
        this.includeFlowsTo = includeFlowsTo;
        this.includeConnectedToIngoingDepth = includeConnectedToIngoingDepth;
        this.includeConnectedToOutgoingDepth = includeConnectedToOutgoingDepth;
    }

    /**
     * Creates a new builder instance for constructing DiagramTrimSettings.
     *
     * @return A new DiagramTrimSettingsBuilder instance
     */
    public static DiagramTrimSettingsBuilder builder() {
        return new DiagramTrimSettingsBuilder();
    }

    /**
     * DomainDiagramConfigBuilder class for creating instances of DiagramTrimSettings.
     */
    public static class DiagramTrimSettingsBuilder {
        private List<String> classesBlacklist$value;
        private List<String> explicitlyIncludedPackageNames$value;
        private List<String> includeConnectedTo$value;
        private List<String> includeConnectedToIngoing$value;
        private List<String> includeConnectedToOutgoing$value;
        private List<String> excludeConnectedToIngoing$value;
        private List<String> excludeConnectedToOutgoing$value;
        private List<String> includeFlowsFrom$value;
        private List<String> includeFlowsTo$value;
        private int includeConnectedToIngoingDepth$value;
        private int includeConnectedToOutgoingDepth$value;

        /**
         * Sets the list of blacklisted classes.
         *
         * @param classesBlacklist List of fully qualified class names to exclude
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withClassesBlacklist(List<String> classesBlacklist) {
            this.classesBlacklist$value = classesBlacklist;
            return this;
        }

        /**
         * Sets the list of explicitly included package names.
         *
         * @param explicitlyIncludedPackageNames List of fully qualified package names to include
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withExplicitlyIncludedPackageNames(List<String> explicitlyIncludedPackageNames) {
            this.explicitlyIncludedPackageNames$value = explicitlyIncludedPackageNames;
            return this;
        }

        /**
         * Sets the list of class names that should be included in the diagram along with their connected nodes.
         *
         * @param includeConnectedTo List of fully qualified class names whose connected nodes should be included
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeConnectedTo(List<String> includeConnectedTo) {
            this.includeConnectedTo$value = includeConnectedTo;
            return this;
        }

        /**
         * Sets the list of class names that should be included in the diagram along with nodes that have ingoing connections to them.
         *
         * @param includeConnectedToIngoing List of fully qualified class names whose ingoing connected nodes should be included
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeConnectedToIngoing(List<String> includeConnectedToIngoing) {
            this.includeConnectedToIngoing$value = includeConnectedToIngoing;
            return this;
        }

        /**
         * Sets the list of class names that should be included in the diagram along with nodes that have outgoing connections from them.
         *
         * @param includeConnectedToOutgoing List of fully qualified class names whose outgoing connected nodes should be included
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeConnectedToOutgoing(List<String> includeConnectedToOutgoing) {
            this.includeConnectedToOutgoing$value = includeConnectedToOutgoing;
            return this;
        }

        /**
         * Limits how many steps the ingoing connections of {@link #withIncludeConnectedToIngoing(List)} are followed,
         * see {@link DiagramTrimSettings#getIncludeConnectedToIngoingDepth()}.
         *
         * @param includeConnectedToIngoingDepth the number of steps, {@code 0} or negative for the complete path (the
         *                                       default)
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeConnectedToIngoingDepth(int includeConnectedToIngoingDepth) {
            this.includeConnectedToIngoingDepth$value = includeConnectedToIngoingDepth;
            return this;
        }

        /**
         * Limits how many steps the outgoing connections of {@link #withIncludeConnectedToOutgoing(List)} are followed,
         * see {@link DiagramTrimSettings#getIncludeConnectedToOutgoingDepth()}.
         *
         * @param includeConnectedToOutgoingDepth the number of steps, {@code 0} or negative for the complete path (the
         *                                        default)
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeConnectedToOutgoingDepth(int includeConnectedToOutgoingDepth) {
            this.includeConnectedToOutgoingDepth$value = includeConnectedToOutgoingDepth;
            return this;
        }

        /**
         * Sets the list of class names whose ingoing connections should be excluded from the diagram.
         *
         * @param excludeConnectedToIngoing List of fully qualified class names whose ingoing connections should be excluded
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withExcludeConnectedToIngoing(List<String> excludeConnectedToIngoing) {
            this.excludeConnectedToIngoing$value = excludeConnectedToIngoing;
            return this;
        }

        /**
         * Sets the list of class names whose outgoing connections should be excluded from the diagram.
         *
         * @param excludeConnectedToOutgoing List of fully qualified class names whose outgoing connections should be excluded
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withExcludeConnectedToOutgoing(List<String> excludeConnectedToOutgoing) {
            this.excludeConnectedToOutgoing$value = excludeConnectedToOutgoing;
            return this;
        }

        /**
         * Restricts the diagram to the domain types reached by the flows starting at the given
         * points. Requires the result of a static analysis to be handed to the generator, see
         * {@link DiagramTrimSettings#getIncludeFlowsFrom()} for the entry syntax.
         *
         * @param includeFlowsFrom List of flow starting points, a full qualified type name each,
         *                         optionally followed by {@code #} and a method name
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeFlowsFrom(List<String> includeFlowsFrom) {
            this.includeFlowsFrom$value = includeFlowsFrom;
            return this;
        }

        /**
         * Restricts the diagram to the domain types leading into the given target points - the
         * backward counterpart of {@link #withIncludeFlowsFrom(List)}. Requires the result of a
         * static analysis to be handed to the generator, see
         * {@link DiagramTrimSettings#getIncludeFlowsTo()} for the entry syntax and how it combines
         * with {@link #withIncludeFlowsFrom(List)}.
         *
         * @param includeFlowsTo List of flow target points, a full qualified type name each,
         *                       optionally followed by {@code #} and a method name
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withIncludeFlowsTo(List<String> includeFlowsTo) {
            this.includeFlowsTo$value = includeFlowsTo;
            return this;
        }

        /**
         * Builds and returns a new DiagramTrimSettings instance.
         *
         * @return A new DiagramTrimSettings instance with the configured settings
         */
        public DiagramTrimSettings build() {
            // a class may be followed in both directions - "what leads to it" and "what does it lead to" - but must
            // not be included and excluded at once, nor followed in both directions completely (includeConnectedTo)
            // and in one of them
            checkNoOverlap(
                "includeConnectedTo", union(includeConnectedTo$value),
                "includeConnectedToIngoing/includeConnectedToOutgoing",
                union(includeConnectedToIngoing$value, includeConnectedToOutgoing$value),
                "excludeConnectedToIngoing/excludeConnectedToOutgoing",
                union(excludeConnectedToIngoing$value, excludeConnectedToOutgoing$value)
            );


            return new DiagramTrimSettings(
                classesBlacklist$value == null ? Collections.emptyList() : classesBlacklist$value,
                explicitlyIncludedPackageNames$value == null ? Collections.emptyList() : explicitlyIncludedPackageNames$value,
                includeConnectedTo$value == null ? Collections.emptyList() : includeConnectedTo$value,
                includeConnectedToIngoing$value == null ? Collections.emptyList() : includeConnectedToIngoing$value,
                includeConnectedToOutgoing$value == null ? Collections.emptyList() : includeConnectedToOutgoing$value,
                excludeConnectedToIngoing$value == null ? Collections.emptyList() : excludeConnectedToIngoing$value,
                excludeConnectedToOutgoing$value == null ? Collections.emptyList() : excludeConnectedToOutgoing$value,
                // deliberately not part of checkNoOverlap: a flow starting/target point may well
                // also be named in a connection setting, the mechanisms are independent
                includeFlowsFrom$value == null ? Collections.emptyList() : includeFlowsFrom$value,
                includeFlowsTo$value == null ? Collections.emptyList() : includeFlowsTo$value,
                includeConnectedToIngoingDepth$value,
                includeConnectedToOutgoingDepth$value);
        }
    }

    @SafeVarargs
    private static Set<String> union(Collection<String>... collections) {
        Set<String> union = new HashSet<>();
        for (Collection<String> collection : collections) {
            if (collection != null) {
                union.addAll(collection);
            }
        }
        return union;
    }

    private static void checkNoOverlap(String firstName, Set<String> first,
                                       String secondName, Set<String> second,
                                       String thirdName, Set<String> third) {
        checkNoOverlap(firstName, first, secondName, second);
        checkNoOverlap(firstName, first, thirdName, third);
        checkNoOverlap(secondName, second, thirdName, third);
    }

    private static void checkNoOverlap(String firstName, Set<String> first, String secondName, Set<String> second) {
        first.stream()
            .filter(second::contains)
            .findFirst()
            .ifPresent(element -> {
                throw new IllegalArgumentException("The class " + element + " was found in the trim settings "
                    + firstName + " and " + secondName + " at once, which exclude each other.");
            });
    }

}

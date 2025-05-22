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

import java.util.Collections;
import java.util.List;

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
        List<String> excludeConnectedToOutgoing
    ) {
        this.classesBlacklist = classesBlacklist;
        this.explicitlyIncludedPackageNames = explicitlyIncludedPackageNames;
        this.includeConnectedTo = includeConnectedTo;
        this.includeConnectedToIngoing = includeConnectedToIngoing;
        this.includeConnectedToOutgoing = includeConnectedToOutgoing;
        this.excludeConnectedToIngoing = excludeConnectedToIngoing;
        this.excludeConnectedToOutgoing = excludeConnectedToOutgoing;
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
         * Builds and returns a new DiagramTrimSettings instance.
         *
         * @return A new DiagramTrimSettings instance with the configured settings
         */
        public DiagramTrimSettings build() {
            return new DiagramTrimSettings(
                classesBlacklist$value == null ? Collections.emptyList() : classesBlacklist$value,
                explicitlyIncludedPackageNames$value == null ? Collections.emptyList() : explicitlyIncludedPackageNames$value,
                includeConnectedTo$value == null ? Collections.emptyList() : includeConnectedTo$value,
                includeConnectedToIngoing$value == null ? Collections.emptyList() : includeConnectedToIngoing$value,
                includeConnectedToOutgoing$value == null ? Collections.emptyList() : includeConnectedToOutgoing$value,
                excludeConnectedToIngoing$value == null ? Collections.emptyList() : excludeConnectedToIngoing$value,
                excludeConnectedToOutgoing$value == null ? Collections.emptyList() : excludeConnectedToOutgoing$value);
        }
    }

}

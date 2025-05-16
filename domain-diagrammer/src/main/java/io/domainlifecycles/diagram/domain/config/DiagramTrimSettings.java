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
    private final List<String> transitiveFilterSeedDomainServiceTypeNames;
    private final List<String> explicitlyIncludedPackageNames;

    /**
     * Gets the list of blacklisted class names that should be excluded from the diagram.
     *
     * @return List of fully qualified class names to exclude
     */
    public List<String> getClassesBlacklist() {
        return classesBlacklist;
    }

    /**
     * Gets the list of domain service type names that will be used as seed points for transitive filtering.
     *
     * @return List of domain service type names for transitive filtering
     */
    public List<String> getTransitiveFilterSeedDomainServiceTypeNames() {
        return transitiveFilterSeedDomainServiceTypeNames;
    }

    /**
     * Retrieves the list of explicitly included package names.
     *
     * @return List of fully qualified package names that are explicitly included in the diagram.
     */
    public List<String> getExplicitlyIncludedPackageNames() {
        return explicitlyIncludedPackageNames;
    }

    private DiagramTrimSettings(
        List<String> classesBlacklist,
        List<String> transitiveFilterSeedDomainServiceTypeNames,
        List<String> explicitlyIncludedPackageNames) {
        this.classesBlacklist = classesBlacklist;
        this.transitiveFilterSeedDomainServiceTypeNames = transitiveFilterSeedDomainServiceTypeNames;
        this.explicitlyIncludedPackageNames = explicitlyIncludedPackageNames;
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
        private List<String> transitiveFilterSeedDomainServiceTypeNames$value;
        private List<String> explicitlyIncludedPackageNames$value;

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
         * Sets the list of domain service type names for transitive filtering.
         *
         * @param transitiveFilterSeedDomainServiceTypeNames List of domain service type names
         * @return This builder instance
         */
        public DiagramTrimSettingsBuilder withTransitiveFilterSeedDomainServiceTypeNames(List<String> transitiveFilterSeedDomainServiceTypeNames) {
            this.transitiveFilterSeedDomainServiceTypeNames$value = transitiveFilterSeedDomainServiceTypeNames;
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
         * Builds and returns a new DiagramTrimSettings instance.
         *
         * @return A new DiagramTrimSettings instance with the configured settings
         */
        public DiagramTrimSettings build() {
            return new DiagramTrimSettings(
                classesBlacklist$value == null ? Collections.emptyList() : classesBlacklist$value,
                transitiveFilterSeedDomainServiceTypeNames$value == null ? Collections.emptyList() : transitiveFilterSeedDomainServiceTypeNames$value,
                explicitlyIncludedPackageNames$value == null ? Collections.emptyList() : explicitlyIncludedPackageNames$value
            );
        }
    }

}

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

package io.domainlifecycles.plugin.mirror;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

/**
 * Represents the configuration required for serializing domain models into JSON files.
 *
 * This configuration allows for specifying the output file name and the related context packages
 * that are required for the serialization process. These configurations are typically utilized
 * by plugins, such as `JsonRenderGoal`, to generate and store JSON representations of domain models.
 *
 * Fields:
 * - `fileName`: The name of the JSON file to be generated, as defined in the Maven plugin configuration.
 * - `contextPackages`: A list of package names that should be included during the serialization process.
 *
 * Responsibilities:
 * - Encapsulates metadata related to JSON serialization, including the file name and context packages.
 * - Used by higher-level components to facilitate domain-specific serialization workflows.
 *
 * Typical usage:
 * - As part of a Maven plugin configuration, where instances of this class define the details of
 *   the serialization process to be executed during a build lifecycle phase.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class PluginSerializationConfiguration {

    @Parameter(property = "fileName", defaultValue = "mirror.json")
    private String fileName;

    @Parameter(property = "domainModelPackages", required = true)
    private List<String> domainModelPackages;

    /**
     * Packages whose types, as superclass or interface (direct or inherited), exclude a class from being
     * mirrored as non-domain class. Unset means the default {@code org.jooq}, which leaves out the code
     * jOOQ generates for tables, records, schemas and catalogs; an empty list also means this default
     * (Maven cannot distinguish an unset list parameter from an empty one).
     */
    @Parameter(property = "nonDomainExcludedSupertypePackages", required = false)
    private List<String> nonDomainExcludedSupertypePackages;

    /**
     * Packages whose classes are not mirrored as non-domain classes, e.g. generated code without a common
     * supertype. Unset means none.
     */
    @Parameter(property = "nonDomainExcludedPackages", required = false)
    private List<String> nonDomainExcludedPackages;

    /**
     * Retrieves the name of the file associated with this configuration.
     *
     * The file name represents the intended name for the JSON output
     * as defined in the plugin's serialization configuration.
     *
     * @return the name of the file to be used for JSON output
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Retrieves the list of domain model packages defined in the serialization configuration.
     *
     * The context packages represent the package names required during the serialization
     * process to include relevant domain models or resources.
     *
     * @return a list of context package names to be used for JSON serialization
     */
    public List<String> getDomainModelPackages() {
        return domainModelPackages;
    }

    /**
     * Gets the packages whose types, as supertypes, exclude a class from the mirrored non-domain classes.
     *
     * @return the excluded supertype packages, {@code null} or empty for the default ({@code org.jooq})
     */
    public List<String> getNonDomainExcludedSupertypePackages() {
        return nonDomainExcludedSupertypePackages;
    }

    /**
     * Gets the packages whose classes are not mirrored as non-domain classes.
     *
     * @return the excluded packages, or {@code null} for none
     */
    public List<String> getNonDomainExcludedPackages() {
        return nonDomainExcludedPackages;
    }
}

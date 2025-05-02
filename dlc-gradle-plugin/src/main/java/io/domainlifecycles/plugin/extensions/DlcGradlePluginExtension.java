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

package io.domainlifecycles.plugin.extensions;

import org.gradle.api.provider.Property;

/**
 * Represents the Gradle plugin extension for configuring diagram and JSON model generation tasks.
 *
 * This class serves as the main entry point for defining configuration properties for
 * the diagram and JSON model output generation during the build process.
 * It provides access to the {@link DiagramTaskConfigurationExtension} and
 * {@link JsonTaskConfigurationExtension} for detailed configuration of these tasks.
 *
 * @author Leon Völlinger
 */
public abstract class DlcGradlePluginExtension {

    /**
     * Retrieves the configuration for diagram generation tasks.
     *
     * This method provides access to the configuration properties specific
     * to the generation of diagrams, such as the output directory and the
     * diagrams to be generated.
     *
     * @return a {@code Property} encapsulating the {@code DiagramTaskConfigurationExtension},
     *         which contains the settings related to diagram generation tasks.
     */
    public abstract Property<DiagramTaskConfigurationExtension> getDiagram();

    /**
     * Retrieves the configuration for JSON model generation tasks.
     *
     * This method provides access to the configuration properties specific
     * to the generation of JSON models, such as the output directory and
     * serialization configurations for the JSON files.
     *
     * @return a {@code Property} encapsulating the {@code JsonTaskConfigurationExtension},
     *         which contains the settings related to JSON model generation tasks.
     */
    public abstract Property<JsonTaskConfigurationExtension> getJsonModel();

    /**
     * Retrieves the configuration for Domain model upload tasks.
     *
     * This method provides access to the configuration properties specific
     * to the upload of Domain models, such as the Diagram-Viewer projectname, API-KEY, Context-Packages,
     * and Diagram Viewer base url.
     *
     * @return a {@code Property} encapsulating the {@code DomainModelUploadTaskConfigurationExtension},
     *         which contains the settings related to JSON model generation tasks.
     */
    public abstract Property<DomainModelUploadTaskConfigurationExtension> getDomainModelUpload();
}

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
 *  Copyright 2019-2024 the original author or authors.
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

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Represents the configuration for domain model upload tasks in a Gradle plugin.
 *
 * This abstract class defines methods for accessing configuration properties required
 * to upload domain models to an external server or system. These configurations include
 * the base URL for the diagram viewer, API key for authentication, project name for
 * association, and a list of context package names related to the domain model.
 *
 * @author Leon Völlinger
 */
public abstract class DomainModelUploadTaskConfigurationExtension {

    /**
     * Retrieves the base URL for the diagram viewer.
     *
     * This property specifies the base URL used to access or interact with the diagram viewer
     * in the domain model upload configuration.
     *
     * @return a {@code Property<String>} representing the base URL for the diagram viewer.
     */
    public abstract Property<String> getDiagramViewerBaseUrl();

    /**
     * Retrieves the API key property for authentication.
     *
     * This method provides access to the API key that is required
     * for authenticating requests made during the domain model upload tasks.
     *
     * @return a {@code Property<String>} representing the API key.
     */
    public abstract Property<String> getApiKey();

    /**
     * Retrieves the project name property for the domain model upload task configuration.
     *
     * This method provides access to the project name associated with the domain model upload task.
     * The project name is used to identify and associate the uploaded domain models with a specific
     * project in the external system or service.
     *
     * @return a {@code Property<String>} representing the project name.
     */
    public abstract Property<String> getProjectName();

    /**
     * Retrieves the list of context package names relevant to the domain model.
     *
     * This method provides access to the context packages, which specify the
     * scoped Java packages associated with the domain model in the upload task configuration.
     * These packages are typically used to identify and isolate specific parts of the domain model.
     *
     * @return a {@code ListProperty<String>} representing the list of context package names.
     */
    public abstract ListProperty<String> getContextPackages();
}

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

package io.domainlifecycles.plugins.viewer;

import java.util.List;

/**
 * The DomainModelUploader interface defines a contract for components that facilitate
 * the process of uploading a serialized domain model to a designated Diagram Viewer system.
 *
 * This interface requires the implementation of a method to handle the uploading process
 * by providing the necessary domain model data, configuration parameters, and communication
 * details for the target system.
 *
 * Responsibilities:
 * - Handle the serialization and packaging of the domain model data.
 * - Ensure the delivery of the domain model to the Diagram Viewer through an HTTP-based API.
 * - Manage authentication and project-specific configurations during the upload.
 *
 * @author Leon Völlinger
 */
public interface DomainModelUploader {

    /**
     * Uploads a serialized domain model to a specific project in the Diagram Viewer platform.
     *
     * This method facilitates the process of uploading a domain model, represented in JSON format,
     * along with corresponding configuration details required for proper association with the
     * target project on the Diagram Viewer.
     *
     * The method prepares and sends the required HTTP request, including authentication and
     * project-specific headers, and handles any potential errors during the upload process.
     *
     * @param domainModelJson       a JSON-string representation of the domain model to be uploaded
     * @param domainModelPackages   a list of package names defining the domain model classes for processing
     * @param apiKey                the API key required for authentication with the Diagram Viewer platform
     * @param projectName           the name of the target project on the Diagram Viewer platform
     * @param diagramViewerBaseUrl  the base URL of the Diagram Viewer instance where the domain model should be uploaded
     */
    void uploadDomainModel(String domainModelJson, List<String> domainModelPackages, String apiKey, String projectName, String diagramViewerBaseUrl);
}

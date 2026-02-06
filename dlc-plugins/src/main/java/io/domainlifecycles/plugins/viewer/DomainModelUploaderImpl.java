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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.plugins.viewer.model.DomainMirrorUploadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Implementation of the DomainModelUploader interface for uploading a serialized domain model
 * to a designated Diagram Viewer system. This implementation handles the process of preparing
 * and executing an HTTP PUT request to upload the domain model, along with necessary metadata
 * and authorization headers.
 *
 * Responsibilities:
 * - Prepares an HTTP request to upload the domain model to the Diagram Viewer system.
 * - Ensures proper error handling and logging during the upload process.
 * - Formats the domain model data and additional configuration into a JSON request body.
 * - Configures required authentication headers for secure communication.
 *
 * @author Leon Völlinger
 */
public class DomainModelUploaderImpl implements DomainModelUploader {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomainModelUploaderImpl.class);

    private static final String DIAGRAM_VIEWER_DOMAIN_MIRROR_UPLOAD_PATH = "/api/upload/domain-mirror/";
    private static final String API_KEY_HEADER_NAME = "X-API-KEY";

    /**
     * Uploads a domain model to a Diagram Viewer project using the provided configuration and authentication details.
     *
     * This method sends a serialized domain model in JSON format to a specified Diagram Viewer instance,
     * associating it with the target project. It handles request preparation, API key authentication,
     * error handling, and logs pertinent information during the process.
     *
     * @param domainMirrorJson       a JSON-string representation of the domain model to be uploaded
     * @param domainModelPackages    a list of package names that define the domain model classes to be included
     * @param apiKey                 the API key required for authenticating with the Diagram Viewer platform
     * @param projectName            the name of the target project where the domain model will be uploaded
     * @param diagramViewerBaseUrl   the base URL of the Diagram Viewer instance to which the upload request is sent
     */
    @Override
    public void uploadDomainModel(String domainMirrorJson, List<String> domainModelPackages, String apiKey, String projectName, String diagramViewerBaseUrl) {
        LOGGER.debug(String.format("Trying to upload Domain-Model to Diagram-Viewer project '%s' with base url '%s'.", projectName, diagramViewerBaseUrl));

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = buildDomainMirrorUploadRequest(domainMirrorJson, domainModelPackages, projectName, apiKey, diagramViewerBaseUrl);

        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            handleResponseErrors(response);
            LOGGER.info("Successfully uploaded Domain-Mirror to Diagram-Viewer.");
        } catch (IOException | InterruptedException e) {
            throw DLCPluginsException.fail("Could not send Domain-Mirror to Diagram-Viewer.", e);
        }
    }

    private HttpRequest buildDomainMirrorUploadRequest(String domainMirrorJson, List<String> domainPackages, String projectName, String apiKey, String diagramViewerBaseUrl) {
        final String domainMirrorUploadDtoJsonString = generateJsonRequestBody(domainMirrorJson, domainPackages);

        return HttpRequest.newBuilder()
            .uri(URI.create(diagramViewerBaseUrl + DIAGRAM_VIEWER_DOMAIN_MIRROR_UPLOAD_PATH + projectName))
            .header("Content-Type", "application/json")
            .header(API_KEY_HEADER_NAME, apiKey)
            .PUT(BodyPublishers.ofString(domainMirrorUploadDtoJsonString))
            .build();
    }

    private static String generateJsonRequestBody(String domainMirrorJson, List<String> domainModelPackages) {
        DomainMirrorUploadDto domainMirrorUploadDto = new DomainMirrorUploadDto(domainMirrorJson, domainModelPackages);
        ObjectMapper o = new ObjectMapper();
        try {
            return o.writeValueAsString(domainMirrorUploadDto);
        } catch (JsonProcessingException e) {
            throw DLCPluginsException.fail("Could not serialize DomainMirrorUploadDTO to JSON.", e);
        }
    }

    private void handleResponseErrors(HttpResponse<String> response) {
        if(response.statusCode() == 200) {
            return;
        }

        throw DLCPluginsException.fail(
            String.format("Diagram-Viewer returned failure while processing Domain-Model. Status-Code: '%s'. Error-Message: '%s'.",
                response.statusCode(), response.body()));
    }
}

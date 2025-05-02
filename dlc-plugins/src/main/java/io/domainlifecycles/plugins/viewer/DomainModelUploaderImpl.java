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

import io.domainlifecycles.plugins.exception.DLCPluginsException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainModelUploaderImpl implements DomainModelUploader {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomainModelUploaderImpl.class);

    private static final String DIAGRAM_VIEWER_DOMAIN_MODEL_UPLOAD_PATH = "/api/domain-model/";
    private static final String API_KEY_HEADER_NAME = "X-API-KEY";

    @Override
    public void uploadDomainModel(String domainModelJson, String apiKey, String projectName, String diagramViewerBaseUrl) {
        LOGGER.debug(String.format("Trying to upload Domain-Model to Diagram-Viewer project '%s' with base url '%s'.", projectName, diagramViewerBaseUrl));

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = buildDomainModelUploadRequest(domainModelJson, projectName, apiKey, diagramViewerBaseUrl);

        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            handleResponseErrors(response);
            LOGGER.info("Successfully uploaded Domain-Model to Diagram-Viewer.");
        } catch (IOException | InterruptedException e) {
            throw DLCPluginsException.fail("Could not send Domain-Model to Diagram-Viewer.", e);
        }
    }

    private HttpRequest buildDomainModelUploadRequest(String domainModelJson, String projectName, String apiKey, String diagramViewerBaseUrl) {
        return HttpRequest.newBuilder()
            .uri(URI.create(diagramViewerBaseUrl + DIAGRAM_VIEWER_DOMAIN_MODEL_UPLOAD_PATH + projectName))
            .header("Content-Type", "application/json")
            .header(API_KEY_HEADER_NAME, apiKey)
            .PUT(BodyPublishers.ofString(domainModelJson))
            .build();
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

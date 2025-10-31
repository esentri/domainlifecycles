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

package io.domainlifecycles.plugins.diagram.kroki;

import io.domainlifecycles.plugins.diagram.FileType;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

/**
 * The KrokiClient class serves as a client for interacting with a Kroki Docker container.
 * It facilitates the conversion of Nomnoml diagrams to a specified file format using the
 * Kroki server by managing the lifecycle of the Docker container and handling HTTP requests.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class KrokiClient {

    private static final Logger log = LoggerFactory.getLogger(KrokiDockerAdapter.class);

    private static final String KROKI_CONTAINER_URL = "http://localhost:8000";
    private static final String KROKI_NOMNOML_SVG_PATH = "/nomnoml/svg";

    private static final Integer MAX_RETRIES = 5;
    private static final Integer WAIT_TIMEOUT_MS = 500;

    private final KrokiDockerAdapter krokiDockerAdapter;

    /**
     * Constructs a new instance of {@code KrokiClient}.
     * This client is responsible for interacting with the Kroki server for rendering diagrams.
     * Internally, it initializes a {@link KrokiDockerAdapter} to manage the lifecycle of the Docker container
     * running the Kroki service.
     */
    public KrokiClient() {
        this.krokiDockerAdapter = new KrokiDockerAdapter();
    }

    /**
     * Initializes the KrokiClient by starting the associated Kroki Docker container.
     * This method must be invoked before performing any actions involving the Kroki server.
     * It ensures that the necessary Docker container is running and available for use.
     */
    public void initialize() { krokiDockerAdapter.start(); }

    /**
     * Has to be called after all Kroki actions have been performed, otherwise Docker-Container
     * keeps running.
     */
    public void finish() {
        krokiDockerAdapter.stop();
    }

    /**
     * Converts raw Nomnoml content into the specified file type using a Kroki server.
     * This method utilizes a Kroki Docker container for rendering diagrams.
     *
     * @param rawNomNomlContent the raw Nomnoml diagram content to be converted
     * @param fileType the desired file type for the converted output (e.g., SVG, PDF, PNG, etc.)
     * @return a byte array representing the converted diagram in the specified file type
     */
    public byte[] convertTo(final String rawNomNomlContent, final FileType fileType) {
        final String path = getKrokiPath(fileType);
        return convert(rawNomNomlContent, path);
    }

    private byte[] convert(String rawInputDiagramContent, String path) {
        log.info("Converting Nomnoml diagram to specified format via Kroki Docker container.");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(KROKI_CONTAINER_URL + path))
            .header("Accept", "text/plain")
            .POST(BodyPublishers.ofString(rawInputDiagramContent))
            .build();

        return sendWithRetries(request);
    }

    private byte[] sendWithRetries(final HttpRequest httpRequest) {

        for(int retryCounter = 0; retryCounter < MAX_RETRIES; retryCounter++) {
            try {
                log.debug(String.format("Sending HTTP request to Kroki Docker container. Retry: %s", retryCounter + 1));
                final HttpResponse<byte[]> response = HttpClient.newHttpClient().send(httpRequest, BodyHandlers.ofByteArray());

                if (response.statusCode() < 400) {
                    log.debug("HTTP request to Kroki Docker container has been successful.");
                    return response.body();
                }
                if (response.statusCode() >= 400) {
                    throw DLCPluginsException.fail(String.format("Kroki Docker container returned error for conversion: %s",
                        new String(response.body(), StandardCharsets.UTF_8)));
                }
            } catch (IOException | InterruptedException e) {
                try {
                    sleep(WAIT_TIMEOUT_MS);
                } catch (InterruptedException ignored) { }
            }
        }
        throw DLCPluginsException.fail(String.format("Kroki server couldn't be reached in specified retry limit (Retries: %s, Timeout: %s)", MAX_RETRIES, WAIT_TIMEOUT_MS));
    }

    private String getKrokiPath(final FileType fileType) {
        String krokiPath;
        switch (fileType) {
            case SVG -> krokiPath = KROKI_NOMNOML_SVG_PATH;
            default -> throw DLCPluginsException.fail(String.format("Filetype %s not allowed for Kroki conversion", fileType));
        }
        return krokiPath;
    }
}

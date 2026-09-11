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
 *  Copyright 2019-2026 the original author or authors.
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.staticanalysis.DomainClasspath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Exercises {@link DomainModelUploaderImpl} against a local {@link HttpServer} test double, using
 * the current test JVM's own classpath (which includes the compiled {@code tests.shared} domain
 * fixtures, via {@code test-shared-impl}) as the "project" classpath to build the domain model -
 * and, when enabled, the static analysis result - from.
 */
public class DomainModelUploaderImplTest {

    private static final List<String> DOMAIN_MODEL_PACKAGES = List.of("tests.shared");

    private static final List<URL> CLASS_PATH_FILES = currentTestClasspath();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServer server;

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void uploadsDomainMirrorAndDomainCallsWhenStaticAnalysisIsEnabled() throws Exception {
        AtomicReference<String> capturedPath = new AtomicReference<>();
        AtomicReference<String> capturedApiKeyHeader = new AtomicReference<>();
        AtomicReference<String> capturedContentEncodingHeader = new AtomicReference<>();
        AtomicReference<byte[]> capturedBody = new AtomicReference<>();
        startServer(exchange -> {
            capturedPath.set(exchange.getRequestURI().getPath());
            capturedApiKeyHeader.set(exchange.getRequestHeaders().getFirst("X-API-KEY"));
            capturedContentEncodingHeader.set(exchange.getRequestHeaders().getFirst("Content-Encoding"));
            capturedBody.set(exchange.getRequestBody().readAllBytes());
        });

        new DomainModelUploaderImpl().uploadDomainModel(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, true, "secret-api-key", "my-project", baseUrl());

        assertThat(capturedPath.get()).isEqualTo("/api/upload/domain-mirror/my-project");
        assertThat(capturedApiKeyHeader.get()).isEqualTo("secret-api-key");
        assertThat(capturedContentEncodingHeader.get()).isEqualTo("gzip");

        JsonNode body = objectMapper.readTree(gunzip(capturedBody.get()));
        assertThat(body.get("domainMirror").isObject()).isTrue();
        assertThat(body.get("domainCalls").isObject()).isTrue();
        assertThat(body.get("domainCalls").get("callsByCaller").isArray()).isTrue();
        assertThat(body.get("domainModelPackages").get(0).asText()).isEqualTo("tests.shared");
    }

    @Test
    void omitsDomainCallsWhenStaticAnalysisIsDisabled() throws Exception {
        AtomicReference<byte[]> capturedBody = new AtomicReference<>();
        startServer(exchange -> capturedBody.set(exchange.getRequestBody().readAllBytes()));

        new DomainModelUploaderImpl().uploadDomainModel(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, false, "secret-api-key", "my-project", baseUrl());

        JsonNode body = objectMapper.readTree(gunzip(capturedBody.get()));
        assertThat(body.get("domainMirror").isObject()).isTrue();
        assertThat(body.has("domainCalls")).isFalse();
    }

    @Test
    void wrapsAnErrorResponseFromTheViewerInADLCPluginsException() throws Exception {
        startServer(500, exchange -> exchange.getRequestBody().readAllBytes());

        assertThatThrownBy(() -> new DomainModelUploaderImpl().uploadDomainModel(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, false, "secret-api-key", "my-project", baseUrl()))
            .isInstanceOf(DLCPluginsException.class)
            .hasMessageContaining("500");
    }

    @Test
    void streamsDomainMirrorAndDomainCallsWhenStaticAnalysisIsEnabled() throws Exception {
        AtomicReference<String> capturedPath = new AtomicReference<>();
        AtomicReference<String> capturedApiKeyHeader = new AtomicReference<>();
        AtomicReference<String> capturedContentEncodingHeader = new AtomicReference<>();
        AtomicReference<String> capturedTransferEncodingHeader = new AtomicReference<>();
        AtomicReference<String> capturedContentLengthHeader = new AtomicReference<>();
        AtomicReference<byte[]> capturedBody = new AtomicReference<>();
        startServer(exchange -> {
            capturedPath.set(exchange.getRequestURI().getPath());
            capturedApiKeyHeader.set(exchange.getRequestHeaders().getFirst("X-API-KEY"));
            capturedContentEncodingHeader.set(exchange.getRequestHeaders().getFirst("Content-Encoding"));
            capturedTransferEncodingHeader.set(exchange.getRequestHeaders().getFirst("Transfer-Encoding"));
            capturedContentLengthHeader.set(exchange.getRequestHeaders().getFirst("Content-Length"));
            capturedBody.set(exchange.getRequestBody().readAllBytes());
        });

        new DomainModelUploaderImpl().uploadDomainModelStreaming(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, true, "secret-api-key", "my-project", baseUrl());

        assertThat(capturedPath.get()).isEqualTo("/api/upload/domain-mirror/my-project");
        assertThat(capturedApiKeyHeader.get()).isEqualTo("secret-api-key");
        assertThat(capturedContentEncodingHeader.get()).isEqualTo("gzip");
        // the compressed size is not known upfront, so this must be sent chunked rather than with a
        // fixed Content-Length
        assertThat(capturedTransferEncodingHeader.get()).isEqualTo("chunked");
        assertThat(capturedContentLengthHeader.get()).isNull();

        JsonNode body = objectMapper.readTree(gunzip(capturedBody.get()));
        assertThat(body.get("domainMirror").isObject()).isTrue();
        assertThat(body.get("domainCalls").isObject()).isTrue();
        assertThat(body.get("domainCalls").get("callsByCaller").isArray()).isTrue();
        assertThat(body.get("domainModelPackages").get(0).asText()).isEqualTo("tests.shared");
    }

    @Test
    void streamingOmitsDomainCallsWhenStaticAnalysisIsDisabled() throws Exception {
        AtomicReference<byte[]> capturedBody = new AtomicReference<>();
        startServer(exchange -> capturedBody.set(exchange.getRequestBody().readAllBytes()));

        new DomainModelUploaderImpl().uploadDomainModelStreaming(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, false, "secret-api-key", "my-project", baseUrl());

        JsonNode body = objectMapper.readTree(gunzip(capturedBody.get()));
        assertThat(body.get("domainMirror").isObject()).isTrue();
        assertThat(body.has("domainCalls")).isFalse();
    }

    @Test
    void streamingAndNonStreamingUploadsProduceEquivalentJson() throws Exception {
        AtomicReference<byte[]> nonStreamingBody = new AtomicReference<>();
        startServer(exchange -> nonStreamingBody.set(exchange.getRequestBody().readAllBytes()));
        new DomainModelUploaderImpl().uploadDomainModel(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, true, "secret-api-key", "my-project", baseUrl());
        server.stop(0);

        AtomicReference<byte[]> streamingBody = new AtomicReference<>();
        startServer(exchange -> streamingBody.set(exchange.getRequestBody().readAllBytes()));
        new DomainModelUploaderImpl().uploadDomainModelStreaming(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, true, "secret-api-key", "my-project", baseUrl());

        // both calls independently re-run the domain mirror initialization and the static analysis
        // from scratch, so the two JSON documents are not guaranteed to be byte-for-byte identical
        // (e.g. Map/Set iteration order is not guaranteed to be stable across independent runs) -
        // comparing full multi-megabyte strings char-by-char is also needlessly expensive, so this
        // compares structural shape instead, which is what streaming is actually meant to preserve
        JsonNode nonStreaming = objectMapper.readTree(gunzip(nonStreamingBody.get()));
        JsonNode streaming = objectMapper.readTree(gunzip(streamingBody.get()));

        assertThat(streaming.get("domainMirror").get("allTypeMirrors").size())
            .isEqualTo(nonStreaming.get("domainMirror").get("allTypeMirrors").size());
        assertThat(streaming.get("domainCalls").get("callsByCaller").size())
            .isEqualTo(nonStreaming.get("domainCalls").get("callsByCaller").size());
        assertThat(streaming.get("domainModelPackages")).isEqualTo(nonStreaming.get("domainModelPackages"));
    }

    @Test
    void streamingWrapsAnErrorResponseFromTheViewerInADLCPluginsException() throws Exception {
        startServer(500, exchange -> exchange.getRequestBody().readAllBytes());

        assertThatThrownBy(() -> new DomainModelUploaderImpl().uploadDomainModelStreaming(
            CLASS_PATH_FILES, DOMAIN_MODEL_PACKAGES, false, "secret-api-key", "my-project", baseUrl()))
            .isInstanceOf(DLCPluginsException.class)
            .hasMessageContaining("500");
    }

    private interface RequestHandler {
        void handle(com.sun.net.httpserver.HttpExchange exchange) throws java.io.IOException;
    }

    private void startServer(RequestHandler handler) throws java.io.IOException {
        startServer(200, handler);
    }

    private void startServer(int responseStatus, RequestHandler handler) throws java.io.IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/", exchange -> {
            try {
                handler.handle(exchange);
            } finally {
                byte[] response = new byte[0];
                exchange.sendResponseHeaders(responseStatus, response.length);
                exchange.getResponseBody().close();
            }
        });
        server.start();
    }

    private String baseUrl() {
        return "http://localhost:" + server.getAddress().getPort();
    }

    /**
     * A classpath scoped to just the {@code tests.shared} domain fixtures (and the DLC base types
     * they inherit from), derived via {@link DomainClasspath#ofMirroredTypes(DomainMirror)} rather
     * than from the full test JVM classpath - the latter drags every jar on the test classpath
     * (SootUp's own dependencies included) into the static analysis, which is needlessly expensive.
     */
    private static List<URL> currentTestClasspath() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        DomainMirror mirror = factory.initializeDomainMirror();

        return DomainClasspath.ofMirroredTypes(mirror).stream()
            .map(DomainModelUploaderImplTest::toUrl)
            .toList();
    }

    private static URL toUrl(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String gunzip(byte[] gzipped) throws java.io.IOException {
        try (GZIPInputStream gzipStream = new GZIPInputStream(new java.io.ByteArrayInputStream(gzipped))) {
            ByteArrayOutputStream decompressed = new ByteArrayOutputStream();
            gzipStream.transferTo(decompressed);
            return decompressed.toString(StandardCharsets.UTF_8);
        }
    }
}

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
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.serialize.DomainSerializer;
import io.domainlifecycles.mirror.serialize.jackson3.JacksonDomainSerializer;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.plugins.staticanalysis.DomainCallsAnalyzer;
import io.domainlifecycles.plugins.staticanalysis.DomainCallsAnalyzerImpl;
import io.domainlifecycles.plugins.util.DLCUtils;
import io.domainlifecycles.plugins.viewer.model.DomainMirrorUploadDto;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.serialize.DomainCallsSerializationException;
import io.domainlifecycles.staticanalysis.serialize.DomainCallsSerializer;
import io.domainlifecycles.staticanalysis.serialize.jackson3.JacksonDomainCallsSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPOutputStream;

/**
 * Implementation of the DomainModelUploader interface for uploading a serialized domain model,
 * and optionally the result of a static analysis of the domain classes, to a designated Diagram
 * Viewer system. This implementation handles the process of initializing and serializing the
 * domain model (and, if requested, running and serializing the static analysis), and preparing
 * and executing an HTTP PUT request to upload them, along with necessary metadata and
 * authorization headers.
 *
 * Responsibilities:
 * - Initializes the domain model from the given classpath and serializes it to JSON.
 * - Optionally runs a static analysis of the domain classes and serializes its result to JSON.
 * - Prepares an HTTP request to upload the domain model (and static analysis result) to the
 *   Diagram Viewer system.
 * - Ensures proper error handling and logging during the upload process.
 * - Formats the domain model data and additional configuration into a JSON request body.
 * - Configures required authentication headers for secure communication.
 *
 * For a domain of a few hundred types with the corresponding static analysis result, the request
 * body can already reach the tens of megabytes, so it is gzip-compressed before being sent
 * ({@code Content-Encoding: gzip}), and a connect and overall request timeout are applied so an
 * unreachable or slow Diagram Viewer fails the build instead of hanging it indefinitely.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 */
public class DomainModelUploaderImpl implements DomainModelUploader {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomainModelUploaderImpl.class);

    private static final String DIAGRAM_VIEWER_DOMAIN_MIRROR_UPLOAD_PATH = "/api/upload/domain-mirror/";
    private static final String API_KEY_HEADER_NAME = "X-API-KEY";

    /** How long to wait for the connection to the Diagram Viewer to be established. */
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

    /** How long to wait for the whole upload (request body, most importantly) plus response. */
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(5);

    /**
     * Number of not-yet-consumed chunks {@link #uploadDomainModelStreaming} buffers between the
     * writer thread producing the request body and the HTTP client consuming it, before the writer
     * blocks. Provides backpressure without an unbounded queue growing without limit if production
     * outpaces network I/O.
     */
    private static final int QUEUE_CAPACITY = 64;

    private final DomainSerializer domainSerializer = new JacksonDomainSerializer(true);
    private final DomainCallsSerializer domainCallsSerializer = new JacksonDomainCallsSerializer(true);
    private final DomainCallsAnalyzer domainCallsAnalyzer = new DomainCallsAnalyzerImpl();

    /**
     * A single, reused {@link HttpClient} rather than one created per upload call. This matters
     * beyond the (small) setup cost: the JDK's {@code HttpClient} is intended to be a long-lived,
     * reusable object, and on Java versions before {@code HttpClient} became {@link AutoCloseable}
     * (JDK 21), an {@code HttpClient} built without an explicit executor may leave its internal
     * worker threads running as non-daemon threads until it becomes unreachable and is garbage
     * collected. Creating a fresh client per call - especially several within the same process, as
     * happens across the various upload tasks/goals a single build may run - was observed to leave
     * enough such threads behind to prevent the build (or a test worker process) from exiting
     * promptly. Explicitly running the client on a daemon thread factory closes that gap regardless.
     */
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(CONNECT_TIMEOUT)
        .executor(Executors.newCachedThreadPool(DomainModelUploaderImpl::newDaemonThread))
        .build();

    private static Thread newDaemonThread(Runnable task) {
        final Thread thread = new Thread(task, "dlc-domain-model-upload-http-client");
        thread.setDaemon(true);
        return thread;
    }

    /**
     * Initializes and uploads a domain model - and, if requested, the result of a static analysis
     * of the domain classes - to a Diagram Viewer project using the provided configuration and
     * authentication details.
     *
     * This method initializes the domain model from the given classpath, optionally runs a static
     * analysis on top of it, and sends both, serialized to JSON, to a specified Diagram Viewer
     * instance, associating them with the target project. It handles request preparation, API key
     * authentication, error handling, and logs pertinent information during the process.
     *
     * @param classPathFiles         the classpath the domain model (and, if run, the static analysis)
     *                               is initialized from
     * @param domainModelPackages    a list of package names that define the domain model classes to be included
     * @param runStaticAnalysis      whether a static analysis of the domain classes should be run and its
     *                               result ({@code DomainCalls}) uploaded alongside the domain model
     * @param apiKey                 the API key required for authenticating with the Diagram Viewer platform
     * @param projectName            the name of the target project where the domain model will be uploaded
     * @param diagramViewerBaseUrl   the base URL of the Diagram Viewer instance to which the upload request is sent
     */
    @Override
    public void uploadDomainModel(List<URL> classPathFiles, List<String> domainModelPackages, boolean runStaticAnalysis,
                                  String apiKey, String projectName, String diagramViewerBaseUrl) {
        LOGGER.debug(String.format("Trying to upload Domain-Model to Diagram-Viewer project '%s' with base url '%s'.", projectName, diagramViewerBaseUrl));

        final DomainMirror domainMirror = buildDomainMirror(classPathFiles, domainModelPackages);
        final String domainMirrorJson = domainSerializer.serialize(domainMirror);

        final String domainCallsJson;
        if (runStaticAnalysis) {
            final DomainCalls domainCalls = domainCallsAnalyzer.analyze(classPathFiles, domainMirror);
            domainCallsJson = serializeDomainCalls(domainCalls);
        } else {
            domainCallsJson = null;
        }

        final HttpRequest request = buildDomainMirrorUploadRequest(
            domainMirrorJson, domainCallsJson, domainModelPackages, projectName, apiKey, diagramViewerBaseUrl);

        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            handleResponseErrors(response);
            LOGGER.info("Successfully uploaded Domain-Mirror to Diagram-Viewer.");
        } catch (IOException | InterruptedException e) {
            throw DLCPluginsException.fail("Could not send Domain-Mirror to Diagram-Viewer.", e);
        }
    }

    /**
     * Same as {@link #uploadDomainModel(List, List, boolean, String, String, String)}, but streams the
     * gzip-compressed request body directly into the HTTP request as it is produced, instead of first
     * assembling it completely in memory. See the interface javadoc for details and trade-offs.
     *
     * @param classPathFiles         the classpath the domain model (and, if run, the static analysis)
     *                               is initialized from
     * @param domainModelPackages    a list of package names that define the domain model classes to be included
     * @param runStaticAnalysis      whether a static analysis of the domain classes should be run and its
     *                               result ({@code DomainCalls}) uploaded alongside the domain model
     * @param apiKey                 the API key required for authenticating with the Diagram Viewer platform
     * @param projectName            the name of the target project where the domain model will be uploaded
     * @param diagramViewerBaseUrl   the base URL of the Diagram Viewer instance to which the upload request is sent
     */
    @Override
    public void uploadDomainModelStreaming(List<URL> classPathFiles, List<String> domainModelPackages, boolean runStaticAnalysis,
                                           String apiKey, String projectName, String diagramViewerBaseUrl) {
        LOGGER.debug(String.format("Trying to stream Domain-Model to Diagram-Viewer project '%s' with base url '%s'.", projectName, diagramViewerBaseUrl));

        final DomainMirror domainMirror = buildDomainMirror(classPathFiles, domainModelPackages);
        final DomainCalls domainCalls = runStaticAnalysis ? domainCallsAnalyzer.analyze(classPathFiles, domainMirror) : null;

        final AtomicReference<Exception> writerFailure = new AtomicReference<>();
        final HttpRequest request = buildStreamingDomainMirrorUploadRequest(
            domainMirror, domainCalls, domainModelPackages, projectName, apiKey, diagramViewerBaseUrl, writerFailure);

        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (writerFailure.get() != null) {
                throw DLCPluginsException.fail("Could not build the streaming upload request body.", writerFailure.get());
            }
            handleResponseErrors(response);
            LOGGER.info("Successfully uploaded Domain-Mirror to Diagram-Viewer (streamed).");
        } catch (IOException | InterruptedException e) {
            if (writerFailure.get() != null) {
                throw DLCPluginsException.fail("Could not send Domain-Mirror to Diagram-Viewer.", writerFailure.get());
            }
            throw DLCPluginsException.fail("Could not send Domain-Mirror to Diagram-Viewer.", e);
        }
    }

    private DomainMirror buildDomainMirror(List<URL> classPathFiles, List<String> domainModelPackages) {
        try {
            return DLCUtils.initializeDomainMirrorFromClassPath(classPathFiles, domainModelPackages.toArray(String[]::new));
        } catch (RuntimeException e) {
            throw DLCPluginsException.fail("DomainMirror couldn't be initialized.", e);
        }
    }

    private String serializeDomainCalls(DomainCalls domainCalls) {
        try {
            return domainCallsSerializer.serialize(domainCalls);
        } catch (DomainCallsSerializationException e) {
            throw DLCPluginsException.fail("Could not serialize the static analysis result (DomainCalls).", e);
        }
    }

    private HttpRequest buildDomainMirrorUploadRequest(String domainMirrorJson, String domainCallsJson, List<String> domainPackages, String projectName, String apiKey, String diagramViewerBaseUrl) {
        final String domainMirrorUploadDtoJsonString = generateJsonRequestBody(domainMirrorJson, domainCallsJson, domainPackages);
        final byte[] compressedBody = gzip(domainMirrorUploadDtoJsonString);

        return HttpRequest.newBuilder()
            .uri(URI.create(diagramViewerBaseUrl + DIAGRAM_VIEWER_DOMAIN_MIRROR_UPLOAD_PATH + projectName))
            .header("Content-Type", "application/json")
            .header("Content-Encoding", "gzip")
            .header(API_KEY_HEADER_NAME, apiKey)
            .timeout(REQUEST_TIMEOUT)
            .PUT(BodyPublishers.ofByteArray(compressedBody))
            .build();
    }

    private static String generateJsonRequestBody(String domainMirrorJson, String domainCallsJson, List<String> domainModelPackages) {
        DomainMirrorUploadDto domainMirrorUploadDto = new DomainMirrorUploadDto(domainMirrorJson, domainCallsJson, domainModelPackages);
        ObjectMapper o = new ObjectMapper();
        try {
            return o.writeValueAsString(domainMirrorUploadDto);
        } catch (JsonProcessingException e) {
            throw DLCPluginsException.fail("Could not serialize DomainMirrorUploadDTO to JSON.", e);
        }
    }

    private static byte[] gzip(String value) {
        try {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(value.getBytes(StandardCharsets.UTF_8));
            }
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw DLCPluginsException.fail("Could not gzip-compress the upload request body.", e);
        }
    }

    private HttpRequest buildStreamingDomainMirrorUploadRequest(DomainMirror domainMirror, DomainCalls domainCalls,
                                                                 List<String> domainModelPackages, String projectName,
                                                                 String apiKey, String diagramViewerBaseUrl,
                                                                 AtomicReference<Exception> writerFailure) {
        return HttpRequest.newBuilder()
            .uri(URI.create(diagramViewerBaseUrl + DIAGRAM_VIEWER_DOMAIN_MIRROR_UPLOAD_PATH + projectName))
            .header("Content-Type", "application/json")
            .header("Content-Encoding", "gzip")
            .header(API_KEY_HEADER_NAME, apiKey)
            .timeout(REQUEST_TIMEOUT)
            .PUT(BodyPublishers.ofInputStream(
                () -> queuedRequestBody(domainMirror, domainCalls, domainModelPackages, writerFailure)))
            .build();
    }

    /**
     * Opens a bounded producer/consumer queue and starts a background thread that writes the
     * gzip-compressed request body into it as it is produced, returning an {@link InputStream} over
     * that queue for the HTTP client to consume. Any failure while producing the body is recorded in
     * {@code writerFailure} rather than thrown here, since this runs on a different thread than the
     * caller of {@code uploadDomainModelStreaming}.
     * <p>
     * A {@link java.util.concurrent.BlockingQueue} of byte chunks is used here rather than the more
     * obvious {@link java.io.PipedOutputStream}/{@link java.io.PipedInputStream} pair: the latter are
     * old, narrowly synchronized JDK classes that are known to interact poorly with some consumers -
     * in particular, they were observed here to occasionally hang indefinitely together with
     * {@link HttpRequest.BodyPublishers#ofInputStream}, without throwing or timing out. A
     * {@code BlockingQueue} backed stream gives the same backpressure/handoff behaviour using
     * well-tested {@code java.util.concurrent} primitives instead.
     */
    private InputStream queuedRequestBody(DomainMirror domainMirror, DomainCalls domainCalls,
                                          List<String> domainModelPackages, AtomicReference<Exception> writerFailure) {
        final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        final Thread writer = new Thread(
            () -> writeCompressedRequestBody(queue, domainMirror, domainCalls, domainModelPackages, writerFailure),
            "dlc-domain-model-upload-writer");
        writer.setDaemon(true);
        writer.start();

        return new QueueInputStream(queue);
    }

    private void writeCompressedRequestBody(BlockingQueue<byte[]> queue, DomainMirror domainMirror, DomainCalls domainCalls,
                                            List<String> domainModelPackages, AtomicReference<Exception> writerFailure) {
        try (QueueOutputStream queueOut = new QueueOutputStream(queue);
             GZIPOutputStream gzipOut = new GZIPOutputStream(queueOut)) {
            writeRequestBody(gzipOut, domainMirror, domainCalls, domainModelPackages);
        } catch (Exception e) {
            writerFailure.set(e);
            LOGGER.debug("Failed to produce the streaming domain model upload body.", e);
            offerEndOfStreamQuietly(queue);
        }
    }

    private void writeRequestBody(OutputStream out, DomainMirror domainMirror, DomainCalls domainCalls,
                                  List<String> domainModelPackages) throws IOException {
        writeLiteral(out, "{\"domainMirror\":");
        domainSerializer.serialize(domainMirror, out);
        if (domainCalls != null) {
            writeLiteral(out, ",\"domainCalls\":");
            domainCallsSerializer.serialize(domainCalls, out);
        }
        writeLiteral(out, ",\"domainModelPackages\":");
        writeLiteral(out, domainModelPackagesJson(domainModelPackages));
        writeLiteral(out, "}");
    }

    private static void writeLiteral(OutputStream out, String literal) throws IOException {
        out.write(literal.getBytes(StandardCharsets.UTF_8));
    }

    private static String domainModelPackagesJson(List<String> domainModelPackages) {
        try {
            return new ObjectMapper().writeValueAsString(domainModelPackages);
        } catch (JsonProcessingException e) {
            throw DLCPluginsException.fail("Could not serialize domainModelPackages to JSON.", e);
        }
    }

    private static void offerEndOfStreamQuietly(BlockingQueue<byte[]> queue) {
        try {
            // put(), not offer(): if the queue happens to be full, this blocks until the consumer
            // (the HTTP client reading the request body) drains a slot, same as an ordinary chunk
            // would. If the consumer has itself already given up (e.g. the request timed out), this
            // daemon thread is left blocked here rather than hanging the caller of
            // uploadDomainModelStreaming, which has already returned (successfully or with an
            // exception) by that point.
            queue.put(QueueOutputStream.END_OF_STREAM);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Writes to a bounded {@link BlockingQueue} of byte chunks, one array per {@code write(...)} call,
     * signalling the end of the stream with a sentinel value on {@link #close()}. See
     * {@link #queuedRequestBody} for why this is used instead of a
     * {@link java.io.PipedOutputStream}/{@link java.io.PipedInputStream} pair.
     */
    private static final class QueueOutputStream extends OutputStream {

        static final byte[] END_OF_STREAM = new byte[0];

        private final BlockingQueue<byte[]> queue;
        private boolean closed = false;

        QueueOutputStream(BlockingQueue<byte[]> queue) {
            this.queue = queue;
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[]{(byte) b}, 0, 1);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (len == 0) {
                return;
            }
            offer(Arrays.copyOfRange(b, off, off + len));
        }

        @Override
        public void close() throws IOException {
            if (closed) {
                return;
            }
            closed = true;
            offer(END_OF_STREAM);
        }

        private void offer(byte[] chunk) throws IOException {
            try {
                queue.put(chunk);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while writing the streaming upload body.", e);
            }
        }
    }

    /**
     * Reads from a bounded {@link BlockingQueue} of byte chunks as written by {@link QueueOutputStream}.
     */
    private static final class QueueInputStream extends InputStream {

        private final BlockingQueue<byte[]> queue;
        private byte[] current = QueueOutputStream.END_OF_STREAM;
        private int pos = 0;
        private boolean eof = false;

        QueueInputStream(BlockingQueue<byte[]> queue) {
            this.queue = queue;
        }

        @Override
        public int read() throws IOException {
            final byte[] one = new byte[1];
            final int n = read(one, 0, 1);
            return n < 0 ? -1 : (one[0] & 0xFF);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (len == 0) {
                return 0;
            }
            if (eof) {
                return -1;
            }
            while (pos >= current.length) {
                try {
                    current = queue.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while reading the streaming upload body.", e);
                }
                pos = 0;
                if (current == QueueOutputStream.END_OF_STREAM) {
                    eof = true;
                    return -1;
                }
            }
            final int n = Math.min(len, current.length - pos);
            System.arraycopy(current, pos, b, off, n);
            pos += n;
            return n;
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

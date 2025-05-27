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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

/**
 * KrokiDockerAdapter is responsible for managing the lifecycle of a Docker container
 * running the Kroki service. It interacts with the Docker client to initialize,
 * start, stop, and create Kroki containers as needed.
 */
class KrokiDockerAdapter {

    private final static String KROKI_CONTAINER_NAME = "kroki-dlc";
    private final static String KROKI_CONTAINER_HOST_NAME = "kroki-dlc-host";
    private final static String KROKI_CONTAINER_IMAGE_NAME = "yuzutech/kroki";
    private final static Logger log = LoggerFactory.getLogger(KrokiDockerAdapter.class);

    private final DockerClient dockerClient;
    private String krokiContainerId;

    KrokiDockerAdapter() {
        dockerClient = initializeDockerClient();
    }

    void start() {
        if(krokiContainerId == null) {
            krokiContainerId = createOrGetKrokiDockerContainerId();
        }
        startContainer();
    }

    void stop() {
        if(krokiContainerId != null) {
            try {
                dockerClient.stopContainerCmd(krokiContainerId).exec();
            } catch (Throwable t) {
                throw DLCPluginsException.fail("Could not stop Kroki Docker container.", t);
            }
            log.debug("Kroki container stopped");
        }
    }

    private void startContainer() {
        try {
            var res = dockerClient.listContainersCmd()
                .withShowAll(true)
                .withIdFilter(List.of(krokiContainerId))
                .exec();
            if(!res.isEmpty()){
                log.debug("Kroki container state:" + res.stream().map(Container::getState).collect(Collectors.joining()));
                var running = res.stream().allMatch(c -> c.getState().equalsIgnoreCase("running"));
                if(!running){
                    dockerClient.startContainerCmd(krokiContainerId).exec();
                }
            }else {
                throw DLCPluginsException.fail(
                    "Kroki container not found");
            }
        } catch(Throwable t) {
            throw DLCPluginsException.fail(
                "Could not start or access Kroki Docker container.", t);
        }
        log.debug("Kroki container running");
    }

    private String createOrGetKrokiDockerContainerId() {
        try {
            final List<Container> foundContainers = dockerClient
                .listContainersCmd()
                .withShowAll(true)
                .withNameFilter(List.of(KROKI_CONTAINER_NAME))
                .exec();

            if(!foundContainers.isEmpty()) {
                log.debug("Found containers: {}", foundContainers.stream().map(c -> c.getId() + " " + c.getNames() ).collect(Collectors.joining("\n")));
                String krokiContainerId = foundContainers.stream().findFirst().get().getId();
                log.debug(String.format("Found existing Kroki Docker container with ID: %s. Reusing this.", krokiContainerId));
                return krokiContainerId;

            }
            log.debug("No existing Kroki Docker container found. Creating new one...");
        } catch(Throwable t) {
            throw DLCPluginsException.fail(
                "Could not create Kroki Docker container. Please check whether your Docker engine is up and running.", t);
        }
        return createKrokiDockerContainer();
    }

    private String createKrokiDockerContainer() {
        final ExposedPort tcp4444 = ExposedPort.tcp(8000);
        final Ports portBindings = new Ports();
        portBindings.bind(tcp4444, Ports.Binding.bindPort(8000));

        try {
            return dockerClient.createContainerCmd(KROKI_CONTAINER_IMAGE_NAME)
                .withName(KROKI_CONTAINER_NAME)
                .withHostName(KROKI_CONTAINER_HOST_NAME)
                .withExposedPorts(tcp4444)
                .withHostConfig(newHostConfig().withPortBindings(portBindings))
                .exec()
                .getId();
        } catch(Throwable t) {
            throw DLCPluginsException.fail(
                "Could not create Kroki Docker container. Please check whether your Docker engine is up and running.", t);
        }
    }

    private DockerClient initializeDockerClient() {
        try {
            final DockerClientConfig standard = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

            final DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(standard.getDockerHost())
                .sslConfig(standard.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

            return DockerClientImpl.getInstance(standard, httpClient);
        } catch(Throwable t) {
            throw DLCPluginsException.fail(
                "Could not establish connection to Docker client. Please check whether your Docker engine is up and running.", t);
        }
    }
}

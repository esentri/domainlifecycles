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

package io.domainlifecycles.plugin.viewer;

import io.domainlifecycles.plugins.json.JsonSerializer;
import io.domainlifecycles.plugins.json.JsonSerializerImpl;
import io.domainlifecycles.plugins.viewer.DomainModelUploader;
import io.domainlifecycles.plugins.viewer.DomainModelUploaderImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maven goal for uploading the domain model JSON to a remote Diagram Viewer application.
 * This Mojo is configured to execute during the "initialize" phase of the Maven lifecycle
 * and requires compile-level dependency resolution.
 *
 * The goal leverages a {@link DomainModelUploader} implementation to send the serialized
 * domain model JSON along with other metadata to a specified Diagram Viewer endpoint.
 * It uses a {@link JsonSerializer} implementation to convert the domain model into
 * a JSON string before uploading.
 *
 * Key Responsibilities:
 * - Collect classpath files and context packages from the Maven project.
 * - Serialize the domain model into a JSON format using {@link JsonSerializer}.
 * - Upload the serialized domain model to the Diagram Viewer using {@link DomainModelUploader}.
 *
 * Configuration Parameters:
 * - diagramViewerBaseUrl: Base URL of the Diagram Viewer application.
 * - apiKey: API key for authentication with the Diagram Viewer application.
 * - projectName: Name of the project to associate with the domain model upload.
 * - contextPackages: List of package names containing domain model classes.
 *
 * @author Leon Völlinger
 */
@Mojo(name = "uploadDomainModel", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.INITIALIZE)
public class UploadDomainModelGoal extends AbstractMojo {

    private final static Logger LOGGER = LoggerFactory.getLogger(UploadDomainModelGoal.class);

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "diagramViewerBaseUrl", required = true)
    private String diagramViewerBaseUrl;

    @Parameter(property = "apiKey", required = true)
    private String apiKey;

    @Parameter(property = "projectName", required = true)
    private String projectName;

    @Parameter(property = "contextPackages", required = true)
    private List<String> contextPackages;

    private DomainModelUploader domainModelUploader;

    /**
     * Executes the Maven goal "uploadDomainModel".
     *
     * This method serves as the entry point for running the goal to upload the serialized
     * domain model to the configured Diagram Viewer application. It first logs the intention
     * to run the goal, initializes a specific implementation of the {@link DomainModelUploader},
     * and then calls the internal {@code uploadDomainModel} method to perform the upload operation.
     *
     * Responsibilities:
     * - Logs the start of the goal execution process.
     * - Initializes the {@link DomainModelUploaderImpl} used for handling the domain model upload.
     * - Delegates the upload logic to the {@code uploadDomainModel} method.
     */
    @Override
    public void execute() {
        LOGGER.info("Running Upload Domain Model Goal...");
        domainModelUploader = new DomainModelUploaderImpl();
        uploadDomainModel();
    }

    private void uploadDomainModel() {
        JsonSerializer jsonSerializer = new JsonSerializerImpl(true);
        final String domainModelJson = jsonSerializer.serialize(ClassLoaderUtils.getParentClasspathFiles(project), contextPackages);
        domainModelUploader.uploadDomainModel(domainModelJson, contextPackages, apiKey, projectName, diagramViewerBaseUrl);
    }
}

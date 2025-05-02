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

package io.domainlifecycles.plugin.viewer;


import io.domainlifecycles.plugins.json.JsonSerializer;
import io.domainlifecycles.plugins.json.JsonSerializerImpl;
import io.domainlifecycles.plugins.viewer.DomainModelUploader;
import io.domainlifecycles.plugins.viewer.DomainModelUploaderImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract task for creating UML-like diagrams based on domain models or other configurations.
 *
 * This class provides functionality to generate diagrams using a customizable configuration and saves
 * the resulting output to a specified directory. It interacts with instances of `DiagramGenerator`
 * to produce the diagrams and relies on project configurations for classpath and diagram details.
 *
 * Key Features:
 * - Receives diagram configurations via `getDiagrams`, which contains context packages and customizable styles.
 * - Output directory for generated diagrams is defined through `getFileOutputDir`.
 * - Utilizes a `DiagramGenerator` implementation to transform configurations into diagram files.
 * - Diagrams are stored with custom file names and formats based on the provided configuration.
 *
 * This task is designed to be extended or utilized in a build automation environment like Gradle.
 *
 * Methods:
 * - `getFileOutputDir()`: Specifies the directory where the generated diagrams will be saved.
 * - `getDiagrams()`: Provides the container of diagram configurations, which define the diagram content and visual styles.
 * - `action()`: Main task action that initializes the diagram generator, processes configurations, and generates/saves diagrams.
 *
 * @author Leon Völlinger
 */
public abstract class UploadDomainModelTask extends DefaultTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(UploadDomainModelTask.class);

    @Input
    public abstract Property<String> getDiagramViewerBaseUrl();

    @Input
    public abstract Property<String> getApiKey();

    @Input
    public abstract Property<String> getProjectName();

    @Input
    public abstract ListProperty<String> getContextPackages();

    private DomainModelUploader domainModelUploader;

    @TaskAction
    public void action() {
        LOGGER.info("Running Upload Domain Model Goal...");
        domainModelUploader = new DomainModelUploaderImpl();
        uploadDomainModel();
    }

    private void uploadDomainModel() {
        JsonSerializer jsonSerializer = new JsonSerializerImpl(true);
        final String domainModelJson = jsonSerializer.serialize(ClassLoaderUtils.getParentClasspathFiles(getProject()),
            getContextPackages().get());

        domainModelUploader.uploadDomainModel(
            domainModelJson, getApiKey().get(), getProjectName().get(), getDiagramViewerBaseUrl().get());
    }
}

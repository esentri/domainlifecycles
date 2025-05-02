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

package io.domainlifecycles.plugin.json;

import io.domainlifecycles.plugin.diagram.CreateDiagramGoal;
import io.domainlifecycles.plugins.json.JsonSerializer;
import io.domainlifecycles.plugins.json.JsonSerializerImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import io.domainlifecycles.utils.FileIOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Mojo implementation for rendering and saving domain models as JSON files based on Maven project configurations.
 * This goal is executed during the Maven build process and allows serialization of context packages into JSON format,
 * which is then saved to the specified output directory.
 *
 * Annotations:
 * - `@Mojo` specifies the Maven goal name ("renderJson"), the required dependency resolution scope, and the default
 *   Maven lifecycle phase it is bound to (INITIALIZE).
 *
 * Key responsibilities:
 * - Reading and processing Maven project-specific configurations needed for JSON serialization.
 * - Iterating over the provided serialization configurations to generate JSON representations of domain models.
 * - Utilizing a JSON serializer to process and produce the JSON content.
 * - Saving the generated JSON files to the defined output directory.
 *
 * Configuration parameters:
 * - `project`: The Maven project associated with the execution of this plugin goal.
 * - `fileOutputDir`: Path to the directory where the generated JSON files will be saved.
 * - `serializations`: List of configurations defining the context packages and file name for each JSON output.
 *
 * Logging:
 * - Logs details during the serialization and saving process of each JSON model.
 *
 * Dependencies:
 * - Utilizes `JsonSerializerImpl` for generating the JSON content.
 * - Makes use of helper utilities such as `DLCUtils`, `ClassLoaderUtils`, and `FileIOUtils` for domain model initialization
 *   and file operations.
 *
 * Note:
 * - Proper configuration of the `serializations` parameter and the `fileOutputDir` property is essential for successful execution.
 * - This class is dependent on the `PluginSerializationConfiguration` and a compatible `JsonSerializer` implementation.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
@Mojo(name = "renderJson", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.INITIALIZE)
public class JsonRenderGoal extends AbstractMojo {

    private final static Logger log = LoggerFactory.getLogger(CreateDiagramGoal.class);

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "fileOutputDir", required = true)
    private String fileOutputDir;

    @Parameter(property = "serializations", required = true)
    private List<PluginSerializationConfiguration> serializations;

    private JsonSerializer jsonSerializer;

    /**
     * Executes the goal logic to render models and save them as JSON files.
     *
     * This method initializes a {@link JsonSerializerImpl} instance configured for pretty print JSON output.
     * It iterates through the collection of serialization configurations and processes each one by invoking
     * the {@code renderAndSaveModelAsJson} method for rendering the model and saving it to the output directory.
     *
     * The execution leverages the following:
     * - Instantiation of the {@link JsonSerializerImpl} for JSON serialization.
     * - Handling of each {@link PluginSerializationConfiguration} to produce corresponding JSON outputs.
     *
     * The JSON files are saved in the directory specified by the `fileOutputDir` field, with filenames
     * determined by each configuration's `fileName` property.
     *
     * This method is a required implementation of the {@code execute()} method inherited from {@link AbstractMojo}.
     */
    @Override
    public void execute() {
        log.info("Running Json Render Goal");
        jsonSerializer = new JsonSerializerImpl(true);
        serializations.forEach(this::renderAndSaveModelAsJson);
    }

    private void renderAndSaveModelAsJson(final PluginSerializationConfiguration pluginSerializationConfiguration) {
        final String jsonContent = jsonSerializer.serialize(ClassLoaderUtils.getParentClasspathFiles(project), pluginSerializationConfiguration.getDomainModelPackages());

        final Path filePath = Path.of(fileOutputDir, pluginSerializationConfiguration.getFileName() + ".json");

        log.info(String.format("Saving JSON model to %s/%s.json", fileOutputDir, pluginSerializationConfiguration.getFileName()));
        FileIOUtils.writeFileTo(filePath, jsonContent);
    }
}

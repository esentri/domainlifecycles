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

package io.domainlifecycles.plugin.mirror;

import io.domainlifecycles.plugins.mirror.MirrorSerializer;
import io.domainlifecycles.plugins.mirror.MirrorSerializerImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import io.domainlifecycles.utils.FileIOUtils;
import java.nio.file.Path;
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
@Mojo(name = "serializeMirror", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.INITIALIZE)
public class MirrorSerializerGoal extends AbstractMojo {

    private final static Logger LOGGER = LoggerFactory.getLogger(MirrorSerializerGoal.class);

    private static final String DEFAULT_JSON_RENDER_FILE_NAME = "mirror.json";
    private static final String META_INF_DLC_MIRROR_FILE_PATH = "src/main/resources/META-INF/dlc/" + DEFAULT_JSON_RENDER_FILE_NAME;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "fileOutputDir")
    private String fileOutputDir;

    @Parameter(property = "serializations")
    private List<PluginSerializationConfiguration> serializations;

    private MirrorSerializer mirrorSerializer;

    /**
     * Executes the goal logic to render models and save them as JSON files.
     *
     * This method initializes a {@link MirrorSerializerImpl} instance configured for pretty print JSON output.
     * It iterates through the collection of serialization configurations and processes each one by invoking
     * the {@code renderAndSaveModelAsJson} method for rendering the model and saving it to the output directory.
     *
     * The execution leverages the following:
     * - Instantiation of the {@link MirrorSerializerImpl} for JSON serialization.
     * - Handling of each {@link PluginSerializationConfiguration} to produce corresponding JSON outputs.
     *
     * The JSON files are saved in the directory specified by the `fileOutputDir` field, with filenames
     * determined by each configuration's `fileName` property.
     *
     * This method is a required implementation of the {@code execute()} method inherited from {@link AbstractMojo}.
     */
    @Override
    public void execute() {
        LOGGER.info("Running Serialize Mirror Goal...");
        mirrorSerializer = new MirrorSerializerImpl(true);
        serializations.forEach(this::renderAndSaveModelAsJson);
    }

    private void renderAndSaveModelAsJson(final PluginSerializationConfiguration pluginSerializationConfiguration) {
        String jsonContent = mirrorSerializer.serialize(ClassLoaderUtils.getParentClasspathFiles(project), pluginSerializationConfiguration.getDomainModelPackages());
        Path filePath;

        if(noFilePathSpecified()) {
            filePath = Path.of(META_INF_DLC_MIRROR_FILE_PATH);
        } else {
            filePath = Path.of(fileOutputDir, !pluginSerializationConfiguration.getFileName().isBlank() ? pluginSerializationConfiguration.getFileName() + ".json" : DEFAULT_JSON_RENDER_FILE_NAME);
        }

        LOGGER.info("Saving JSON model to {}", filePath);
        FileIOUtils.writeFileTo(filePath, jsonContent);
    }

    private boolean noFilePathSpecified() {
        return fileOutputDir == null || fileOutputDir.isBlank();
    }

}

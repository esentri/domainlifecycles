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

package io.domainlifecycles.plugin.diagram;


import io.domainlifecycles.plugin.extensions.PluginDiagramConfigurationExtension;
import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.DiagramGenerator;
import io.domainlifecycles.plugins.diagram.DiagramGeneratorImpl;
import io.domainlifecycles.plugins.util.FileIOUtils;
import io.domainlifecycles.utils.ClassLoaderUtils;
import java.nio.file.Path;
import org.gradle.api.DefaultTask;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

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
public abstract class CreateDiagramTask extends DefaultTask {

    private final static Logger log = LoggerFactory.getLogger(CreateDiagramTask.class);

    /**
     * Returns the directory where generated diagram files will be stored.
     *
     * The output directory is defined as a Gradle `DirectoryProperty` and is used to specify
     * the location for saving the generated UML-like diagrams. This is typically configured
     * during task setup to ensure the files are saved in the desired location.
     *
     * @return a `DirectoryProperty` representing the output directory for the generated diagrams
     */
    @OutputDirectory
    public abstract DirectoryProperty getFileOutputDir();

    /**
     * Retrieves a container of diagram configuration extensions.
     *
     * This method provides access to a user-configurable container of
     * {@link PluginDiagramConfigurationExtension} instances. Each extension
     * represents the configuration for generating a specific diagram, such as
     * format, file name, and various style options. The extensions can be
     * defined and customized to control the appearance and content of
     * generated diagrams.
     *
     * @return a {@link NamedDomainObjectContainer} containing
     *         {@link PluginDiagramConfigurationExtension} elements,
     *         which represent the configurable settings for generating diagrams.
     */
    @Input
    public abstract NamedDomainObjectContainer<PluginDiagramConfigurationExtension> getDiagrams();

    private DiagramGenerator diagramGenerator;

    /**
     * Executes the task action for generating and saving diagrams.
     *
     * This method performs the following steps:
     * 1. Initializes an instance of {@link DiagramGeneratorImpl} to handle diagram generation.
     * 2. Initializes the domain model using class path files retrieved from the parent class loader
     * 3. Iterates over all configured diagrams, retrieved from {@link CreateDiagramTask#getDiagrams()},
     *    and creates and saves each diagram using the {@code createAndSaveDiagram} method.
     *
     * The method is invoked automatically during the execution phase of the Gradle task
     * and ensures all diagrams are generated and stored as per the provided configurations.
     *
     * This is a core part of the functionality for the `CreateDiagramTask` and makes use
     * of auxiliary utilities and components for diagram creation and storage.
     */
    @TaskAction
    public void action() {
        diagramGenerator = new DiagramGeneratorImpl();
        getDiagrams().forEach(this::createAndSaveDiagram);
        diagramGenerator.tearDown();
    }

    private void createAndSaveDiagram(PluginDiagramConfigurationExtension diagramConfigExtension) {
        final DiagramConfig diagramConfig = DiagramConfigMapper.map(diagramConfigExtension);
        byte[] diagramFileContent = diagramGenerator.generateDiagram(
            ClassLoaderUtils.getParentClasspathFiles(getProject()),
            diagramConfig,
            diagramConfigExtension.getDomainModelPackages().getOrElse(Collections.emptyList()).toArray(String[]::new)
        );

        final Path filePath = Path.of(getFileOutputDir().get().toString(),
            diagramConfig.getFileName() + diagramConfig.getFileType().getFileSuffix());

        log.info("Saving diagram to {}", filePath);
        FileIOUtils.writeFileTo(filePath, diagramFileContent);
    }
}

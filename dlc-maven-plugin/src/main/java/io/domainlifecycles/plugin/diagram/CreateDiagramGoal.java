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


import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.DiagramGenerator;
import io.domainlifecycles.plugins.diagram.DiagramGeneratorImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import io.domainlifecycles.plugins.util.FileIOUtils;
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
 * Mojo implementation for generating diagrams based on Maven project configurations.
 * This goal is executed during the Maven build process and allows creating and saving
 * specified diagrams to a desired output directory.
 *
 * This class integrates with the Maven build lifecycle and uses external configuration
 * to generate diagrams in the specified formats and locations.
 *
 * Annotations:
 * - `@Mojo` specifies the Maven Goal name ("createDiagram"), the required dependency
 *   resolution scope, and the default Maven lifecycle phase it is bound to (INITIALIZE).
 *
 * Key responsibilities:
 * - Reading and processing Maven project configurations required for diagram generation.
 * - Instantiating a `DiagramGenerator` implementation to create diagram content.
 * - Iterating over the provided diagram configurations, generating corresponding diagrams,
 *   and saving them to the output directory in the specified formats.
 *
 * Configuration parameters:
 * - `project`: The Maven project associated with the execution of this plugin goal.
 * - `fileOutputDir`: Path to the directory where the generated diagrams will be saved.
 * - `diagrams`: List of configurations defining the diagrams that need to be created.
 *
 * Logging:
 * - Logs details during the generation and saving process of each diagram.
 *
 * Dependencies:
 * - Utilizes `DiagramGeneratorImpl` for creating diagrams.
 * - Makes use of helper utilities such as `DLCUtils`, `ClassLoaderUtils`, and `FileIOUtils`.
 *
 * Note: Proper configuration of the `diagrams` parameter and the `fileOutputDir` property
 * is necessary for successful execution.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 */
@Mojo(name = "createDiagram", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.INITIALIZE)
public class CreateDiagramGoal extends AbstractMojo {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateDiagramGoal.class);

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "fileOutputDir", required = true)
    private String fileOutputDir;

    @Parameter(property = "diagrams", required = true)
    private List<PluginDiagramConfiguration> diagrams;

    private DiagramGenerator diagramGenerator;

    /**
     * Executes the primary task of the goal by generating and saving diagrams based on the provided configurations.
     *
     * This method initializes a {@link DiagramGeneratorImpl} instance to handle the diagram generation process.
     * It iterates over a collection of diagram configurations and processes each one through the {@code createAndSaveDiagram} method.
     *
     * The generated diagrams are saved to the specified output directory as defined by the configuration settings.
     *
     * This method is a required implementation of the {@code execute()} method inherited from {@link AbstractMojo}.
     */
    @Override
    public void execute() {
        LOGGER.info("Running Create Diagram Goal...");
        diagramGenerator = new DiagramGeneratorImpl();
        diagrams.forEach(this::createAndSaveDiagram);
        diagramGenerator.tearDown();
    }

    private void createAndSaveDiagram(PluginDiagramConfiguration mavenDiagramConfiguration) {
        DiagramConfig diagramConfig = DiagramConfigMapper.map(mavenDiagramConfiguration);
        byte[] diagramFileContent = diagramGenerator.generateDiagram(
            ClassLoaderUtils.getParentClasspathFiles(project),
            diagramConfig,
            mavenDiagramConfiguration.getDomainModelPackages().toArray(String[]::new)
        );

        final Path filePath = Path.of(fileOutputDir,
            diagramConfig.getFileName() + diagramConfig.getFileType().getFileSuffix());

        LOGGER.info("Saving diagram to {}/{}.{}", fileOutputDir, mavenDiagramConfiguration.getFileName(),
            mavenDiagramConfiguration.getFormat());
        FileIOUtils.writeFileTo(filePath, diagramFileContent);
    }
}

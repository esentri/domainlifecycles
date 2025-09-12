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

package io.domainlifecycles.plugins.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.plugins.diagram.kroki.KrokiClient;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.plugins.util.DLCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implementation class for generating visual domain diagrams based on class path inputs and configuration details.
 * This class provides mechanisms to generate diagrams in multiple file formats, leveraging the Nomnoml syntax
 * and optionally interacting with an external service for additional file format conversions.
 *
 * This class utilizes the following components:
 * - {@link KrokiClient}: For converting Nomnoml diagrams into specific file formats.
 * - {@link DomainDiagramGenerator}: For creating raw Nomnoml text representations of the domain diagram.
 * - {@link DLCUtils}: For initializing the domain model from provided class path files.
 *
 * The diagram generation process involves:
 * 1. Parsing the context-specific class path files into a domain model using {@link DLCUtils}.
 * 2. Generating a raw Nomnoml textual representation of the diagram from the domain model.
 * 3. Depending on the file type, either returning the raw Nomnoml text or converting it to the desired format using {@link KrokiClient}.
 *
 * Key functionalities include:
 * - Handling exceptions during domain model initialization and file format conversions.
 * - Managing the lifecycle of a Kroki service when needed for diagram conversions.
 *
 * @author Leon Völlinger
 */
public class DiagramGeneratorImpl implements DiagramGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(DiagramGeneratorImpl.class);

    private final KrokiClient krokiClient;

    /**
     * Constructs a new instance of {@code DiagramGeneratorImpl}.
     *
     * This constructor initializes the {@code krokiClient} field with a new instance of {@link KrokiClient}.
     * The {@link KrokiClient} is used for interacting with a Kroki Docker container to render diagrams.
     */
    public DiagramGeneratorImpl() {
        this.krokiClient = new KrokiClient();
    }

    /**
     * Generates a diagram based on the provided configuration and classpath files.
     *
     * @param classPathFiles a list of URLs pointing to the classpath resources used for generating the diagram
     * @param diagramConfig the configuration settings for the diagram, including file name and file type
     * @param domainPackages the packages containing all relevant domain classes
     * @return a byte array representing the generated diagram in the specified format
     * @throws DLCPluginsException if an error occurs during the diagram generation process
     */
    @Override
    public byte[] generateDiagram(List<URL> classPathFiles, final DiagramConfig diagramConfig, final String... domainPackages) {
        LOGGER.info(String.format("Generating diagram %s of type %s", diagramConfig.getFileName(), diagramConfig.getFileType().name()));
        final String rawNomnomlDiagramText = generateRawNomnomlDiagramText(classPathFiles, diagramConfig, domainPackages);

        if(FileType.NOMNOML.equals(diagramConfig.getFileType())) {
            return rawNomnomlDiagramText.getBytes(StandardCharsets.UTF_8);
        }

        try {
            krokiClient.initialize();
            return krokiClient.convertTo(rawNomnomlDiagramText, diagramConfig.getFileType());
        } catch (Exception e) {
            throw DLCPluginsException.fail(
                String.format("Error occurred while generating diagram '%s' (Of type: %s)",
                    diagramConfig.getFileName(), diagramConfig.getFileType().name()), e);
        }
    }

    /**
     * Cleans up resources used by the {@code DiagramGeneratorImpl} instance.
     *
     * This method ensures that the {@code krokiClient} completes its operations
     * and stops any associated Docker containers to prevent resource leakage.
     * It is essential to invoke this method after all diagram generation tasks
     * are complete.
     */
    @Override
    public void tearDown() {
        krokiClient.finish();
    }


    private String generateRawNomnomlDiagramText(List<URL> classPathFiles, final DiagramConfig diagramConfig, final String... domainPackages) {
        DomainMirror dm = null;
        try {
            dm = DLCUtils.initializeDomainMirrorFromClassPath(classPathFiles, domainPackages);
        } catch(RuntimeException e) {
            throw DLCPluginsException.fail("DomainMirror couldn't be initialized.", e);
        }

        final DomainDiagramGenerator generator = new DomainDiagramGenerator(diagramConfig.map(), dm);
        return generator.generateDiagramText();
    }
}

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

import java.net.URL;
import java.util.List;

/**
 * Interface for generating visual diagrams based on a provided class path and configuration details.
 * This interface defines a contract for transforming domain models or related input into visual representations
 * such as diagrams in various formats.
 *
 * @author Leon Völlinger
 */
public interface DiagramGenerator {

    /**
     * Generates a visual diagram based on the provided class path files, configuration, and specified domain packages.
     *
     * @param classPathFiles the list of class path files to be analyzed for diagram generation
     * @param diagramConfig the configuration settings for the diagram, including formatting and display options
     * @param domainModelPackages parameter that specifies the domain model packages to include in the diagram
     * @return a byte array representing the generated diagram
     */
    byte[] generateDiagram(List<URL> classPathFiles, DiagramConfig diagramConfig, String... domainModelPackages);

    /**
     * Performs necessary cleanup and resource deallocation associated with the diagram generation process.
     * This method is intended to be called when the implementation of the DiagramGenerator
     * is no longer needed, ensuring proper release of resources like memory, file handles, or other
     * system resources used during the diagram creation lifecycle.
     */
    void tearDown();
}

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

package io.domainlifecycles.plugin.extensions;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.DirectoryProperty;

/**
 * Represents the configuration for JSON model generation tasks in a Gradle plugin.
 *
 * This class provides the necessary properties and methods to configure the output
 * directory and serialization settings for JSON model generation tasks. It allows
 * users to specify where the JSON files should be generated and define configurations
 * for various serializations.
 *
 * @author Leon Völlinger
 */
public abstract class MirrorSerializerTaskConfigurationExtension {

    /**
     * Retrieves the directory property specifying the output location for generated JSON files.
     *
     * This method provides a customizable directory property that defines where the JSON
     * output files will be written.
     *
     * @return a {@code DirectoryProperty} representing the output directory for JSON files.
     */
    public abstract DirectoryProperty getFileOutputDir();

    /**
     * Retrieves the container for configuring serialization settings for JSON model generation.
     *
     * This method provides access to a container where multiple serialization configurations
     * can be defined, each specifying properties like context packages and file names for
     * individual serialization configurations.
     *
     * @return a {@code NamedDomainObjectContainer} of {@code SerializationConfigurationExtension}
     *         instances, representing the serialization configurations.
     */
    public abstract NamedDomainObjectContainer<SerializationConfigurationExtension> getSerializations();
}

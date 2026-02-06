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

import org.gradle.api.Named;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Represents an abstract extension for configuring serialization settings in a Gradle plugin.
 *
 * This class provides methods for specifying properties related to the serialization
 * configuration, such as defining the context packages involved in serialization and
 * the file name for the output.
 *
 * @author Leon Völlinger
 */
public abstract class SerializationConfigurationExtension implements Named {

    private static final String DEFAULT_MIRROR_JSON_FILE_NAME = "mirror.json";

    /**
     * Constructs a new instance of the {@code SerializationConfigurationExtension}.
     *
     * This constructor initializes the default configuration for serialization,
     * specifically setting a default file name for the output file used during
     * serialization-related tasks. The default file name is {@code mirror.json}, which
     * acts as the convention unless overridden. The {@code Project} parameter allows
     * access to the Gradle project in which this extension is being used.
     *
     * @param project the Gradle {@code Project} instance associated with this serialization configuration.
     */
    public SerializationConfigurationExtension(Project project) {
        getFileName().convention(DEFAULT_MIRROR_JSON_FILE_NAME);
    }

    /**
     * Retrieves the list of domain model packages for serialization configuration.
     *
     * This method provides access to a list property where packages containing the domain model
     * can be defined.
     *
     * @return a {@code ListProperty} of {@code String} representing the domain model packages involved in serialization.
     */
    public abstract ListProperty<String> getDomainModelPackages();

    /**
     * Retrieves the file name associated with the serialization configuration.
     *
     * This method provides access to a property where the output file name for
     * a specific serialization configuration can be defined. The file name is
     * used in tasks related to serialization or output generation.
     * When nothing is specified here, the default filename will be: {@code mirror.json}
     *
     * @return a {@code Property<String>} representing the file name for the output.
     */
    public abstract Property<String> getFileName();
}

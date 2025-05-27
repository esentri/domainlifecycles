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

package io.domainlifecycles.plugins.json;

import java.net.URL;
import java.util.List;

/**
 * Interface for serializing information from given classpath files and context packages into a JSON string.
 * Implementations of this interface are responsible for processing domain models and producing serialized JSON output.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public interface JsonSerializer {

    /**
     * Serializes domain models into a JSON string representation.
     * Initializes the domain model using the provided classpath files and domain model packages.
     *
     * @param classPathFiles a list of URLs representing the classpath files used to initialize the domain model
     * @param domainModelPackages a list of strings specifying the package names that contain the classes defining the domain model
     * @return a JSON string representation of the serialized domain model
     */
    String serialize(List<URL> classPathFiles, final List<String> domainModelPackages);
}

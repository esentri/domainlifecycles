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

import io.domainlifecycles.plugins.exception.DLCPluginsException;

/**
 * The FileType enum represents various file types based on their file suffixes.
 * Each enum value corresponds to a specific file type and its associated suffix.
 *
 * @author Leon Völlinger
 */
public enum FileType {
    PDF(".pdf"),
    SVG(".svg"),
    PNG(".png"),
    JPG( ".jpg"),
    NOMNOML(".nomnoml");

    private final String fileSuffix;

    FileType(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    /**
     * Retrieves a {@code FileType} instance by its name, ignoring case differences.
     *
     * @param name the name of the file type to retrieve, case-insensitive
     * @return the {@code FileType} instance that matches the provided name
     * @throws DLCPluginsException if no matching {@code FileType} is found for the provided name
     */
    public static FileType byName(final String name) {
        for (FileType fileType : FileType.values()) {
            if (fileType.name().equalsIgnoreCase(name)) {
                return fileType;
            }
        }
        throw DLCPluginsException.fail(String.format("Could not find matching FileType for file-name %s.", name));
    }

    /**
     * Retrieves the file suffix associated with this file type.
     *
     * @return the file suffix as a string
     */
    public String getFileSuffix() {
        return fileSuffix;
    }
}

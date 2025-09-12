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

package io.domainlifecycles.utils;

import io.domainlifecycles.exception.DLCGradlePluginException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for performing file I/O operations.
 * Provides methods to write content to a file given its path and content,
 * either in string or byte array format.
 *
 * The methods in this class make use of Java's {@code java.nio.file.Files}
 * for writing data and handle exceptions by throwing a
 * {@code DLCGradlePluginException} in case of an error.
 *
 * @author Leon Völlinger
 */
public class FileIOUtils {

    /**
     * Writes the provided file content to the specified path.
     * This method accepts the file content as a string and delegates the task
     * to the overloaded version of {@code writeFileTo} that works with byte arrays.
     *
     * @param path the path of the file where the content should be written
     * @param fileContent the content to be written to the file as a string
     */
    public static void writeFileTo(final Path path, final String fileContent) {
        writeFileTo(path, fileContent.getBytes());
    }

    /**
     * Writes the provided file content to the specified path.
     * This method takes a byte array as the file content and writes it to the file at the specified path.
     * If an I/O error occurs during the write operation, a {@code DLCGradlePluginException} is thrown.
     *
     * @param path the path of the file where the content should be written
     * @param fileContent the content to be written to the file as a byte array
     * @throws DLCGradlePluginException if an error occurs during the write operation
     */
    public static void writeFileTo(final Path path, final byte[] fileContent) {
        try {
            Files.write(path, fileContent);
        } catch (IOException e) {
            throw DLCGradlePluginException.fail(String.format("Error occurred while trying to save file to %s.", path), e);
        }
    }
}

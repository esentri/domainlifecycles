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

import io.domainlifecycles.exception.DLCMavenPluginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for file input/output operations.
 *
 * This class provides methods for writing file content to the specified path.
 * It supports both string and byte array formats for the file content.
 *
 * Responsibilities:
 * - Write content to a file at a given path in string format.
 * - Write content to a file at a given path in byte array format.
 *
 * Error Handling:
 * - If an I/O operation fails, a custom runtime exception, {@code DLCMavenPluginException}, is thrown with a detailed message.
 *
 * @author Leon Völlinger
 */
public class FileIOUtils {

    /**
     * Writes the given string content to a file at the specified path.
     *
     * This method converts the provided string content into a byte array and
     * delegates the file writing operation to the byte array-based overload
     * of {@code writeFileTo}. If an I/O error occurs during this operation,
     * a runtime exception will be thrown.
     *
     * @param path the {@code Path} object representing the file location where the content should be written
     * @param fileContent the string content to be written to the file
     */
    public static void writeFileTo(final Path path, final String fileContent) {
        writeFileTo(path, fileContent.getBytes());
    }

    /**
     * Writes the given byte array content to a file at the specified path.
     *
     * This method writes the provided byte array to a file. If an I/O error
     * occurs during the operation, a custom runtime exception is thrown.
     *
     * @param path the {@code Path} object representing the file location where the content should be written
     * @param fileContent the byte array content to be written to the file
     * @throws DLCMavenPluginException if an I/O error occurs while writing to the file
     */
    public static void writeFileTo(final Path path, final byte[] fileContent) {
        try {
            Files.write(path, fileContent);
        } catch (IOException e) {
            throw DLCMavenPluginException.fail(String.format("Error occurred while trying to save file to %s.", path), e);
        }
    }
}

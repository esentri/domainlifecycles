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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.plugins.util;

import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileIOUtilsTest {

    @TempDir
    Path tempDir;

    private Path targetFile;

    @BeforeEach
    void setUp() {
        targetFile = tempDir.resolve("nested").resolve("dir").resolve("out.txt");
    }

    @Test
    void writesStringContentAndCreatesMissingParentDirectories() throws IOException {
        FileIOUtils.writeFileTo(targetFile, "hello world");

        assertThat(Files.exists(targetFile)).isTrue();
        assertThat(Files.readString(targetFile, StandardCharsets.UTF_8)).isEqualTo("hello world");
    }

    @Test
    void writesByteContent() throws IOException {
        byte[] content = "binary content".getBytes(StandardCharsets.UTF_8);

        FileIOUtils.writeFileTo(targetFile, content);

        assertThat(Files.readAllBytes(targetFile)).isEqualTo(content);
    }

    @Test
    void overwritesExistingFile() throws IOException {
        FileIOUtils.writeFileTo(targetFile, "first");
        FileIOUtils.writeFileTo(targetFile, "second");

        assertThat(Files.readString(targetFile)).isEqualTo("second");
    }

    @Test
    void wrapsIoExceptionInDLCPluginsException() {
        // the parent directory exists as a regular file, so createDirectories() must fail
        Path blockingFile = tempDir.resolve("blocking-file");
        Path pathWithBlockedParent = blockingFile.resolve("out.txt");

        assertThatThrownBy(() -> {
            Files.writeString(blockingFile, "not a directory");
            FileIOUtils.writeFileTo(pathWithBlockedParent, "content");
        }).isInstanceOf(DLCPluginsException.class);
    }
}

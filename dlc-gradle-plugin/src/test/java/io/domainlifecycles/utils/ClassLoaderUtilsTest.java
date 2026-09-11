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

package io.domainlifecycles.utils;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassLoaderUtilsTest {

    @TempDir
    Path tempDir;

    private Project project;

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    void collectsUrlsForExistingFilesFromBothCollections() throws IOException {
        Path classPathFile = Files.createFile(tempDir.resolve("classpath-entry.jar"));
        Path outputFile = Files.createFile(tempDir.resolve("output-entry.jar"));

        ConfigurableFileCollection classPathFiles = project.getObjects().fileCollection().from(classPathFile.toFile());
        ConfigurableFileCollection outputFiles = project.getObjects().fileCollection().from(outputFile.toFile());

        List<URL> urls = ClassLoaderUtils.getClasspathFiles(classPathFiles, outputFiles);

        assertThat(urls).hasSize(2);
        assertThat(urls).anySatisfy(url -> assertThat(url.getPath()).endsWith("classpath-entry.jar"));
        assertThat(urls).anySatisfy(url -> assertThat(url.getPath()).endsWith("output-entry.jar"));
    }

    @Test
    void skipsFilesThatDoNotExist() {
        Path nonExisting = tempDir.resolve("does-not-exist.jar");

        ConfigurableFileCollection classPathFiles = project.getObjects().fileCollection().from(nonExisting.toFile());
        ConfigurableFileCollection outputFiles = project.getObjects().fileCollection();

        List<URL> urls = ClassLoaderUtils.getClasspathFiles(classPathFiles, outputFiles);

        assertThat(urls).isEmpty();
    }

    @Test
    void returnsEmptyListForEmptyCollections() {
        ConfigurableFileCollection empty = project.getObjects().fileCollection();

        List<URL> urls = ClassLoaderUtils.getClasspathFiles(empty, empty);

        assertThat(urls).isEmpty();
    }
}

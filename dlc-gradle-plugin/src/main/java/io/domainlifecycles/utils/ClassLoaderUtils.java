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
import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides methods for obtaining classpath details related to a given project.
 * It is designed to facilitate working with class loaders in a Gradle project setup.
 *
 * @author Leon Völlinger
 */
public class ClassLoaderUtils {

    private final static Logger log = LoggerFactory.getLogger(ClassLoaderUtils.class);

    /**
     * Retrieves a list of classpath files for the parent project, including the build directory
     * and any runtime dependencies resolved from the project's configuration.
     *
     * @param project the Gradle project from which to extract the parent classpath files
     * @return a list of URLs representing the parent project classpath files
     * @throws DLCGradlePluginException if an error occurs while retrieving or processing the classpath files
     */
    public static List<URL> getParentClasspathFiles(Project project) {
        List<URL> parentClasspath = new ArrayList<>();
        try {
            File parentProjectBuildDir = new File(project.getLayout().getBuildDirectory().getAsFile().get(), "classes/java/main");
            parentClasspath.add(parentProjectBuildDir.toURI().toURL());

            project.getConfigurations()
                .getByName("runtimeClasspath")
                .getResolvedConfiguration()
                .getResolvedArtifacts()
                .forEach(resolvedArtifact -> {
                    try {
                        parentClasspath.add(resolvedArtifact.getFile().toURI().toURL());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });

            parentClasspath.forEach(element -> log.debug("Identified classpath element: {}", element));


            return parentClasspath;
        } catch (Exception e) {
            throw DLCGradlePluginException.fail("Failed to configure plugin with project classloader", e);
        }
    }
}

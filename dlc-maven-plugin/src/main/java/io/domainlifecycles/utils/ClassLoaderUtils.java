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
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for handling classloader-related operations within a Maven project.
 *
 * Responsibilities:
 * - Extracts and processes the classpath elements of a given Maven project.
 * - Constructs a list of URLs representing the project's classpath for use in
 *   dynamic classloading scenarios.
 *
 * This class interacts primarily with MavenProject data to retrieve compile and
 * runtime classpath elements, as well as the project's output directory.
 *
 * @author Leon Völlinger
 */
public class ClassLoaderUtils {

    private final static Logger log = LoggerFactory.getLogger(ClassLoaderUtils.class);

    /**
     * Generates a list of URLs representing the classpath elements of the provided Maven project.
     * This includes compile classpath elements, runtime classpath elements, and the build output directory.
     *
     * @param project the MavenProject from which to retrieve classpath elements
     * @return a list of URLs representing the parent classpath elements
     * @throws DLCMavenPluginException if an error occurs during dependency resolution or URL conversion
     */
    public static List<URL> getParentClasspathFiles(MavenProject project) {
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            classpathElements.addAll(project.getRuntimeClasspathElements());
            classpathElements.add(project.getBuild().getOutputDirectory());

            classpathElements.forEach(element -> log.debug("Identified classpath element: {}", element));

            return classpathElements.stream()
                .map(File::new)
                .map(File::toURI)
                .map(uri -> {
                    try {
                        return uri.toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        } catch (DependencyResolutionRequiredException e) {
            throw DLCMavenPluginException.fail("Error while changing Classloader.", e);
        }
    }
}

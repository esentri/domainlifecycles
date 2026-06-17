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
import org.gradle.api.file.ConfigurableFileCollection;
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
     * Converts the files from the provided classpath and output file collections into a list of URLs.
     * The method checks the existence of each file before attempting to convert it.
     * If a malformed URL is encountered, a {@link DLCGradlePluginException} is thrown.
     *
     * @param classPathFiles the collection of classpath files to be converted to URLs
     * @param outputFiles the collection of output files to be converted to URLs
     * @return a list of URLs corresponding to the files in the classpath and output file collections
     * @throws DLCGradlePluginException if a URL for a file cannot be correctly formed
     */
    public static List<URL> getClasspathFiles(ConfigurableFileCollection classPathFiles, ConfigurableFileCollection outputFiles) {
            List<URL> urls = new ArrayList<>();
            try {
                for (File file : classPathFiles.getFiles()) {
                    if (file.exists()) urls.add(file.toURI().toURL());
                }
                for (File file : outputFiles.getFiles()) {
                    if (file.exists()) urls.add(file.toURI().toURL());
                }
            }catch (MalformedURLException e){
                throw DLCGradlePluginException.fail("Failed to configure plugin with project classloader", e);
            }
            return urls;
    }
}

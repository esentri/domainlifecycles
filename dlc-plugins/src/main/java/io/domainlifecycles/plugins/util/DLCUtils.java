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

package io.domainlifecycles.plugins.util;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;

/**
 * Utility class providing methods to assist with initializing a domain model
 * from classpath resources and managing class loaders.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class DLCUtils {

    private static final Logger log = LoggerFactory.getLogger(DLCUtils.class);

    /**
     * Initializes a domain model by scanning the classes available in the specified classpath files
     * and bounded context packages. Uses a reflective domain model factory to analyze and process
     * the required data to build the domain model.
     *
     * @param classPathFiles a list of URLs pointing to the classpath entries to be loaded
     * @param domainMirrorPackages a list of package names to be scanned
     * @return the initialized DomainMirror containing all mirrored domain types and bounded context mirrors
     * @throws DLCPluginsException if the DomainMirror could not be initialized due to missing or invalid classpath data
     */
    public static DomainMirror initializeDomainMirrorFromClassPath(List<URL> classPathFiles, String... domainMirrorPackages){

        if(classPathFiles != null) {
            var cl = subClassLoader(classPathFiles);
            if (cl.isPresent()) {
                log.info("Classes loaded - Initializing domain model");
                final ReflectiveDomainMirrorFactory domainModelFactory = new ReflectiveDomainMirrorFactory(domainMirrorPackages);
                domainModelFactory.setExternalClassLoader(cl.get());
                domainModelFactory.setGenericTypeResolver(new TypeMetaResolver());
                var dm = domainModelFactory.initializeDomainMirror();
                log.info("Domain model initialized");
                log.info("Mirrored types count = " + dm.getAllDomainTypeMirrors().size());
                return dm;
            }
        }
        throw DLCPluginsException.fail("DomainMirror could not be initialized!");
    }

    private static Optional<ClassLoader> subClassLoader(final List<URL> filesToAddToClasspath) {
        if(filesToAddToClasspath == null) return Optional.empty();
        try {
            URLClassLoader childClassLoader = new URLClassLoader(filesToAddToClasspath.toArray(URL[]::new), DLCUtils.class.getClassLoader());
            return Optional.of(childClassLoader);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }


}

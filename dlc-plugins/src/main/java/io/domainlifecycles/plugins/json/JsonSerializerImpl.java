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

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.serialize.api.DomainSerializer;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.plugins.util.DLCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * Implementation of the {@link JsonSerializer} interface that serializes
 * domain models into JSON strings using a domain serializer.
 *
 * This class processes information from provided classpath files and
 * context packages to initialize a domain model and then serializes
 * the domain model into a JSON format. It supports pretty print
 * serialization through configuration at initialization.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class JsonSerializerImpl implements JsonSerializer {

    private final static Logger log = LoggerFactory.getLogger(JsonSerializerImpl.class);

    private final DomainSerializer domainSerializer;

    /**
     * Constructs an instance of the JsonSerializerImpl.
     *
     * Initializes the internal domain serializer with the provided configuration for pretty print.
     * The domain serializer is responsible for converting a domain model into a JSON string.
     *
     * @param prettyPrint a boolean flag indicating whether the JSON output should be formatted
     *                    in a human-readable way with indentation (true) or minified (false).
     */
    public JsonSerializerImpl(boolean prettyPrint) {
        this.domainSerializer = new JacksonDomainSerializer(prettyPrint);
    }

    /**
     * Serializes a domain model into a JSON string representation. The domain model is initialized
     * using the provided classpath files and context packages.
     *
     * @param classPathFiles a list of URLs representing the classpath files from which the domain model is initialized
     * @param domainModelPackages a list of string package names that contain the classes defining the domain model
     * @return a JSON string representation of the serialized domain model
     * @throws DLCPluginsException if the domain model could not be initialized from the provided classpath files
     */
    @Override
    public String serialize(List<URL> classPathFiles, final List<String> domainModelPackages) {
        DomainMirror dm = null;
        try {
            dm = DLCUtils.initializeDomainMirrorFromClassPath(classPathFiles, domainModelPackages.toArray(String[]::new));
        } catch(RuntimeException e) {
            throw DLCPluginsException.fail("DLC couldn't be initialized.", e);
        }

        log.info("Serializing domain model to JSON");
        return domainSerializer.serialize(dm);
    }
}

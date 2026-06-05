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
 *  Copyright 2019-2024 the original author or authors.
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

package io.domainlifecycles.mirror.serialize;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.exception.MirrorException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A factory class responsible for loading and initializing a {@link DomainMirror} by deserializing its configuration
 * from a predefined JSON file. The deserialization process is facilitated by the provided implementation of
 * {@link DomainSerializer}.
 *
 * The factory reads the configuration from the file located at the path defined by the constant
 * {@code META_INF_DLC_MIRROR_FILE_PATH}. If the file is not found or cannot be read, an exception is thrown.
 *
 * This factory implementation is particularly useful for scenarios where the {@link DomainMirror} instances
 * are predefined and need to be loaded from external resources rather than being dynamically generated.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class DeserializingDomainMirrorFactory implements DomainMirrorFactory {

    /**
     * The relative file path to the JSON file defining domain lifecycle mirrors.
     * This file is expected to be located under the {@code META-INF/dlc} directory
     * and is named {@code mirror.json}.
     *
     * The file serves as the primary configuration source for initializing and
     * deserializing domain lifecycle mirrors within the system.
     */
    public static final String META_INF_DLC_MIRROR_FILE_PATH = "META-INF/dlc/mirror.json";

    private final DomainSerializer domainSerializer;

    /**
     * Constructs a {@code DeserializingDomainMirrorFactory} with the specified {@link DomainSerializer}.
     * The provided {@code domainSerializer} is used for deserializing a domain model from its serialized representation.
     *
     * @param domainSerializer the {@link DomainSerializer} instance to be used for deserialization
     *                         (must not be {@code null})
     * @throws NullPointerException if the {@code domainSerializer} is {@code null}
     */
    public DeserializingDomainMirrorFactory(DomainSerializer domainSerializer) {
        this.domainSerializer = Objects.requireNonNull(domainSerializer, "DomainSerializer cannot be null!");
    }

    /**
     * Initializes the domain with the mirrors defined in {@code mirror.json} file under the {@code META-INF/dlc} directory.
     *
     * @return DomainMirror - a container for all mirrors that were read in the file.
     */
    @Override
    public DomainMirror initializeDomainMirror() {
        String mirrorJson = readMirrorJsonFile(META_INF_DLC_MIRROR_FILE_PATH);
        return deserializeDomainMirror(mirrorJson);
    }

    /**
     * Initializes a DomainMirror instance by deserializing the provided JSON mirror string representation.
     *
     * @param mirrorJson the JSON string containing the serialized DomainMirror configuration
     * @return the deserialized DomainMirror instance
     */
    protected DomainMirror deserializeDomainMirror(String mirrorJson) {
        return domainSerializer.deserialize(mirrorJson);
    }

    /**
     * Reads the contents of a JSON file located at the specified path and returns its content as a string.
     * The method throws a {@link MirrorException} if the file cannot be found or read.
     *
     * @param path the relative file path of the JSON file to be read
     * @return the contents of the JSON file as a UTF-8 encoded string
     * @throws MirrorException if the file is not found or cannot be read
     */
    protected String readMirrorJsonFile(String path) {
        try (InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream(path)) {

            if (inputStream == null) {
                throw MirrorException.fail(
                    String.format("Could not find mirror file for serializing. Make sure file %s is present.",
                        path));
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw MirrorException.fail("Could not read mirror file.", e);
        }
    }
}

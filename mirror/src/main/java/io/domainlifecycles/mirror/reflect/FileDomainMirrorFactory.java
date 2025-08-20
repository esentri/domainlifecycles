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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.serialize.api.DomainSerializer;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileDomainMirrorFactory implements DomainMirrorFactory {

    public static final String META_INF_DLC_MIRROR_FILE_PATH = "META-INF/dlc/mirror.json";

    /**
     * Initializes the domain with the mirrors defined in {@code mirror.json} file under the {@code META-INF/dlc} directory.
     *
     * @return DomainMirror - a container for all mirrors that were read in the file.
     */
    @Override
    public DomainMirror initializeDomainMirror() {
        String mirrorJson = readMirrorJsonFile();
        return initializeDomainMirror(mirrorJson);
    }

    /**
     * Initializes a DomainMirror instance by deserializing the provided JSON mirror string representation.
     *
     * @param mirrorJson the JSON string containing the serialized DomainMirror configuration
     * @return the deserialized DomainMirror instance
     */
    private DomainMirror initializeDomainMirror(String mirrorJson) {
        DomainSerializer domainSerializer = new JacksonDomainSerializer(false);
        return domainSerializer.deserialize(mirrorJson);
    }

    /**
     * Reads the mirror JSON file located at the predefined path and returns its content as a String.
     * If the file is not found or cannot be read, a MirrorException is thrown.
     *
     * @return the content of the mirror JSON file as a String
     * @throws MirrorException if the file cannot be found or read
     */
    private String readMirrorJsonFile() {
        try (InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream(META_INF_DLC_MIRROR_FILE_PATH)) {

            if (inputStream == null) {
                throw MirrorException.fail(
                    String.format("Could not find mirror file for serializing. Make sure file %s is present.",
                        META_INF_DLC_MIRROR_FILE_PATH));
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw MirrorException.fail("Could not read mirror file.", e);
        }
    }
}

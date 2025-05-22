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

package io.domainlifecycles.plugins.viewer.model.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Custom serializer for serializing a string in its raw JSON form.
 *
 * This class extends the {@link StdSerializer} to customize the way string values
 * are serialized in JSON. By default, it bypasses escaping or other transformations
 * and writes the string as raw JSON content.
 *
 * It is useful for cases where the string contains pre-formatted or raw JSON that
 * should be directly included in the serialized output without alteration.
 *
 * @author Leon Völlinger
 */
public class DomainMirrorJsonSerializer extends StdSerializer<String> {

    /**
     * Default constructor for the {@code DomainMirrorJsonSerializer}.
     *
     * Initializes the serializer with the default handled type as {@code String}.
     * This serializer is specifically designed to handle Strings to retain their
     * raw JSON representation during serialization.
     */
    public DomainMirrorJsonSerializer() {
        super(String.class);
    }

    /**
     * Serializes a string into its raw JSON representation.
     *
     * This method writes the string value directly to the JSON output without escaping
     * or additional formatting, treating it as raw JSON content. It is particularly
     * useful for serializing strings that already represent JSON to prevent unnecessary
     * transformations.
     *
     * @param value the string value to serialize
     * @param gen the JSON generator used to write the serialized output
     * @param provider the serializer provider for accessing serializers and configuration
     * @throws IOException if an I/O error occurs during serialization
     */
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeRawValue(value);
    }
}

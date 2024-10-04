/*
 *
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

package io.domainlifecycles.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;

import java.io.IOException;

/**
 * {@link Domain} based serialization of {@link Identity} instances.
 *
 * @author Mario Herb
 * @see StdSerializer
 */
@SuppressWarnings("rawtypes")
public class IdentitySerializer extends StdSerializer<Identity> {

    public IdentitySerializer() {
        super(Identity.class);
    }

    /**
     * Serialize Identities.
     *
     * @param identity           Value to serialize; can <b>not</b> be null.
     * @param jsonGenerator      Generator used to output resulting Json content
     * @param serializerProvider Provider that can be used to get serializers for
     *                           serializing Objects value contains, if any.
     * @throws IOException if serialization fails
     */
    @Override
    public void serialize(Identity identity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!jsonGenerator.getOutputContext().inRoot()) {
            Object value = identity.value();
            jsonGenerator.writeObject(value);
        } else {
            jsonGenerator.writeStartObject();
            Object value = identity.value();
            jsonGenerator.writeFieldName("id");
            jsonGenerator.writeObject(value);
            jsonGenerator.writeEndObject();
        }
    }
}

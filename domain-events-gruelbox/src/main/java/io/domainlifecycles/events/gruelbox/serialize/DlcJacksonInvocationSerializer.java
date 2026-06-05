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

package io.domainlifecycles.events.gruelbox.serialize;

import com.gruelbox.transactionoutbox.Invocation;
import com.gruelbox.transactionoutbox.InvocationSerializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Jackson-based implementation of {@link InvocationSerializer} for Domain Lifecycle Events.
 *
 * @author Mario Herb
 */
public final class DlcJacksonInvocationSerializer implements InvocationSerializer {
    private final ObjectMapper mapper;

    /**
     * Constructor with custom Jackson ObjectMapper.
     *
     * @param mapper Jackson ObjectMapper instance
     */
    public DlcJacksonInvocationSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Constructor with default Jackson configuration for Domain Lifecycle Events.
     */
    public DlcJacksonInvocationSerializer(
    ) {
        this.mapper = JsonMapper.builder()
            .setDefaultTyping(TransactionOutboxJacksonModule.typeResolver())
            .addModule(new TransactionOutboxJacksonModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .build();
    }

    /**
     * Serializes the given {@link Invocation} object into the provided {@link Writer}.
     *
     * @param invocation The {@link Invocation} object to serialize. Cannot be null.
     * @param writer The {@link Writer} instance where the serialized representation
     *               of the {@link Invocation} object will be written. Cannot be null.
     * @throws RuntimeException If an error occurs during serialization due to a {@link JacksonException}.
     */
    @Override
    public void serializeInvocation(Invocation invocation, Writer writer) {
        try {
            mapper.writeValue(writer, invocation);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes an {@link Invocation} object from a character stream.
     *
     * @param reader The {@link Reader} instance providing the input data
     *               to deserialize the {@link Invocation} object from.
     * @return The deserialized {@link Invocation} object.
     * @throws IOException If an I/O error occurs while reading the input.
     */
    @Override
    public Invocation deserializeInvocation(Reader reader) throws IOException {
        // read ahead to check if old style
        BufferedReader br = new BufferedReader(reader);
        return mapper.readValue(br, Invocation.class);
    }
}

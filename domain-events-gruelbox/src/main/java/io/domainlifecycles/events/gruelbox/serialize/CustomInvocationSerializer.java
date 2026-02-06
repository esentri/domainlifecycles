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
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.jsontype.TypeSerializer;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * Custom serializer for {@link Invocation} objects to ensure consistent serialization format.
 *
 * @author Mario Herb
 */
class CustomInvocationSerializer extends StdSerializer<Invocation> {

    /**
     * Constructor for CustomInvocationSerializer.
     */
    public CustomInvocationSerializer() {
        this(Invocation.class);
    }

    protected CustomInvocationSerializer(Class<Invocation> t) {
        super(t);
    }

    /**
     * Serialize an {@link Invocation} object with type information.
     *
     * @param value Value to serialize; can <b>not</b> be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Context that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @param typeSer Type serializer to use for including type information
     * @throws JacksonException
     */
    @Override
    public void serializeWithType(
        Invocation value, JsonGenerator gen, SerializationContext serializers, TypeSerializer typeSer)
        throws JacksonException {
        serialize(value, gen, serializers);
    }

    /**
     * Serialize an {@link Invocation} object
     *
     * @param value Value to serialize; can <b>not</b> be null.
     * @param gen Generator used to output resulting Json content
     * @param provider Context that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws JacksonException
     */
    @Override
    public void serialize(Invocation value, JsonGenerator gen, SerializationContext provider)
        throws JacksonException {
        gen.writeStartObject();
        gen.writeStringProperty("className", value.getClassName());
        gen.writeStringProperty("methodName", value.getMethodName());
        gen.writeArrayPropertyStart("parameterTypes");
        for (Class<?> parameterType : value.getParameterTypes()) {
            gen.writeString(parameterType.getCanonicalName());
        }
        gen.writeEndArray();
        gen.writePOJOProperty("args", value.getArgs());
        gen.writePOJOProperty("mdc", value.getMdc());
        gen.writePOJOProperty("session", value.getSession());
        gen.writeEndObject();
    }
}


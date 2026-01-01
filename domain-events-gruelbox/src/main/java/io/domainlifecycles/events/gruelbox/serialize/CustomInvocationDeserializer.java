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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.jsontype.TypeDeserializer;
import tools.jackson.databind.node.ArrayNode;

/**
 * Custom deserializer for {@link Invocation} objects that handles deserialization of immutable collections.
 *
 * @author Mario Herb
 */
class CustomInvocationDeserializer extends StdDeserializer<Invocation> {

    private static final Pattern setPattern =
        Pattern.compile("\\{\\w*\"(java.util.ImmutableCollections\\$Set[\\dN]+)\"\\w*:");
    private static final Pattern mapPattern =
        Pattern.compile("\\{\\w*\"(java.util.ImmutableCollections\\$Map[\\dN]+)\"\\w*:");
    private static final Pattern listPattern =
        Pattern.compile("\\{\\w*\"(java.util.ImmutableCollections\\$List[\\dN]+)\"\\w*:");

    protected CustomInvocationDeserializer(Class<?> vc) {
        super(vc);
    }

    CustomInvocationDeserializer() {
        this(Invocation.class);
    }

    /**
     * Deserialize invocation from JSON
     * @param p JsonParser
     * @param ctxt DeserializationContext
     * @param typeDeserializer Deserializer to use for handling type information
     * @return Invocation
     * @throws JacksonException
     */
    @Override
    public Invocation deserializeWithType(
        JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
        throws JacksonException {
        return deserialize(p, ctxt);
    }

    /**
     * Deserialize invocation from JSON
     *
     * @param p Parser used for reading JSON content
     * @param ctxt Context that can be used to access information about
     *   this deserialization activity.
     *
     * @return
     * @throws JacksonException
     */
    @Override
    public Invocation deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        JsonNode node = p.objectReadContext().readTree(p);
        String className = node.get("className").asString();
        String methodName = node.get("methodName").asString();
        ArrayNode paramTypes = ((ArrayNode) node.get("parameterTypes"));
        JsonNode arguments = node.get("args");
        JsonNode processedArguments = replaceImmutableCollections(arguments, p);
        Class<?>[] types = new Class<?>[paramTypes.size()];

        for (int i = 0; i < paramTypes.size(); i++) {
            try {
                types[i] = ClassUtils.getClass(paramTypes.get(i).asString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Object[] args = p.objectReadContext().treeAsTokens(processedArguments).readValueAs(Object[].class);

        Map<String, String> mdc =
            p.objectReadContext()
                .readValue(p.objectReadContext().treeAsTokens(node.get("mdc")), new TypeReference<>() {});

        var sessionNode = node.get("session");
        Map<String, String> session = null;
        if (sessionNode != null && !sessionNode.isNull()) {
            Map<String, String> sessTmp = new HashMap<>();
            sessionNode.forEachEntry((key, value) -> sessTmp.put(key, value.asText()));
            session = sessTmp;
        }
        return new Invocation(className, methodName, types, args, mdc, session);
    }

    private JsonNode replaceImmutableCollections(JsonNode arguments, JsonParser p)
        throws JacksonException {
        String args = arguments.toString();
        args = setPattern.matcher(args).replaceAll("{\"java.util.HashSet\":");
        args = mapPattern.matcher(args).replaceAll("{\"java.util.HashMap\":");
        args = listPattern.matcher(args).replaceAll("{\"java.util.ArrayList\":");
        JsonParser parser = p.objectReadContext().createParser(args);
        return p.objectReadContext().readTree(parser);
    }
}

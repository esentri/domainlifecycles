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
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom deserializer for {@link TransactionOutboxEntry} to handle JSON deserialization.
 *
 * @author Mario Herb
 */
class TransactionOutboxEntryDeserializer extends ValueDeserializer<TransactionOutboxEntry> {

    /**
     * Deserialize JSON content into a {@link TransactionOutboxEntry} object.
     *
     * @param p Parser used for reading JSON content
     * @param c Context that can be used to access information about
     *   this deserialization activity.
     *
     * @return
     * @throws JacksonException
     */
    @Override
    public TransactionOutboxEntry deserialize(JsonParser p, DeserializationContext c) throws JacksonException {
        ObjectReadContext oc = p.objectReadContext();
        JsonNode entry = oc.readTree(p);
        var invocation = entry.get("invocation");
        return TransactionOutboxEntry.builder()
            .id(entry.get("id").asText())
            .lastAttemptTime(mapNullableInstant(entry.get("lastAttemptTime"), c))
            .nextAttemptTime(mapNullableInstant(entry.get("nextAttemptTime"), c))
            .attempts(entry.get("attempts").asInt())
            .blocked(entry.get("blocked").asBoolean())
            .processed(entry.get("processed").asBoolean())
            .uniqueRequestId(mapNullableString(entry.get("uniqueRequestId")))
            .version(entry.get("version").asInt())
            .invocation(
                new Invocation(
                    invocation.get("className").asText(),
                    invocation.get("methodName").asText(),
                    c.readTreeAsValue(invocation.get("parameterTypes"), Class[].class),
                    c.readTreeAsValue(invocation.get("args"), Object[].class),
                    mapNullableStringMap(invocation.get("mdc"), c),
                    mapNullableStringMap(invocation.get("session"), c)))
            .build();
    }

    private String mapNullableString(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        return node.asText();
    }

    private Instant mapNullableInstant(JsonNode node, DeserializationContext c) throws JacksonException {
        if (node == null || node.isNull()) {
            return null;
        }
        return c.readTreeAsValue(node, Instant.class);
    }

    private Map<String, String> mapNullableStringMap(JsonNode node, DeserializationContext c) {
        if (node == null || node.isNull()) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        node.forEachEntry((key, value) -> result.put(key, value.asText()));
        return result;
    }

}

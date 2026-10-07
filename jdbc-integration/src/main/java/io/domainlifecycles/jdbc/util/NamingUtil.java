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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Helper naming utilities to transform snake case String into camel case and the other way round.
 * <p>
 * Mirrors {@code io.domainlifecycles.jooq.util.NamingUtil} from the jOOQ based persistence integration:
 * both integrations use the same "camelCase property name" &lt;-&gt; "SNAKE_CASE column name" convention, so
 * record property names stay consistent across both, which lets shared components such as
 * {@link io.domainlifecycles.persistence.mapping.RecordPropertyMatcher} implementations behave identically.
 *
 * @author Mario Herb
 */
public final class NamingUtil {

    private NamingUtil() {
    }

    /**
     * Transform a snake case String into camel case.
     *
     * @param snakeCase input String
     * @return camelCase String
     */
    public static String snakeCaseToCamelCase(String snakeCase) {
        StringBuilder builder = new StringBuilder(snakeCase.toLowerCase());

        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '_') {
                // collapse a run of one or more consecutive underscores into a single word
                // boundary, and stop deleting once nothing is left to capitalize (a trailing
                // underscore), rather than reading past the end of the shrunk builder
                do {
                    builder.deleteCharAt(i);
                } while (i < builder.length() && builder.charAt(i) == '_');
                if (i < builder.length()) {
                    builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
                }
            }
        }

        return builder.toString();
    }

    /**
     * Transform a camel case String into snake case.
     * <p>
     * A digit run stays attached to the word segment it trails (e.g. {@code "orderIdBv3"} becomes
     * {@code "order_id_bv3"}, not {@code "order_id_bv_3"}) - only a transition into an uppercase
     * letter starts a new segment. This matches {@code io.domainlifecycles.jooq.util.NamingUtil}'s
     * behavior (see this class's own class-level javadoc): both integrations derive a database
     * sequence name from an {@code Identity} class's simple name this way, and a digit-suffixed
     * class name (e.g. {@code OrderIdBv3}) already has a real, glued-together sequence name in the
     * shared test migration schema (e.g. {@code order_id_bv3_seq}) that a segmented conversion would
     * fail to find.
     *
     * @param camelCase input String
     * @return snake case String
     */
    public static String camelCaseToSnakeCase(String camelCase) {
        StringBuilder builder = new StringBuilder(camelCase);

        for (int i = 0; i < builder.length(); i++) {
            char c = builder.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.insert(i, "_");
                builder.replace(
                    i + 1, i + 2,
                    String.valueOf(
                        Character.toLowerCase(
                            builder.charAt(i + 1))));
            }
        }
        var returnVal = builder.toString();
        if (returnVal.startsWith("_")) {
            returnVal = returnVal.substring(1);
        }
        return returnVal;
    }

    /**
     * Derive the name of the database sequence an {@code Identity} type's values are drawn from: the
     * identity's simple name in snake case plus {@code _SEQ}. For an identity declared as an inner class,
     * every enclosing class name is prefixed, so {@code Order.OrderId} becomes {@code ORDER_ORDER_ID_SEQ}
     * and {@code Room.Id} becomes {@code ROOM_ID_SEQ}; a top-level {@code OrderId} stays {@code ORDER_ID_SEQ}.
     *
     * @param identityTypeName binary name of the identity type, as reported by the domain mirror
     *                         (e.g. {@code com.example.Room$Id})
     * @return upper case sequence name
     */
    public static String identitySequenceName(String identityTypeName) {
        var binarySimpleName = identityTypeName.substring(identityTypeName.lastIndexOf('.') + 1);
        // each '$'-separated segment is converted on its own, so "Room$Id" yields "room_id", not "room$_id"
        return Arrays.stream(binarySimpleName.split("\\$"))
            .map(NamingUtil::camelCaseToSnakeCase)
            .collect(Collectors.joining("_"))
            .toUpperCase() + "_SEQ";
    }
}

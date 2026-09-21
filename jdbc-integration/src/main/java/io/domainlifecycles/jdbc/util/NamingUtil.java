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
     * A digit run is also treated as its own segment (e.g. {@code "testEntity2Id"} becomes {@code
     * "test_entity_2_id"}, matching this module's own migration schema's {@code test_entity_2_id} column) -
     * not just a transition into an uppercase letter - since the physical column names this maps against
     * consistently separate a trailing/embedded number from the preceding word with its own underscore.
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
            } else if (Character.isDigit(c) && i > 0) {
                char previous = builder.charAt(i - 1);
                if (previous != '_' && !Character.isDigit(previous)) {
                    builder.insert(i, "_");
                }
            }
        }
        var returnVal = builder.toString();
        if (returnVal.startsWith("_")) {
            returnVal = returnVal.substring(1);
        }
        return returnVal;
    }
}

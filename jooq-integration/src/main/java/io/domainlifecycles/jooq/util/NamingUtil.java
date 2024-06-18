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

package io.domainlifecycles.jooq.util;

/**
 * Helper naming utilities to transform snake case String into caml case and the other way round.
 *
 * @author Mario Herb
 */
public class NamingUtil {

    /**
     * Transform a snake case String into camel case.
     *
     * @param snakeCase input String
     * @return camelCase String
     */
    public static String snakeCaseToCamelCase(String snakeCase) {
        // Convert to StringBuilder
        StringBuilder builder
            = new StringBuilder(snakeCase.toLowerCase());

        // Traverse the string character by
        // character and remove underscore
        // and capitalize next letter
        for (int i = 0; i < builder.length(); i++) {

            // Check char is underscore
            if (builder.charAt(i) == '_') {

                builder.deleteCharAt(i);
                builder.replace(
                    i, i + 1,
                    String.valueOf(
                        Character.toUpperCase(
                            builder.charAt(i))));
            }
        }

        return builder.toString();
    }

    /**
     * Transforma a camel case String into snake case.
     * @param camelCase input String
     * @return snake case String
     */
    public static String camelCaseToSnakeCase(String camelCase) {
        // Convert to StringBuilder
        StringBuilder builder
            = new StringBuilder(camelCase);

        // Traverse the string character by
        for (int i = 0; i < builder.length(); i++) {

            // Check char is upper case
            if (Character.isUpperCase(builder.charAt(i))) {

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
}

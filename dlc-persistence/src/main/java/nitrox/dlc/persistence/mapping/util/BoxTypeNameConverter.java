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

package nitrox.dlc.persistence.mapping.util;

/**
 * Low level conversion helper for Java primitive types
 *
 * @author Mario Herb
 */
public class BoxTypeNameConverter {

    /**
     * Return the boxed type if given type is primitive.
     *
     * @param className full qualified class name
     * @return corresponding full qualified boxed typename.
     */
    public static String convertToBoxedType(String className) {
        var returnClass = className;
        if (long.class.getName().equals(className)) {
            returnClass = Long.class.getName();
        } else if (int.class.getName().equals(className)) {
            returnClass = Integer.class.getName();
        } else if (double.class.getName().equals(className)) {
            returnClass = Double.class.getName();
        } else if (byte.class.getName().equals(className)) {
            returnClass = Byte.class.getName();
        } else if (short.class.getName().equals(className)) {
            returnClass = Short.class.getName();
        } else if (float.class.getName().equals(className)) {
            returnClass = Float.class.getName();
        } else if (boolean.class.getName().equals(className)) {
            returnClass = Boolean.class.getName();
        } else if (char.class.getName().equals(className)) {
            returnClass = Character.class.getName();
        }
        return returnClass;
    }
}

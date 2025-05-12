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

package io.domainlifecycles.persistence.mapping.converter;

/**
 * Converts a FROM type to a TO type.
 *
 * @param <FROM> the source type
 * @param <TO>   the target type
 * @author Mario Herb
 */
public abstract class TypeConverter<FROM, TO> {

    /**
     * Represents the class of the source type in the type conversion process.
     * This field is used to define the type being converted from and acts as an
     * identifier for the source type of a {@link TypeConverter}.
     */
    public final Class<FROM> fromClass;
    /**
     * Represents the class type of the target type in a type conversion process.
     * This field is used to define the type being converted to and acts as an
     * identifier for the destination type utilized by a {@link TypeConverter}.
     */
    public final Class<TO> toClass;

    /**
     * Constructs a new TypeConverter with the provided source and target type classes.
     *
     * @param fromClass the class of the source type to be converted
     * @param toClass the class of the target type after conversion
     */
    public TypeConverter(Class<FROM> fromClass, Class<TO> toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    /**
     * Converts a FROM type to a TO type.
     *
     * @param from the source type
     * @return the target type
     */
    public abstract TO convert(FROM from);

}

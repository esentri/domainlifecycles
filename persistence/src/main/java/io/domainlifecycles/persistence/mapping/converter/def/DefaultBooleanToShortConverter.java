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

package io.domainlifecycles.persistence.mapping.converter.def;

import io.domainlifecycles.persistence.mapping.converter.TypeConverter;

/**
 * Converts a Boolean to a Short.
 *
 * @author Mario Herb
 */
public class DefaultBooleanToShortConverter extends TypeConverter<Boolean, Short> {

    /**
     * Constructs a DefaultBooleanToShortConverter, which is a TypeConverter that defines
     * conversion behavior from a Boolean to a Short. This ensures type-safe conversions
     * between the specified types.
     */
    public DefaultBooleanToShortConverter() {
        super(Boolean.class, Short.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short convert(Boolean aBoolean) {
        if (aBoolean != null && aBoolean) {
            return Short.valueOf("1");
        }
        return Short.valueOf("0");
    }
}

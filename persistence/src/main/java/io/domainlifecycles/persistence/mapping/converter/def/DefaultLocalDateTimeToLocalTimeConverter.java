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

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Converts a LocalDateTime to a LocalTime.
 *
 * @author Mario Herb
 */
public class DefaultLocalDateTimeToLocalTimeConverter extends TypeConverter<LocalDateTime, LocalTime> {

    /**
     * Constructs a DefaultLocalDateTimeToLocalTimeConverter, which defines the behavior
     * for converting a LocalDateTime instance to a LocalTime instance.
     * This converter is type-safe, ensuring transformations are performed within the defined types.
     */
    public DefaultLocalDateTimeToLocalTimeConverter() {
        super(LocalDateTime.class, LocalTime.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime convert(LocalDateTime from) {
        if (null == from) {
            return null;
        }
        return from.toLocalTime();
    }
}

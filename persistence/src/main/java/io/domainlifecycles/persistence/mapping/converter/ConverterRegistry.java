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

package io.domainlifecycles.persistence.mapping.converter;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registry of converters.
 *
 * @author Mario Herb
 */
public class ConverterRegistry {

    private final Map<ConverterKey, TypeConverter<?, ?>> converterMap = new HashMap<>();

    /**
     * Registers a converter.
     *
     * @param tc the converter to register.
     */
    public void registerConverter(TypeConverter<?, ?> tc) {
        var k = new ConverterKey(tc.fromClass.getName(), tc.toClass.getName());
        converterMap.put(k, tc);

    }

    /**
     * Gets a converter.
     *
     * @param fromClassName full qualified name of source type to convert
     * @param toClassName   full qualified name of target type after conversion
     * @return the converter converting from {@code fromClass} to {@code toClass}
     */
    public TypeConverter<?, ?> getTypeConverter(String fromClassName, String toClassName) {

        var k = new ConverterKey(fromClassName, toClassName);
        var tc = converterMap.get(k);
        if (tc == null) {
            throw DLCPersistenceException.fail("No converter found to convert '%s' into '%s'.", fromClassName,
                toClassName);
        }
        return tc;
    }

    private record ConverterKey(String fromClassName, String toClassName) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            var that = (ConverterKey) o;
            return Objects.equals(fromClassName, that.fromClassName) &&
                Objects.equals(toClassName, that.toClassName);
        }

    }
}

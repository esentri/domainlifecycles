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

package io.domainlifecycles.spring.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.domain.types.Identity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * A converter factory that creates a converter from String to a given {@link Identity} type.
 *
 * @author Mario Herb
 * @author Dominik Galler
 */
@Deprecated
public class StringToDomainIdentityConverterFactory implements ConverterFactory<String, Identity<?>> {

    private final ObjectMapper objectMapper;

    /**
     * Constructor for StringToDomainIdentityConverterFactory
     *
     * @param objectMapper the ObjectMapper used
     */
    public StringToDomainIdentityConverterFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Identity<?>> Converter<String, T> getConverter(Class<T> targetClass) {
        return new StringToDomainIdentityConverter<>(targetClass, objectMapper);
    }

    private record StringToDomainIdentityConverter<T extends Identity<?>>(
        Class<T> targetClass,
        ObjectMapper objectMapper)
        implements Converter<String, T> {

        @Override
        public T convert(String source) {
            try {
                return objectMapper.readValue("{\"value\": \"" + source + "\"}", targetClass);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Not able to parse identity value", e);
            }
        }
    }
}

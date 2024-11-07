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
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.Domain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * A converter factory that creates a converter from String to a given {@link ValueObject} type.
 *
 * @author Mario Herb
 * @author Dominik Galler
 */
public class StringToDomainValueObjectConverterFactory
    implements ConverterFactory<String, ValueObject> {

    private final ObjectMapper objectMapper;

    public StringToDomainValueObjectConverterFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ValueObject> Converter<String, T> getConverter(Class<T> targetClass) {
        return new StringToDomainValueObjectConverter<>(targetClass, objectMapper);
    }

    private record StringToDomainValueObjectConverter<T extends ValueObject>(Class<T> targetClass,
                                                                             ObjectMapper objectMapper)
        implements Converter<String, T> {

        @Override
        public T convert(String source) {
            var voMirror = Domain.valueObjectMirrorFor(targetClass.getName());
            var singleValuedField = voMirror.singledValuedField();
            if (singleValuedField.isPresent()) {
                try {
                    return objectMapper.readValue("{\"" + singleValuedField.get().getName() + "\": \"" + source + "\"}",
                        targetClass);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(
                        String.format("Not able to parse single property value of ValueObject '%s'",
                            targetClass.getName()), e);
                }
            } else {
                throw new RuntimeException("Could not map value object of type " + targetClass.getName()
                    + " from String value '" + source + "'. Please provide a custom converter of type" +
                    " 'org.springframework.core.convert.converter.Converter'!");
            }
        }
    }
}

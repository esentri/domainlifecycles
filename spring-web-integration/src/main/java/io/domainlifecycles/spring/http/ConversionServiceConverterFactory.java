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
 *  Copyright 2019-2025 the original author or authors.
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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.support.FormattingConversionService;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for creating {@link Converter} instances that utilize a {@link ConversionService}
 * for type conversion operations. This class abstracts the process of obtaining a suitable
 * {@link ConversionService} and caching it for subsequent use, ensuring efficient type conversion
 * across multiple invocations.
 *
 * @param <T> the target type for which the converter is designed to operate.
 */
public abstract class ConversionServiceConverterFactory<T> implements ConverterFactory<String, T> {

    private final ObjectProvider<FormattingConversionService> formattingCsProvider;
    private final ObjectProvider<ConversionService> conversionCsProvider;
    private ConversionService cachedConversionService;
    private final Map<Class<?>, Converter<String, ?>> cache = new ConcurrentHashMap<>();

    /**
     * Constructs a new instance of ConversionServiceConverterFactory.
     *
     * @param formattingCsProvider the provider for {@link FormattingConversionService}.
     * @param conversionCsProvider the provider for {@link ConversionService}.
     */
    public ConversionServiceConverterFactory(
        ObjectProvider<FormattingConversionService> formattingCsProvider,
        ObjectProvider<ConversionService> conversionCsProvider
    ) {
        this.formattingCsProvider = formattingCsProvider;
        this.conversionCsProvider = conversionCsProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <S extends T>Converter<String, S> getConverter(Class<S> targetType) {
        return (Converter<String, S>) cache.computeIfAbsent(targetType, this::buildConverter);
    }

    /**
     * Retrieves the {@link ConversionService} to be used for type conversions.
     * This method first checks for the availability of a {@link FormattingConversionService}.
     * If no {@link FormattingConversionService} is available, it attempts to retrieve a basic
     * {@link ConversionService}.
     *
     * @param targetType the target type for which the converter is being built.
     * @return a {@link Converter} instance for converting strings to the specified target type.
     */
    private Converter<String, T> buildConverter(Class<?> targetType) {
        resolveConversionService();
        Objects.requireNonNull(targetType, "Target type cannot be null");
        Class<?> valueType = getValueTypeClass(targetType);
        return source -> {
            if (source == null) return null;
            Object baseValue = cachedConversionService.convert(source, valueType);
            return createTargetInstance(baseValue, targetType);
        };
    }

    /**
     * Resolves and caches the appropriate {@link ConversionService} to be used for type conversions.
     * The method checks for the availability of a {@link FormattingConversionService} first.
     * If no {@link FormattingConversionService} is available, it attempts to retrieve a basic
     * {@link ConversionService}. If a {@link ConversionService} is already cached, no action is taken.
     *
     * This ensures that the correct type conversion service (either specialized or basic) is used
     * for subsequent operations, while also avoiding repeated lookups or resource allocations
     * by caching the result.
     *
     * If neither {@link FormattingConversionService} nor {@link ConversionService} are available,
     * the cached service remains null.
     *
     * @throws IllegalStateException if no {@link ConversionService} is available for type conversion.
     */
    public void resolveConversionService(){
        if (cachedConversionService == null){
            FormattingConversionService fcs = formattingCsProvider.getIfAvailable();
            cachedConversionService = (fcs != null) ? fcs : conversionCsProvider.getIfAvailable();
            if(cachedConversionService == null){
                throw new IllegalStateException("No ConversionService available for type conversion");
            }
        }
    }

    /**
     * Retrieves the {@link Class} object representing the type of values
     * this converter handles during conversion operations.
     *
     * @param targetType the target type for which the converter is created.
     * @return the {@link Class} object corresponding to the type of
     *         values this converter is designed to operate on.
     */
    protected abstract Class<?> getValueTypeClass(Class<?> targetType);

    /**
     * Creates an instance of the target type based on the given value and target type class.
     * This method is intended to be implemented by subclasses to define the logic for
     * creating a specific instance of the target type.
     *
     * @param value the input value to be converted or represented as an instance of the target type
     * @param targetType the {@code Class} object representing the desired target type
     * @return an instance of the target type, created based on the provided value
     */
    protected abstract T createTargetInstance(Object value, Class<?> targetType);

}

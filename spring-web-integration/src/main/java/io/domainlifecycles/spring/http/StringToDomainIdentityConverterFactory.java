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

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionService;

/**
 * A converter factory that creates a converter from String to a given {@link Identity} type.
 *
 * @author Mario Herb
 */
public class StringToDomainIdentityConverterFactory extends ConversionServiceConverterFactory<Identity<?>> {

    /**
     * Constructs a {@code StringToDomainIdentityConverterFactory} using the provided object providers
     * for {@link FormattingConversionService} and {@link ConversionService}.
     *
     * @param formattingCsProvider the provider for {@link FormattingConversionService}, used to handle type conversions with formatting capabilities.
     * @param conversionCsProvider the provider for {@link ConversionService}, used as a fallback if a {@link FormattingConversionService} is not available.
     */
    public StringToDomainIdentityConverterFactory(
        ObjectProvider<FormattingConversionService> formattingCsProvider,
        ObjectProvider<ConversionService> conversionCsProvider
        ) {
        super(formattingCsProvider, conversionCsProvider);
    }

    /**
     * Retrieves the class type of the value that this converter factory works with (Identity value type).
     *
     * @param targetType the target {@link Identity} type for which the converter is created.
     * @return the {@code Class} object representing the type of values handled by the converter factory,
     * or {@code null} if the type cannot be determined.
     */
    @Override
    protected Class<?> getValueTypeClass(Class<?> targetType) {
        var idMirror = Domain.identityMirrorFor(targetType.getName());
        return DlcAccess.getClassForName(idMirror.getValueTypeName().orElseThrow());
    }

    /**
     * Creates an instance of the target {@link Identity} type using the provided value and target type information.
     *
     * @param value the value to be used to instantiate the {@link Identity} object.
     * @param targetType the class of the target {@link Identity} type to be instantiated.
     * @return a new instance of the target {@link Identity} type.
     */
    @Override
    protected Identity<?> createTargetInstance(Object value, Class<?> targetType) {
        return DlcAccess.newIdentityInstance(value, targetType.getName());
    }
}

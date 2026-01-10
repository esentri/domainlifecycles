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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Autoconfiguration for web/REST-related functionality.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 */
@AutoConfiguration(after = {DlcDomainAutoConfiguration.class})
public class DlcSpringWebAutoConfiguration {

    @Bean
    StringToDomainIdentityConverterFactory stringToDomainIdentityConverterFactory(
        ObjectProvider<FormattingConversionService> formattingConversionServiceObjectProvider,
        ObjectProvider<ConversionService> conversionServiceProvider
    ) {
        return new StringToDomainIdentityConverterFactory(formattingConversionServiceObjectProvider, conversionServiceProvider);
    }

    @Bean
    StringToDomainValueObjectConverterFactory stringToDomainValueObjectConverterFactory(
        ObjectProvider<FormattingConversionService> formattingConversionServiceObjectProvider,
        ObjectProvider<ConversionService> conversionServiceProvider
    ) {
        return new StringToDomainValueObjectConverterFactory(formattingConversionServiceObjectProvider, conversionServiceProvider);
    }

    @Bean
    WebMvcConfigurer dlcWebMvcConfigurer(StringToDomainIdentityConverterFactory identityConverterFactoryfactory,
                                         StringToDomainValueObjectConverterFactory valueObjectConverterFactory) {
        return new WebMvcConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverterFactory(identityConverterFactoryfactory);
                registry.addConverterFactory(valueObjectConverterFactory);
            }
        };
    }

    /**
     * Creates a default response entity builder for standardized API responses.
     * This builder provides a consistent format for REST API responses across
     * the application when using DLC domain objects.
     *
     * @return a new ResponseEntityBuilder instance for creating standardized responses
     */
    @ConditionalOnMissingBean(ResponseEntityBuilder.class)
    @Bean
    public ResponseEntityBuilder defaultResponseEntityBuilder() {
        return new DefaultResponseEntityBuilder();
    }
}

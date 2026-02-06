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

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Auto-configuration class for enabling Spring Web MVC-specific features within the DLC framework.
 * This configuration is loaded after the core domain configuration provided by {@code DlcDomainAutoConfiguration}.
 * It registers specialized beans and configurations required for web-specific functionality in the DLC framework.
 *
 * @author Mario Herb
 */
@AutoConfiguration(after = {DlcDomainAutoConfiguration.class})
public class DlcSpringWebAutoConfiguration {

    /**
     * Inner guarded configuration loaded only if WebMvcConfigurer exists on classpath
     */
    @AutoConfiguration
    @ConditionalOnClass(name = "org.springframework.web.servlet.config.annotation.WebMvcConfigurer")
    static class WebConfiguration {

        /**
         * Registers a {@link StringToDomainIdentityConverterFactory} bean for converting strings
         * into {@link Identity} types using provided conversion services.
         *
         * @param formattingConversionServiceProvider the provider for {@link FormattingConversionService},
         *                                            enabling type conversions with formatting capabilities.
         * @param conversionServiceProvider the provider for {@link ConversionService},
         *                                  used as a fallback if a {@link FormattingConversionService} is not available.
         * @return an instance of {@link StringToDomainIdentityConverterFactory} configured with the provided conversion services.
         */
        @Bean
        StringToDomainIdentityConverterFactory stringToDomainIdentityConverterFactory(
            ObjectProvider<FormattingConversionService> formattingConversionServiceProvider,
            ObjectProvider<ConversionService> conversionServiceProvider
        ) {
            return new StringToDomainIdentityConverterFactory(formattingConversionServiceProvider, conversionServiceProvider);
        }

        /**
         * Registers a {@code StringToDomainValueObjectConverterFactory} bean for converting strings
         * into {@link ValueObject} types using provided conversion services.
         *
         * @param formattingConversionServiceProvider the provider for {@link FormattingConversionService},
         *                                            enabling type conversions with formatting capabilities.
         * @param conversionServiceProvider the provider for {@link ConversionService},
         *                                  used as a fallback if a {@link FormattingConversionService} is not available.
         * @return an instance of {@link StringToDomainValueObjectConverterFactory} configured with the provided conversion services.
         */
        @Bean
        StringToDomainValueObjectConverterFactory stringToDomainValueObjectConverterFactory(
            ObjectProvider<FormattingConversionService> formattingConversionServiceProvider,
            ObjectProvider<ConversionService> conversionServiceProvider
        ) {
            return new StringToDomainValueObjectConverterFactory(formattingConversionServiceProvider, conversionServiceProvider);
        }

        /**
         * Configures a {@link WebMvcConfigurer} bean that registers converter factories
         * for converting strings into domain-specific {@link Identity} and {@link ValueObject} types.
         *
         * @param identityConverterFactory the {@link StringToDomainIdentityConverterFactory} instance used to handle
         *                                 conversions from strings to domain-specific {@link Identity} objects.
         * @param valueObjectConverterFactory the {@link StringToDomainValueObjectConverterFactory} instance used to handle
         *                                    conversions from strings to domain-specific {@link ValueObject} objects.
         * @return an instance of {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}
         *         with the configured formatters for handling domain-specific type conversions.
         */
        @Bean
        org.springframework.web.servlet.config.annotation.WebMvcConfigurer dlcWebMvcConfigurer(
            StringToDomainIdentityConverterFactory identityConverterFactory,
            StringToDomainValueObjectConverterFactory valueObjectConverterFactory
        ) {
            return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
                @Override
                public void addFormatters(@NonNull FormatterRegistry registry) {
                    registry.addConverterFactory(identityConverterFactory);
                    registry.addConverterFactory(valueObjectConverterFactory);
                }
            };
        }

        /**
         * Registers a default implementation of {@link ResponseEntityBuilder} as a Spring bean.
         * This bean is only created if no other {@link ResponseEntityBuilder} bean is already defined in the application context.
         *
         * @return an instance of {@link DefaultResponseEntityBuilder}, which is the default implementation of the {@link ResponseEntityBuilder} interface.
         */
        @Bean
        @ConditionalOnMissingBean(ResponseEntityBuilder.class)
        public ResponseEntityBuilder defaultResponseEntityBuilder() {
            return new DefaultResponseEntityBuilder();
        }
    }
}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import java.util.Objects;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * Autoconfiguration for web/REST-related functionality.
 *
 * @author leonvoellinger
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
@AutoConfigureAfter({DlcJacksonAutoConfiguration.class, JacksonAutoConfiguration.class, DlcDomainAutoConfiguration.class})
public class DlcSpringWebAutoConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    /**
     * Constructs the web configuration.
     *
     * @param objectMapper the Jackson object mapper used for serialization and deserialization
     */

    public DlcSpringWebAutoConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper instance is required");
    }

    /**
     * Registers converters for handling domain objects in Spring MVC.
     * This method enables automatic conversion of string parameters to DLC domain types
     * (ValueObjects and Identities) in REST controller methods.
     * <p>
     * The converters use the configured ObjectMapper to deserialize string representations
     * into proper domain object instances.
     * </p>
     *
     * @param registry the formatter registry to add converters to
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToDomainIdentityConverterFactory(objectMapper));
        registry.addConverterFactory(new StringToDomainValueObjectConverterFactory(objectMapper));
    }

    /**
     * Creates a default response entity builder for standardized API responses.
     * This builder provides a consistent format for REST API responses across
     * the application when using DLC domain objects.
     *
     * @return a new ResponseEntityBuilder instance for creating standardized responses
     */
    @ConditionalOnMissingBean
    @Bean
    public ResponseEntityBuilder defaultResponseEntityBuilder() {
        return new DefaultResponseEntityBuilder();
    }
}

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

package sampleshop.configuration.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Simple web configuration
 * adding some {@link org.springframework.core.convert.converter.ConverterFactory} instances
 * to simplify conversion of ValueObject and Identity types.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    /**
     * Create a new simple web configuration for Spring working with DLC.
     */
    public WebConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Spring integration to enable mapping of single valued ValueObjects or Ids,
     * which are represented as basic properties in RestController endpoints.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToDomainIdentityConverterFactory(objectMapper));
        registry.addConverterFactory(new StringToDomainValueObjectConverterFactory(objectMapper));
    }

    /**
     * Optional DLC response format.
     */
    @Bean
    public ResponseEntityBuilder defaultResponseEntityBuilder(){
        return new DefaultResponseEntityBuilder();
    }

}

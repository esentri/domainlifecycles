package io.domainlifecycles.springboot3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToDomainIdentityConverterFactory(objectMapper));
        registry.addConverterFactory(new StringToDomainValueObjectConverterFactory(objectMapper));
    }

    @Bean
    public ResponseEntityBuilder responseEntityBuilder() {
        return new DefaultResponseEntityBuilder();
    }
}


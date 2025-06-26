package io.domainlifecycles.autoconfig.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.spring.http.DefaultResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.StringToDomainIdentityConverterFactory;
import io.domainlifecycles.spring.http.StringToDomainValueObjectConverterFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(ObjectMapper.class)
@AutoConfigureAfter({DlcJacksonAutoConfiguration.class, JacksonAutoConfiguration.class, DlcDomainAutoConfiguration.class})
public class DlcSpringWebAutoConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public DlcSpringWebAutoConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper instance is required");
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
    @ConditionalOnMissingBean()
    @Bean
    public ResponseEntityBuilder defaultResponseEntityBuilder() {
        return new DefaultResponseEntityBuilder();
    }
}

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter(DlcJacksonAutoConfiguration.class)
public class DlcSpringOpenApiAutoConfiguration {

    /**
     * Spring Doc Open API integration of DLC
     */
    @Bean
    @ConditionalOnMissingBean(DlcOpenApiCustomizer.class)
    @ConditionalOnBean({SpringDocConfigProperties.class, DlcJacksonModule.class})
    public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
        return new DlcOpenApiCustomizer(springDocConfigProperties);
    }
}

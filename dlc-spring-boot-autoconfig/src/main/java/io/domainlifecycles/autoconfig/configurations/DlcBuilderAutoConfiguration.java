package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter(DlcDomainAutoConfiguration.class)
public class DlcBuilderAutoConfiguration {

    /**
     * Default configuration to make DLC work with inner builders or Lombok builders.
     */
    @Bean
    @ConditionalOnMissingBean(DomainObjectBuilderProvider.class)
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }
}

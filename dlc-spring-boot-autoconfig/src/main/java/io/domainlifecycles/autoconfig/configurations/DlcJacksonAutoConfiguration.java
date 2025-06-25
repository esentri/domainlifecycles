package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@AutoConfigureAfter({DlcJooqPersistenceAutoConfiguration.class, DlcBuilderAutoConfiguration.class, DlcDomainAutoConfiguration.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DlcJacksonAutoConfiguration {

    /**
     * DLC Jackson integration
     */
    @ConditionalOnMissingBean(DlcJacksonModule.class)
    @Bean
    @ConditionalOnBean({DomainObjectBuilderProvider.class, EntityIdentityProvider.class})
    DlcJacksonModule dlcModuleConfigurationWithEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                      DomainObjectBuilderProvider domainObjectBuilderProvider,
                                                                      EntityIdentityProvider entityIdentityProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }

    @ConditionalOnMissingBean({DlcJacksonModule.class, EntityIdentityProvider.class})
    @Bean
    @ConditionalOnBean(DomainObjectBuilderProvider.class)
    DlcJacksonModule dlcModuleConfigurationWithoutEntityIdentityProvider(List<? extends JacksonMappingCustomizer<?>> customizers,
                                                                         DomainObjectBuilderProvider domainObjectBuilderProvider
    ) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }
}

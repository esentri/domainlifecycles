package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DlcDomainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Domain.class)
    public DomainMirror initializedDomain() {
        if(!Domain.isInitialized()) {
            Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));
        }
        return Domain.getDomainMirror();
    }
}

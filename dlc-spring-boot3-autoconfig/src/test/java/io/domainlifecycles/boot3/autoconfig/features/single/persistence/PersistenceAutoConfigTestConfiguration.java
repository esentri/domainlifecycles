package io.domainlifecycles.boot3.autoconfig.features.single.persistence;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceAutoConfigTestConfiguration {

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }
}

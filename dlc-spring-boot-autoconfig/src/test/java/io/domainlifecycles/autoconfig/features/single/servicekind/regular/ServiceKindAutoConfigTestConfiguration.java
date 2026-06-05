package io.domainlifecycles.autoconfig.features.single.servicekind.regular;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tests.shared.persistence.PersistenceEventTestHelper;

@Configuration

public class ServiceKindAutoConfigTestConfiguration {

    @Bean
    PersistenceEventPublisher persistenceEventPublisher() {
        PersistenceEventTestHelper testHelper = new PersistenceEventTestHelper();
        return testHelper.testEventPublisher;
    }

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }
}

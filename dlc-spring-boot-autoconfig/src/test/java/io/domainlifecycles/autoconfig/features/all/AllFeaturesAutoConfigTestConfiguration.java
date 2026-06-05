package io.domainlifecycles.autoconfig.features.all;

import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import tests.shared.persistence.PersistenceEventTestHelper;

@Configuration
@DependsOn("initializedDomain")
public class AllFeaturesAutoConfigTestConfiguration {

    @Bean
    PersistenceEventPublisher persistenceEventPublisher() {
        PersistenceEventTestHelper testHelper = new PersistenceEventTestHelper();
        return testHelper.testEventPublisher;
    }
}

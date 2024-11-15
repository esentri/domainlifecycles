package io.domainlifecycles.springboot3.persistence.bestellung;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.springboot3.persistence.base.SpringPersistenceEventPublisher;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeansConfig {

    @Bean
    public BestellungBv3Repository bestellungRepository(
        DSLContext dslContext,
        SpringPersistenceEventPublisher springPersistenceEventPublisher,
        JooqDomainPersistenceProvider jooqDomainPersistenceProvider

    ) {
        return new BestellungBv3Repository(
            dslContext,
            springPersistenceEventPublisher,
            jooqDomainPersistenceProvider
        );
    }
}

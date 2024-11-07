package io.domainlifecycles.springboot2.persistence.bestellung;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.springboot2.persistence.base.SpringBoot2PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeansConfig {

    @Bean
    public BestellungRepository bestellungRepository(
        DSLContext dslContext,
        SpringBoot2PersistenceEventPublisher springPersistenceEventPublisher,
        JooqDomainPersistenceProvider jooqDomainPersistenceProvider

    ) {
        return new BestellungRepository(
            dslContext,
            springPersistenceEventPublisher,
            jooqDomainPersistenceProvider
        );
    }
}

package io.domainlifecycles.springboot.persistence.order;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.springboot.persistence.base.SpringPersistenceEventPublisher;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeansConfig {

    @Bean
    public OrderBv3Repository orderRepository(
        DSLContext dslContext,
        SpringPersistenceEventPublisher springPersistenceEventPublisher,
        JooqDomainPersistenceProvider jooqDomainPersistenceProvider

    ) {
        return new OrderBv3Repository(
            dslContext,
            springPersistenceEventPublisher,
            jooqDomainPersistenceProvider
        );
    }
}

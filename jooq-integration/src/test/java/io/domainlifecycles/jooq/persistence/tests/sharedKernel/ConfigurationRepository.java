package io.domainlifecycles.jooq.persistence.tests.sharedKernel;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.shared.one.Configuration;
import tests.shared.persistence.domain.shared.one.ConfigurationId;

public class ConfigurationRepository extends JooqAggregateRepository<Configuration, ConfigurationId> {

    public ConfigurationRepository(DSLContext dslContext,
                                   PersistenceEventPublisher persistenceEventPublisher,
                                   JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            Configuration.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }
}

package io.domainlifecycles.jdbc.persistence.tests.sharedKernel;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.shared.one.Configuration;
import tests.shared.persistence.domain.shared.one.ConfigurationId;

public class ConfigurationRepository extends JdbcAggregateRepository<Configuration, ConfigurationId> {

    public ConfigurationRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(Configuration.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

package io.domainlifecycles.jdbc.persistence.tests.temporal;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.temporal.TestRootTemporal;
import tests.shared.persistence.domain.temporal.TestRootTemporalId;

public class TemporalAggregateRootRepository extends JdbcAggregateRepository<TestRootTemporal, TestRootTemporalId> {

    public TemporalAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootTemporal.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

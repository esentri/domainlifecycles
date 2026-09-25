package io.domainlifecycles.jdbc.persistence.tests.ignoring;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoring;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoringId;

public class SimpleAggregateRootIgnoringRepository
    extends JdbcAggregateRepository<TestRootSimpleIgnoring, TestRootSimpleIgnoringId> {

    public SimpleAggregateRootIgnoringRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimpleIgnoring.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

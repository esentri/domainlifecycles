package io.domainlifecycles.jdbc.persistence.tests.simple;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

public class SimpleAggregateRootRepository extends JdbcAggregateRepository<TestRootSimple, TestRootSimpleId> {

    public SimpleAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimple.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

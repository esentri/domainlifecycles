package io.domainlifecycles.jdbc.persistence.tests.simpleUuid;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

public class SimpleUuidAggregateRootRepository
    extends JdbcAggregateRepository<TestRootSimpleUuid, TestRootSimpleUuidId> {

    public SimpleUuidAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimpleUuid.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

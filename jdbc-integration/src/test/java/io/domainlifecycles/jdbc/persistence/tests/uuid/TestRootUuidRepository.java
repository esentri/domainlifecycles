package io.domainlifecycles.jdbc.persistence.tests.uuid;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.uuid.TestRootUuid;

public class TestRootUuidRepository extends JdbcAggregateRepository<TestRootUuid, TestRootUuid.TestRootUuidId> {

    public TestRootUuidRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootUuid.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

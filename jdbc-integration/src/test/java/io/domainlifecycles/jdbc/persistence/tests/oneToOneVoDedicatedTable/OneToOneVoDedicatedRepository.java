package io.domainlifecycles.jdbc.persistence.tests.oneToOneVoDedicatedTable;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicatedId;

public class OneToOneVoDedicatedRepository
    extends JdbcAggregateRepository<TestRootOneToOneVoDedicated, TestRootOneToOneVoDedicatedId> {

    public OneToOneVoDedicatedRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToOneVoDedicated.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

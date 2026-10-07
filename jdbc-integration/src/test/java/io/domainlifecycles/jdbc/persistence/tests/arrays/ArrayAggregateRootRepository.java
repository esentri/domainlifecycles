package io.domainlifecycles.jdbc.persistence.tests.arrays;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.arrays.TestRootArray;
import tests.shared.persistence.domain.arrays.TestRootArrayId;

public class ArrayAggregateRootRepository extends JdbcAggregateRepository<TestRootArray, TestRootArrayId> {

    public ArrayAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootArray.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

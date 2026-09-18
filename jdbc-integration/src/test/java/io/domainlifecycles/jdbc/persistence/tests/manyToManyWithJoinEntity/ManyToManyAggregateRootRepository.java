package io.domainlifecycles.jdbc.persistence.tests.manyToManyWithJoinEntity;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

public class ManyToManyAggregateRootRepository
    extends JdbcAggregateRepository<TestRootManyToMany, TestRootManyToManyId> {

    public ManyToManyAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootManyToMany.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

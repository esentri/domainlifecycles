package io.domainlifecycles.jdbc.persistence.tests.oneToMany;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

public class OneToManyAggregateRootRepository extends JdbcAggregateRepository<TestRootOneToMany, TestRootOneToManyId> {

    public OneToManyAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToMany.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

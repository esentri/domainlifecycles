package io.domainlifecycles.jdbc.persistence.tests.oneToOneFollowingFK;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

public class OneToOneFollowingAggregateRootRepository
    extends JdbcAggregateRepository<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> {

    public OneToOneFollowingAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToOneFollowing.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

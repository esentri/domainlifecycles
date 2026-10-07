package io.domainlifecycles.jdbc.persistence.tests.oneToOneLeadingFK;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

public class OneToOneLeadingAggregateRootRepository
    extends JdbcAggregateRepository<TestRootOneToOneLeading, TestRootOneToOneLeadingId> {

    public OneToOneLeadingAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToOneLeading.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

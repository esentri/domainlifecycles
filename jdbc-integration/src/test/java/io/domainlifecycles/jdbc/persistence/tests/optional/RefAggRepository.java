package io.domainlifecycles.jdbc.persistence.tests.optional;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.optional.RefAgg;
import tests.shared.persistence.domain.optional.RefAggId;

public class RefAggRepository extends JdbcAggregateRepository<RefAgg, RefAggId> {

    public RefAggRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(RefAgg.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

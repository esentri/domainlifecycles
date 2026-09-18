package io.domainlifecycles.jdbc.persistence.tests.valueobjectsNested;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNested;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNestedId;

public class VoAggregateNestedRepository extends JdbcAggregateRepository<VoAggregateNested, VoAggregateNestedId> {

    public VoAggregateNestedRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateNested.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

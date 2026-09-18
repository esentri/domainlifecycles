package io.domainlifecycles.jdbc.persistence.tests.records;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;

public class RecordTestRepository extends JdbcAggregateRepository<RecordTest, RecordTestId> {

    public RecordTestRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(RecordTest.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

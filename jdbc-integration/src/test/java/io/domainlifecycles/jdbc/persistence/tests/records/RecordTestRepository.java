package io.domainlifecycles.jdbc.persistence.tests.records;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;

public class RecordTestRepository extends JdbcAggregateRepository<RecordTest, RecordTestId> {

    public RecordTestRepository(JdbcConnectionProvider connectionProvider,
                                 JdbcDialect dialect,
                                 JdbcSchemaMetadata schemaMetadata,
                                 JdbcDomainPersistenceProvider domainPersistenceProvider,
                                 PersistenceEventPublisher persistenceEventPublisher) {
        super(RecordTest.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

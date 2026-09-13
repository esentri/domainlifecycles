package io.domainlifecycles.jdbc.persistence.tests.ignoring;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoring;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoringId;

public class SimpleAggregateRootIgnoringRepository
    extends JdbcAggregateRepository<TestRootSimpleIgnoring, TestRootSimpleIgnoringId> {

    public SimpleAggregateRootIgnoringRepository(JdbcConnectionProvider connectionProvider,
                                                  JdbcDialect dialect,
                                                  JdbcSchemaMetadata schemaMetadata,
                                                  JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                  PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimpleIgnoring.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

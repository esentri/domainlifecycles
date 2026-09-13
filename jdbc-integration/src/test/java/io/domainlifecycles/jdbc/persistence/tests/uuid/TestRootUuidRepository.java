package io.domainlifecycles.jdbc.persistence.tests.uuid;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.uuid.TestRootUuid;

public class TestRootUuidRepository extends JdbcAggregateRepository<TestRootUuid, TestRootUuid.TestRootUuidId> {

    public TestRootUuidRepository(JdbcConnectionProvider connectionProvider,
                                   JdbcDialect dialect,
                                   JdbcSchemaMetadata schemaMetadata,
                                   JdbcDomainPersistenceProvider domainPersistenceProvider,
                                   PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootUuid.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

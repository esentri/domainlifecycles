package io.domainlifecycles.jdbc.persistence.tests.simpleUuid;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

public class SimpleUuidAggregateRootRepository
    extends JdbcAggregateRepository<TestRootSimpleUuid, TestRootSimpleUuidId> {

    public SimpleUuidAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                              JdbcDialect dialect,
                                              JdbcSchemaMetadata schemaMetadata,
                                              JdbcDomainPersistenceProvider domainPersistenceProvider,
                                              PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimpleUuid.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

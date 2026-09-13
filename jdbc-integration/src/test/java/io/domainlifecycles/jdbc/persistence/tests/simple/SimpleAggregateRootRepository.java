package io.domainlifecycles.jdbc.persistence.tests.simple;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

public class SimpleAggregateRootRepository extends JdbcAggregateRepository<TestRootSimple, TestRootSimpleId> {

    public SimpleAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                          JdbcDialect dialect,
                                          JdbcSchemaMetadata schemaMetadata,
                                          JdbcDomainPersistenceProvider domainPersistenceProvider,
                                          PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootSimple.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

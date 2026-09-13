package io.domainlifecycles.jdbc.persistence.tests.arrays;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.arrays.TestRootArray;
import tests.shared.persistence.domain.arrays.TestRootArrayId;

public class ArrayAggregateRootRepository extends JdbcAggregateRepository<TestRootArray, TestRootArrayId> {

    public ArrayAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                         JdbcDialect dialect,
                                         JdbcSchemaMetadata schemaMetadata,
                                         JdbcDomainPersistenceProvider domainPersistenceProvider,
                                         PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootArray.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

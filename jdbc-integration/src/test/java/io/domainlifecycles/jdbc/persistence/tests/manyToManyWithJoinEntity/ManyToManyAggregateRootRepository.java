package io.domainlifecycles.jdbc.persistence.tests.manyToManyWithJoinEntity;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

public class ManyToManyAggregateRootRepository
    extends JdbcAggregateRepository<TestRootManyToMany, TestRootManyToManyId> {

    public ManyToManyAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                              JdbcDialect dialect,
                                              JdbcSchemaMetadata schemaMetadata,
                                              JdbcDomainPersistenceProvider domainPersistenceProvider,
                                              PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootManyToMany.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

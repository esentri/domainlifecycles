package io.domainlifecycles.jdbc.persistence.tests.oneToMany;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

public class OneToManyAggregateRootRepository extends JdbcAggregateRepository<TestRootOneToMany, TestRootOneToManyId> {

    public OneToManyAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                             JdbcDialect dialect,
                                             JdbcSchemaMetadata schemaMetadata,
                                             JdbcDomainPersistenceProvider domainPersistenceProvider,
                                             PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToMany.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

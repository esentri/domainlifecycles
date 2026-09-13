package io.domainlifecycles.jdbc.persistence.tests.oneToOneFollowingFK;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

public class OneToOneFollowingAggregateRootRepository
    extends JdbcAggregateRepository<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> {

    public OneToOneFollowingAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                                     JdbcDialect dialect,
                                                     JdbcSchemaMetadata schemaMetadata,
                                                     JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                     PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToOneFollowing.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

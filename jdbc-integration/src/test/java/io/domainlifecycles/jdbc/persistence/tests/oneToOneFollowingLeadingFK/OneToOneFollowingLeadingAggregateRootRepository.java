package io.domainlifecycles.jdbc.persistence.tests.oneToOneFollowingLeadingFK;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

public class OneToOneFollowingLeadingAggregateRootRepository
    extends JdbcAggregateRepository<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> {

    public OneToOneFollowingLeadingAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                                            JdbcDialect dialect,
                                                            JdbcSchemaMetadata schemaMetadata,
                                                            JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                            PersistenceEventPublisher persistenceEventPublisher) {
        super(TestRootOneToOneFollowingLeading.class, connectionProvider, dialect, schemaMetadata,
            domainPersistenceProvider, persistenceEventPublisher);
    }
}

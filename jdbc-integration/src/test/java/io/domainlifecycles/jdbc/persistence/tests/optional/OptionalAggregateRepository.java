package io.domainlifecycles.jdbc.persistence.tests.optional;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.optional.OptionalAggregateId;

public class OptionalAggregateRepository extends JdbcAggregateRepository<OptionalAggregate, OptionalAggregateId> {

    public OptionalAggregateRepository(JdbcConnectionProvider connectionProvider,
                                        JdbcDialect dialect,
                                        JdbcSchemaMetadata schemaMetadata,
                                        JdbcDomainPersistenceProvider domainPersistenceProvider,
                                        PersistenceEventPublisher persistenceEventPublisher) {
        super(OptionalAggregate.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

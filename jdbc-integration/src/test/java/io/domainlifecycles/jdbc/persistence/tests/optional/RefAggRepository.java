package io.domainlifecycles.jdbc.persistence.tests.optional;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.optional.RefAgg;
import tests.shared.persistence.domain.optional.RefAggId;

public class RefAggRepository extends JdbcAggregateRepository<RefAgg, RefAggId> {

    public RefAggRepository(JdbcConnectionProvider connectionProvider,
                             JdbcDialect dialect,
                             JdbcSchemaMetadata schemaMetadata,
                             JdbcDomainPersistenceProvider domainPersistenceProvider,
                             PersistenceEventPublisher persistenceEventPublisher) {
        super(RefAgg.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

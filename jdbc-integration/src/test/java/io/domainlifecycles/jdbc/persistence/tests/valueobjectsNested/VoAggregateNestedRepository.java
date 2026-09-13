package io.domainlifecycles.jdbc.persistence.tests.valueobjectsNested;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNested;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNestedId;

public class VoAggregateNestedRepository extends JdbcAggregateRepository<VoAggregateNested, VoAggregateNestedId> {

    public VoAggregateNestedRepository(JdbcConnectionProvider connectionProvider,
                                        JdbcDialect dialect,
                                        JdbcSchemaMetadata schemaMetadata,
                                        JdbcDomainPersistenceProvider domainPersistenceProvider,
                                        PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateNested.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

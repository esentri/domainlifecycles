package io.domainlifecycles.jdbc.persistence.tests.valueobjectsPrimitive;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

public class VoAggregatePrimitiveRepository
    extends JdbcAggregateRepository<VoAggregatePrimitive, VoAggregatePrimitiveId> {

    public VoAggregatePrimitiveRepository(JdbcConnectionProvider connectionProvider,
                                          JdbcDialect dialect,
                                          JdbcSchemaMetadata schemaMetadata,
                                          JdbcDomainPersistenceProvider domainPersistenceProvider,
                                          PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregatePrimitive.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

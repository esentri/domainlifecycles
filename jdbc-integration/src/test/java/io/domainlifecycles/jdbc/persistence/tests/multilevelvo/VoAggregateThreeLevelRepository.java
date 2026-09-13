package io.domainlifecycles.jdbc.persistence.tests.multilevelvo;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;

public class VoAggregateThreeLevelRepository
    extends JdbcAggregateRepository<VoAggregateThreeLevel, VoAggregateThreeLevelId> {

    public VoAggregateThreeLevelRepository(JdbcConnectionProvider connectionProvider,
                                            JdbcDialect dialect,
                                            JdbcSchemaMetadata schemaMetadata,
                                            JdbcDomainPersistenceProvider domainPersistenceProvider,
                                            PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateThreeLevel.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

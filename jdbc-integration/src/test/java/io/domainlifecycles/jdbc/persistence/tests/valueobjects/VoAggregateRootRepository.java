package io.domainlifecycles.jdbc.persistence.tests.valueobjects;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoAggregateRootId;

public class VoAggregateRootRepository extends JdbcAggregateRepository<VoAggregateRoot, VoAggregateRootId> {

    public VoAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                      JdbcDialect dialect,
                                      JdbcSchemaMetadata schemaMetadata,
                                      JdbcDomainPersistenceProvider domainPersistenceProvider,
                                      PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateRoot.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

package io.domainlifecycles.jdbc.persistence.tests.valueobjectAutoMapping;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRootId;

public class AutoMappedVoAggregateRootRepository
    extends JdbcAggregateRepository<AutoMappedVoAggregateRoot, AutoMappedVoAggregateRootId> {

    public AutoMappedVoAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                                JdbcDialect dialect,
                                                JdbcSchemaMetadata schemaMetadata,
                                                JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                PersistenceEventPublisher persistenceEventPublisher) {
        super(AutoMappedVoAggregateRoot.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

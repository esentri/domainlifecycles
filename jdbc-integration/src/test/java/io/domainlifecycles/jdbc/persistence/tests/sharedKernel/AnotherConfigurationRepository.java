package io.domainlifecycles.jdbc.persistence.tests.sharedKernel;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.shared.another.AnotherConfiguration;
import tests.shared.persistence.domain.shared.another.AnotherConfigurationId;

public class AnotherConfigurationRepository
    extends JdbcAggregateRepository<AnotherConfiguration, AnotherConfigurationId> {

    public AnotherConfigurationRepository(JdbcConnectionProvider connectionProvider,
                                           JdbcDialect dialect,
                                           JdbcSchemaMetadata schemaMetadata,
                                           JdbcDomainPersistenceProvider domainPersistenceProvider,
                                           PersistenceEventPublisher persistenceEventPublisher) {
        super(AnotherConfiguration.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

package io.domainlifecycles.jdbc.persistence.tests.valueobjectTwoLevel;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevel;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevelId;

public class VoAggregateTwoLevelRepository
    extends JdbcAggregateRepository<VoAggregateTwoLevel, VoAggregateTwoLevelId> {

    public VoAggregateTwoLevelRepository(JdbcConnectionProvider connectionProvider,
                                          JdbcDialect dialect,
                                          JdbcSchemaMetadata schemaMetadata,
                                          JdbcDomainPersistenceProvider domainPersistenceProvider,
                                          PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateTwoLevel.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

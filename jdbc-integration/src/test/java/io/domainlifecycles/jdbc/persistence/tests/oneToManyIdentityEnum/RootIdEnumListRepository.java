package io.domainlifecycles.jdbc.persistence.tests.oneToManyIdentityEnum;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumListId;

public class RootIdEnumListRepository extends JdbcAggregateRepository<RootIdEnumList, RootIdEnumListId> {

    public RootIdEnumListRepository(JdbcConnectionProvider connectionProvider,
                                     JdbcDialect dialect,
                                     JdbcSchemaMetadata schemaMetadata,
                                     JdbcDomainPersistenceProvider domainPersistenceProvider,
                                     PersistenceEventPublisher persistenceEventPublisher) {
        super(RootIdEnumList.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

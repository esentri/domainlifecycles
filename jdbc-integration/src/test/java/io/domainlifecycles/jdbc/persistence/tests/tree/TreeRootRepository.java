package io.domainlifecycles.jdbc.persistence.tests.tree;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.tree.TreeRoot;
import tests.shared.persistence.domain.tree.TreeRootId;

public class TreeRootRepository extends JdbcAggregateRepository<TreeRoot, TreeRootId> {

    public TreeRootRepository(JdbcConnectionProvider connectionProvider,
                              JdbcDialect dialect,
                              JdbcSchemaMetadata schemaMetadata,
                              JdbcDomainPersistenceProvider domainPersistenceProvider,
                              PersistenceEventPublisher persistenceEventPublisher) {
        super(TreeRoot.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
    }
}

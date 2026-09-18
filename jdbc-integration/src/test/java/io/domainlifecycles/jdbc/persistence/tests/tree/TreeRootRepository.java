package io.domainlifecycles.jdbc.persistence.tests.tree;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.tree.TreeRoot;
import tests.shared.persistence.domain.tree.TreeRootId;

public class TreeRootRepository extends JdbcAggregateRepository<TreeRoot, TreeRootId> {

    public TreeRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(TreeRoot.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

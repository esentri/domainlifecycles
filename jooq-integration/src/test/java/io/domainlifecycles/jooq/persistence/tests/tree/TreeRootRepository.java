package io.domainlifecycles.jooq.persistence.tests.tree;


import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.tree.TreeRoot;
import tests.shared.persistence.domain.tree.TreeRootId;

@Slf4j
public class TreeRootRepository extends JooqAggregateRepository<TreeRoot, TreeRootId> {

    public TreeRootRepository(DSLContext dslContext, JooqDomainPersistenceProvider domainPersistenceProvider,
                              PersistenceEventPublisher persistenceEventPublisher) {
        super(TreeRoot.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }
}

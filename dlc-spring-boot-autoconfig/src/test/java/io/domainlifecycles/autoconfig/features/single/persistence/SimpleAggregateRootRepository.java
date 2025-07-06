package io.domainlifecycles.autoconfig.features.single.persistence;

import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;


public class SimpleAggregateRootRepository extends JooqAggregateRepository<TestRootSimple, TestRootSimpleId> {

    public SimpleAggregateRootRepository(DSLContext dslContext,
                                         PersistenceEventPublisher persistenceEventPublisher,
                                         JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootSimple.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);
    }
}

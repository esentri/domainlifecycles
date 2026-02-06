package io.domainlifecycles.boot3.autoconfig.features.single.persistence;

import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
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

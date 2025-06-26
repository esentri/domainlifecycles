package io.domainlifecycles.autoconfig.features.single.persistence;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;


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

package io.domainlifecycles.jooq.persistence.tests.simple;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;


public class SimpleAggregateRootRepository extends JooqAggregateRepository<TestRootSimple, TestRootSimpleId> {

    private static final Logger log = LoggerFactory.getLogger(SimpleAggregateRootRepository.class);

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

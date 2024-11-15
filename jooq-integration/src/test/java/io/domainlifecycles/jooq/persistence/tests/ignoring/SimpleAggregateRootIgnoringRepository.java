package io.domainlifecycles.jooq.persistence.tests.ignoring;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoring;
import tests.shared.persistence.domain.ignoring.TestRootSimpleIgnoringId;


public class SimpleAggregateRootIgnoringRepository extends JooqAggregateRepository<TestRootSimpleIgnoring,
    TestRootSimpleIgnoringId> {

    private static final Logger log = LoggerFactory.getLogger(SimpleAggregateRootIgnoringRepository.class);

    public SimpleAggregateRootIgnoringRepository(DSLContext dslContext,
                                                 PersistenceEventPublisher persistenceEventPublisher,
                                                 JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootSimpleIgnoring.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

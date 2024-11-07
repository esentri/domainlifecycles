package io.domainlifecycles.jooq.persistence.tests.simpleUuid;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

public class SimpleUuidAggregateRootRepository extends JooqAggregateRepository<TestRootSimpleUuid,
    TestRootSimpleUuidId> {


    private static final Logger log = LoggerFactory.getLogger(SimpleUuidAggregateRootRepository.class);

    public SimpleUuidAggregateRootRepository(DSLContext dslContext,
                                             PersistenceEventPublisher persistenceEventPublisher,
                                             JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootSimpleUuid.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);
    }

}

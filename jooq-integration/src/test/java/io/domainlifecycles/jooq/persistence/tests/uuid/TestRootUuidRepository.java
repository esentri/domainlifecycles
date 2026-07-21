package io.domainlifecycles.jooq.persistence.tests.uuid;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.uuid.TestRootUuid;

public class TestRootUuidRepository extends JooqAggregateRepository<TestRootUuid, TestRootUuid.TestRootUuidId> {

    private static final Logger log = LoggerFactory.getLogger(TestRootUuidRepository.class);
    public TestRootUuidRepository(DSLContext dslContext,
                                  PersistenceEventPublisher persistenceEventPublisher,
                                  JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootUuid.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

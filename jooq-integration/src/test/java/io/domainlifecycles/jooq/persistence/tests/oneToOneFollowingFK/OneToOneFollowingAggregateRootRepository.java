package io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingFK;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

public class OneToOneFollowingAggregateRootRepository extends JooqAggregateRepository<TestRootOneToOneFollowing,
    TestRootOneToOneFollowingId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OneToOneFollowingAggregateRootRepository.class);

    public OneToOneFollowingAggregateRootRepository(DSLContext dslContext,
                                                    PersistenceEventPublisher persistenceEventPublisher,
                                                    JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootOneToOneFollowing.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

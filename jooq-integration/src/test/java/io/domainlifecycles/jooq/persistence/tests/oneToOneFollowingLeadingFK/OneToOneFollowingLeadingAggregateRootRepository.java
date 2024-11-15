package io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingLeadingFK;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

public class OneToOneFollowingLeadingAggregateRootRepository extends JooqAggregateRepository<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(
        OneToOneFollowingLeadingAggregateRootRepository.class);

    public OneToOneFollowingLeadingAggregateRootRepository(DSLContext dslContext,
                                                           PersistenceEventPublisher persistenceEventPublisher,
                                                           JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootOneToOneFollowingLeading.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

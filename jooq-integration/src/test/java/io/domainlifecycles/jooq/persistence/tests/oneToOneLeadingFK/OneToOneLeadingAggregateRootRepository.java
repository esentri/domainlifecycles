package io.domainlifecycles.jooq.persistence.tests.oneToOneLeadingFK;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

public class OneToOneLeadingAggregateRootRepository extends JooqAggregateRepository<TestRootOneToOneLeading,
    TestRootOneToOneLeadingId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OneToOneLeadingAggregateRootRepository.class);

    public OneToOneLeadingAggregateRootRepository(DSLContext dslContext,
                                                  PersistenceEventPublisher persistenceEventPublisher,
                                                  JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootOneToOneLeading.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );

    }

}

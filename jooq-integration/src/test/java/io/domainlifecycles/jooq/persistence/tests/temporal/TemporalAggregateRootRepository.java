package io.domainlifecycles.jooq.persistence.tests.temporal;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.temporal.TestRootTemporal;
import tests.shared.persistence.domain.temporal.TestRootTemporalId;

@Slf4j
public class TemporalAggregateRootRepository extends JooqAggregateRepository<TestRootTemporal, TestRootTemporalId> {


    public TemporalAggregateRootRepository(DSLContext dslContext,
                                           PersistenceEventPublisher persistenceEventPublisher,
                                           JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootTemporal.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);
    }

}

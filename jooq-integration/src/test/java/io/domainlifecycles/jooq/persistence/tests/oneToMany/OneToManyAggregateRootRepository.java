package io.domainlifecycles.jooq.persistence.tests.oneToMany;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

public class OneToManyAggregateRootRepository extends JooqAggregateRepository<TestRootOneToMany, TestRootOneToManyId> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OneToManyAggregateRootRepository.class);

    public OneToManyAggregateRootRepository(DSLContext dslContext,
                                            PersistenceEventPublisher persistenceEventPublisher,
                                            JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootOneToMany.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

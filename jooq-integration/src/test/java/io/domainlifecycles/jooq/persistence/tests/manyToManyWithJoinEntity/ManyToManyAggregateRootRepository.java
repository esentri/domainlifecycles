package io.domainlifecycles.jooq.persistence.tests.manyToManyWithJoinEntity;


import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

public class ManyToManyAggregateRootRepository extends JooqAggregateRepository<TestRootManyToMany,
    TestRootManyToManyId> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ManyToManyAggregateRootRepository.class);

    public ManyToManyAggregateRootRepository(DSLContext dslContext,
                                             PersistenceEventPublisher persistenceEventPublisher,
                                             JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootManyToMany.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }


}

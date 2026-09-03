package io.domainlifecycles.jooq.persistence.tests.arrays;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.arrays.TestRootArray;
import tests.shared.persistence.domain.arrays.TestRootArrayId;

public class ArrayAggregateRootRepository extends JooqAggregateRepository<TestRootArray, TestRootArrayId> {

    public ArrayAggregateRootRepository(DSLContext dslContext,
                                        PersistenceEventPublisher persistenceEventPublisher,
                                        JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootArray.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

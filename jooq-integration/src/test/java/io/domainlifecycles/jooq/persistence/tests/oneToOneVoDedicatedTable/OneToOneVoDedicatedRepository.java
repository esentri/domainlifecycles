package io.domainlifecycles.jooq.persistence.tests.oneToOneVoDedicatedTable;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicatedId;

public class OneToOneVoDedicatedRepository extends JooqAggregateRepository<TestRootOneToOneVoDedicated,
    TestRootOneToOneVoDedicatedId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OneToOneVoDedicatedRepository.class);

    public OneToOneVoDedicatedRepository(DSLContext dslContext,
                                         PersistenceEventPublisher persistenceEventPublisher,
                                         JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootOneToOneVoDedicated.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );

    }

}

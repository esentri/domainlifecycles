package io.domainlifecycles.jooq.persistence.tests.records;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;


public class RecordTestRepository extends JooqAggregateRepository<RecordTest, RecordTestId> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RecordTestRepository.class);

    public RecordTestRepository(DSLContext dslContext,
                                PersistenceEventPublisher persistenceEventPublisher,
                                JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            RecordTest.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

package io.domainlifecycles.jooq.persistence.tests.optional;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.optional.RefAgg;
import tests.shared.persistence.domain.optional.RefAggId;

public class RefAggRepository extends JooqAggregateRepository<RefAgg, RefAggId> {


    private static final Logger log = LoggerFactory.getLogger(RefAggRepository.class);

    public RefAggRepository(DSLContext dslContext,
                            PersistenceEventPublisher persistenceEventPublisher,
                            JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            RefAgg.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

package io.domainlifecycles.jooq.persistence.tests.valueobjectsPrimitive;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

public class VoAggregatePrimitiveRepository extends JooqAggregateRepository<VoAggregatePrimitive,
    VoAggregatePrimitiveId> {

    private static final Logger log = LoggerFactory.getLogger(VoAggregatePrimitiveRepository.class);

    public VoAggregatePrimitiveRepository(DSLContext dslContext,
                                          PersistenceEventPublisher persistenceEventPublisher,
                                          JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            VoAggregatePrimitive.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

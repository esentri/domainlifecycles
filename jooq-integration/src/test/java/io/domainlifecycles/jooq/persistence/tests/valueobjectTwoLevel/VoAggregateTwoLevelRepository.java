package io.domainlifecycles.jooq.persistence.tests.valueobjectTwoLevel;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevel;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevelId;

public class VoAggregateTwoLevelRepository extends JooqAggregateRepository<VoAggregateTwoLevel, VoAggregateTwoLevelId> {

    private static final Logger log = LoggerFactory.getLogger(VoAggregateTwoLevelRepository.class);

    public VoAggregateTwoLevelRepository(DSLContext dslContext,
                                         PersistenceEventPublisher persistenceEventPublisher,
                                         JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            VoAggregateTwoLevel.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

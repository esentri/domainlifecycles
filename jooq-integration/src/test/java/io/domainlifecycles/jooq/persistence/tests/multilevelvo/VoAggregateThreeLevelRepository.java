package io.domainlifecycles.jooq.persistence.tests.multilevelvo;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;

public class VoAggregateThreeLevelRepository extends JooqAggregateRepository<VoAggregateThreeLevel,
    VoAggregateThreeLevelId> {

    private static final Logger log = LoggerFactory.getLogger(VoAggregateThreeLevelRepository.class);

    public VoAggregateThreeLevelRepository(DSLContext dslContext,
                                           PersistenceEventPublisher persistenceEventPublisher,
                                           JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            VoAggregateThreeLevel.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher

        );
    }

}

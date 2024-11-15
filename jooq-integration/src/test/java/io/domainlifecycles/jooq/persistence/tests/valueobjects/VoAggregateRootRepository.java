package io.domainlifecycles.jooq.persistence.tests.valueobjects;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoAggregateRootId;

public class VoAggregateRootRepository extends JooqAggregateRepository<VoAggregateRoot, VoAggregateRootId> {

    private static final Logger log = LoggerFactory.getLogger(VoAggregateRootRepository.class);

    public VoAggregateRootRepository(DSLContext dslContext,
                                     PersistenceEventPublisher persistenceEventPublisher,
                                     JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            VoAggregateRoot.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

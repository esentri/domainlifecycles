package io.domainlifecycles.jooq.persistence.tests.valueobjectAutoMapping;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRootId;

public class AutoMappedVoAggregateRootRepository extends JooqAggregateRepository<AutoMappedVoAggregateRoot,
    AutoMappedVoAggregateRootId> {

    private static final Logger log = LoggerFactory.getLogger(AutoMappedVoAggregateRootRepository.class);

    public AutoMappedVoAggregateRootRepository(DSLContext dslContext,
                                               PersistenceEventPublisher persistenceEventPublisher,
                                               JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            AutoMappedVoAggregateRoot.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

package io.domainlifecycles.jooq.persistence.tests.valueobjectsNested;


import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNested;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNestedId;

public class VoAggregateNestedRepository extends JooqAggregateRepository<VoAggregateNested, VoAggregateNestedId> {

    private static final Logger log = LoggerFactory.getLogger(VoAggregateNestedRepository.class);

    public VoAggregateNestedRepository(DSLContext dslContext,
                                       PersistenceEventPublisher persistenceEventPublisher,
                                       JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            VoAggregateNested.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }

}

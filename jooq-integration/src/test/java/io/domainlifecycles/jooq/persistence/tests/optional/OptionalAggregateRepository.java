package io.domainlifecycles.jooq.persistence.tests.optional;


import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.optional.OptionalAggregateId;

public class OptionalAggregateRepository extends JooqAggregateRepository<OptionalAggregate, OptionalAggregateId> {


    private static final Logger log = LoggerFactory.getLogger(OptionalAggregateRepository.class);

    public OptionalAggregateRepository(DSLContext dslContext,
                                       PersistenceEventPublisher persistenceEventPublisher,
                                       JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            OptionalAggregate.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }

}

package io.domainlifecycles.jooq.persistence.tests.sharedKernel;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import tests.shared.persistence.domain.shared.another.AnotherConfiguration;
import tests.shared.persistence.domain.shared.another.AnotherConfigurationId;

public class AnotherConfigurationRepository extends JooqAggregateRepository<AnotherConfiguration,
    AnotherConfigurationId> {

    public AnotherConfigurationRepository(DSLContext dslContext,
                                          PersistenceEventPublisher persistenceEventPublisher,
                                          JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            AnotherConfiguration.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }
}

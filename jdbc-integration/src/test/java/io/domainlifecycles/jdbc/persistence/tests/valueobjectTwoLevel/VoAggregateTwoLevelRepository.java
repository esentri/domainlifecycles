package io.domainlifecycles.jdbc.persistence.tests.valueobjectTwoLevel;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevel;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevelId;

public class VoAggregateTwoLevelRepository
    extends JdbcAggregateRepository<VoAggregateTwoLevel, VoAggregateTwoLevelId> {

    public VoAggregateTwoLevelRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateTwoLevel.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

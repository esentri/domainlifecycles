package io.domainlifecycles.jdbc.persistence.tests.multilevelvo;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;

public class VoAggregateThreeLevelRepository
    extends JdbcAggregateRepository<VoAggregateThreeLevel, VoAggregateThreeLevelId> {

    public VoAggregateThreeLevelRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateThreeLevel.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

package io.domainlifecycles.jdbc.persistence.tests.valueobjects;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoAggregateRootId;

public class VoAggregateRootRepository extends JdbcAggregateRepository<VoAggregateRoot, VoAggregateRootId> {

    public VoAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregateRoot.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

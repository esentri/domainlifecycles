package io.domainlifecycles.jdbc.persistence.tests.valueobjectAutoMapping;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRootId;

public class AutoMappedVoAggregateRootRepository
    extends JdbcAggregateRepository<AutoMappedVoAggregateRoot, AutoMappedVoAggregateRootId> {

    public AutoMappedVoAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(AutoMappedVoAggregateRoot.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

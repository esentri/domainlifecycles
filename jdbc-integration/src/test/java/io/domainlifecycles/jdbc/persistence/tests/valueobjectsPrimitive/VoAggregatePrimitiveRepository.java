package io.domainlifecycles.jdbc.persistence.tests.valueobjectsPrimitive;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

public class VoAggregatePrimitiveRepository
    extends JdbcAggregateRepository<VoAggregatePrimitive, VoAggregatePrimitiveId> {

    public VoAggregatePrimitiveRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(VoAggregatePrimitive.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

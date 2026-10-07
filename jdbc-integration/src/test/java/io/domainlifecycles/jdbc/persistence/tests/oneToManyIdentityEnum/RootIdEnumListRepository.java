package io.domainlifecycles.jdbc.persistence.tests.oneToManyIdentityEnum;

import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumListId;

public class RootIdEnumListRepository extends JdbcAggregateRepository<RootIdEnumList, RootIdEnumListId> {

    public RootIdEnumListRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                       PersistenceEventPublisher persistenceEventPublisher) {
        super(RootIdEnumList.class, domainPersistenceProvider, persistenceEventPublisher);
    }
}

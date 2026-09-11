package test.domain;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;

import java.util.List;

public class MyRepositoryImpl extends JooqAggregateRepository<MyAggregateRoot, MyAggregateRoot.Id> implements MyRepository {
    /**
     * Constructs an instance of JooqAggregateRepository.
     *
     * @param aggregateRootClass        the class type of the aggregate root
     * @param dslContext                the DSLContext used for database interaction
     * @param domainPersistenceProvider the provider for domain-specific persistence actions
     * @param persistenceEventPublisher the publisher for persistence events
     */
    public MyRepositoryImpl(Class<MyAggregateRoot> aggregateRootClass,
                            DSLContext dslContext,
                            JooqDomainPersistenceProvider domainPersistenceProvider, PersistenceEventPublisher persistenceEventPublisher) {
        super(aggregateRootClass, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }

    @Override
    public List<MyAggregateRoot> findSome(String searchName) {
        return null;
    }

    @Override
    public void someInheritedOperation() {
        // calls a domain method that appears NOWHERE else, to make the descent provable
        update(null);   // or any unique domain call available in the impl
    }
}

package io.domainlifecycles.reflect;

import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.DomainStructureAwareRepository;
import io.domainlifecycles.persistence.repository.persister.Persister;

public class ReflectAggregateRepositoryImpl extends DomainStructureAwareRepository<ReflectAggregate.ReflectAggregateId, ReflectAggregate, Object> implements ReflectAggregateRepository {
    protected ReflectAggregateRepositoryImpl(Persister<Object> persister,
                                             DomainPersistenceProvider<Object> domainPersistenceProvider) {
        super(persister, domainPersistenceProvider);
    }

    @Override
    public FetcherResult<ReflectAggregate, Object> findResultById(ReflectAggregate.ReflectAggregateId id) {
        return null;
    }
}

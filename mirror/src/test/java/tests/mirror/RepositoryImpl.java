package tests.mirror;

import java.util.Optional;

public class RepositoryImpl implements RepositoryInterface{
    @Override
    public Optional<AggregateRootInterface> findById(IdentityInterface identityInterface) {
        return Optional.empty();
    }

    @Override
    public AggregateRootInterface insert(AggregateRootInterface aggregateRoot) {
        return null;
    }

    @Override
    public AggregateRootInterface update(AggregateRootInterface aggregateRoot) {
        return null;
    }

    @Override
    public Optional<AggregateRootInterface> deleteById(IdentityInterface identityInterface) {
        return Optional.empty();
    }
}

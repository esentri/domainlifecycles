package fixtures.backwardflow;

import java.util.Optional;

public class TargetRepositoryImpl implements TargetRepository {

    @Override
    public Optional<TargetAggregate> findById(TargetAggregateId id) {
        return Optional.empty();
    }

    @Override
    public TargetAggregate insert(TargetAggregate aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public TargetAggregate update(TargetAggregate aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Optional<TargetAggregate> deleteById(TargetAggregateId id) {
        return Optional.empty();
    }
}

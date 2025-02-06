package sample;

import io.domainlifecycles.domain.types.Repository;

import java.util.Optional;

public class TestRepository implements Repository<TestIdentity, TestAggregateRoot> {
    @Override
    public Optional<TestAggregateRoot> findById(TestIdentity testIdentity) {
        return Optional.empty();
    }

    @Override
    public TestAggregateRoot insert(TestAggregateRoot aggregateRoot) {
        return null;
    }

    @Override
    public TestAggregateRoot update(TestAggregateRoot aggregateRoot) {
        return null;
    }

    @Override
    public Optional<TestAggregateRoot> deleteById(TestIdentity testIdentity) {
        return Optional.empty();
    }
}

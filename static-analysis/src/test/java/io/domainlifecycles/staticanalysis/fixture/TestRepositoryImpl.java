package io.domainlifecycles.staticanalysis.fixture;

import java.util.Optional;

public class TestRepositoryImpl implements TestRepository {

    @Override
    public Optional<TestAggregate> findById(TestAggregateId id) {
        return Optional.empty();
    }

    @Override
    public TestAggregate insert(TestAggregate aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public TestAggregate update(TestAggregate aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Optional<TestAggregate> deleteById(TestAggregateId id) {
        return Optional.empty();
    }
}

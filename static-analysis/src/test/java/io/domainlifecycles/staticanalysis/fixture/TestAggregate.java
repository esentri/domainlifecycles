package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class TestAggregate extends AggregateRootBase<TestAggregateId> {

    private final TestAggregateId id;

    public TestAggregate(TestAggregateId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}

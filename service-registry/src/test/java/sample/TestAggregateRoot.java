package sample;

import io.domainlifecycles.domain.types.AggregateRoot;

public class TestAggregateRoot implements AggregateRoot<TestIdentity> {
    @Override
    public TestIdentity id() {
        return null;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}

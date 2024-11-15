package io.domainlifecycles.reflect;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;


public class ReflectAggregate implements AggregateRoot<ReflectAggregate.ReflectAggregateId> {
    public record ReflectAggregateId(Long value) implements Identity<Long> {
    }

    public String value;
    public ReflectAggregateId id;
    public long concurrencyVersion;

    @Override
    public ReflectAggregateId id() {
        return id;
    }

    @Override
    public long concurrencyVersion() {
        return concurrencyVersion;
    }


}

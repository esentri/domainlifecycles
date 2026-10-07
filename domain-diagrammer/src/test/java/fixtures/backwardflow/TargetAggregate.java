package fixtures.backwardflow;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class TargetAggregate extends AggregateRootBase<TargetAggregateId> {

    private final TargetAggregateId id;

    public TargetAggregate(TargetAggregateId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}

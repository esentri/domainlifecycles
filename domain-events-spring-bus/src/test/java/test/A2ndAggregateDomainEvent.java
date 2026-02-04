package test;

import io.domainlifecycles.domain.types.AggregateDomainEvent;

public record A2ndAggregateDomainEvent(
    String message) implements AggregateDomainEvent<AnAggregate.AggregateId, AnAggregate> {

    @Override
    public AnAggregate.AggregateId targetId() {
        return new AnAggregate.AggregateId(1L);
    }
}

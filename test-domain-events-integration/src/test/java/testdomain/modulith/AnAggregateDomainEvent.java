package testdomain.modulith;

import io.domainlifecycles.domain.types.AggregateDomainEvent;

public record AnAggregateDomainEvent(
    String message, AnAggregate.AggregateId targetId) implements AggregateDomainEvent<AnAggregate.AggregateId, AnAggregate> {
}

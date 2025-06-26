package io.domainlifecycles.autoconfig.features.single.events.services;

import io.domainlifecycles.domain.types.AggregateDomainEvent;

public record AnAggregateDomainEvent(
    String message) implements AggregateDomainEvent<AnAggregate.AggregateId, AnAggregate> {

    @Override
    public AnAggregate.AggregateId targetId() {
        return new AnAggregate.AggregateId(1L);
    }
}

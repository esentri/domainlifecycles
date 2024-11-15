package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnAggregate extends AggregateRootBase<AnAggregate.AggregateId> {

    public record AggregateId(Long value) implements Identity<Long> {
    }

    private AggregateId id;
    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    protected AnAggregate(AggregateId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    @ListensTo(domainEventType = AnAggregateDomainEvent.class)
    public void onEvent(AnAggregateDomainEvent domainEvent) {
        if (domainEvent.message().startsWith("TestAggregateDomainWithException")) {
            throw new RuntimeException("Provoked runtime error!");
        }
        received.add(domainEvent);
    }

}

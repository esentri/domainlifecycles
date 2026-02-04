package io.domainlifecycles.autoconfig.model.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.QueryHandler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AQueryHandler implements QueryHandler {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @DomainEventListener
    public void onADomainEvent(ADomainEvent domainEvent) {
        System.out.println("ADomainEvent received in AQueryHandler! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}

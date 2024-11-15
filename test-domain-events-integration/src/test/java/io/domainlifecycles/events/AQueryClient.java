package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.QueryClient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AQueryClient implements QueryClient {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent) {
        System.out.println("ADomainEvent received in AQueryClient! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}

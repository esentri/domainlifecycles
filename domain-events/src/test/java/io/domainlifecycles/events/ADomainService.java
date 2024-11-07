package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.ListensTo;

import java.util.ArrayList;
import java.util.List;

public class ADomainService implements DomainService {

    public List<DomainEvent> received = new ArrayList<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onDomainEvent(ADomainEvent domainEvent) {
        if (domainEvent.message().startsWith("TestDomainServiceRollback")) {
            throw new RuntimeException("Provoked error!");
        }
        System.out.println("ADomainEvent received in ADomainService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

    @ListensTo(domainEventType = PassThroughDomainEvent.class)
    public void onDomainEvent(PassThroughDomainEvent domainEvent) {
        System.out.println("PassThroughDomainEvent received in ADomainService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }
}

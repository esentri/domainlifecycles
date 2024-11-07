package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.OutboundService;

import java.util.ArrayList;
import java.util.List;

public class AnOutboundService implements OutboundService {

    public List<DomainEvent> received = new ArrayList<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent) {
        System.out.println("ADomainEvent received in AnOutboundService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

    @ListensTo(domainEventType = PassThroughDomainEvent.class)
    public void onDomainEvent(PassThroughDomainEvent domainEvent) {
        System.out.println("PassThroughDomainEvent received in AnOutboundService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }
}

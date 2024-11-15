package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.OutboundService;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class AnOutboundService implements OutboundService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent){
        log.debug("ADomainEvent received in AnOutboundService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}

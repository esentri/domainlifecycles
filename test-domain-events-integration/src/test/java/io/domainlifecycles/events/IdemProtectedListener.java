package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class IdemProtectedListener implements ApplicationService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @ListensTo(domainEventType = IdemProtectedDomainEvent.class)
    public void handle(IdemProtectedDomainEvent domainEvent){
        log.debug("{} received!", domainEvent);
        received.add(domainEvent);
    }
}

package io.domainlifecycles.autoconfig.features.single.events.services;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.ListensTo;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ADomainService implements DomainService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onDomainEvent(ADomainEvent domainEvent) {
        if (domainEvent.message().startsWith("TestDomainServiceRollback")) {
            throw new RuntimeException("Provoked error!");
        }
        log.debug("ADomainEvent received in ADomainService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}

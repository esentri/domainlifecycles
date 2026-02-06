package io.domainlifecycles.boot3.autoconfig.model.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class ADomainService implements DomainService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @DomainEventListener
    public void onDomainEvent(ADomainEvent domainEvent) {
        if (domainEvent.message().startsWith("TestDomainServiceRollback")) {
            throw new RuntimeException("Provoked error!");
        }
        log.debug("ADomainEvent received in ADomainService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}
